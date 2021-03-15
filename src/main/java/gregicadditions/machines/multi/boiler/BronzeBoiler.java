package gregicadditions.machines.multi.boiler;

import codechicken.lib.raytracer.CuboidRayTraceResult;
import codechicken.lib.render.CCRenderState;
import codechicken.lib.render.pipeline.IVertexOperation;
import codechicken.lib.vec.Matrix4;
import gregicadditions.capabilities.impl.GAMultiblockRecipeLogic;
import gregicadditions.capabilities.impl.GARecipeMapMultiblockController;
import gregicadditions.item.GAMetaBlocks;
import gregicadditions.recipes.GARecipeMaps;
import gregtech.api.GTValues;
import gregtech.api.capability.GregtechCapabilities;
import gregtech.api.capability.IFuelInfo;
import gregtech.api.capability.IMultipleTankHandler;
import gregtech.api.capability.impl.*;
import gregtech.api.capability.tool.ISoftHammerItem;
import gregtech.api.gui.Widget;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.metatileentity.MetaTileEntityHolder;
import gregtech.api.metatileentity.multiblock.IMultiblockPart;
import gregtech.api.metatileentity.multiblock.MultiblockAbility;
import gregtech.api.metatileentity.multiblock.RecipeMapMultiblockController;
import gregtech.api.multiblock.BlockPattern;
import gregtech.api.multiblock.FactoryBlockPattern;
import gregtech.api.multiblock.PatternMatchContext;
import gregtech.api.recipes.ModHandler;
import gregtech.api.recipes.Recipe;
import gregtech.api.recipes.RecipeMaps;
import gregtech.api.recipes.recipes.FuelRecipe;
import gregtech.api.render.ICubeRenderer;
import gregtech.api.render.Textures;
import gregtech.common.blocks.BlockFireboxCasing;
import gregtech.common.tools.DamageValues;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.*;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidTank;
import net.minecraftforge.items.IItemHandlerModifiable;


import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;

import static gregtech.api.gui.widgets.AdvancedTextWidget.withButton;
import static gregtech.api.gui.widgets.AdvancedTextWidget.withHoverTextTranslate;
import static gregtech.api.unification.material.Materials.Bronze;

public class BronzeBoiler extends GARecipeMapMultiblockController {

    private static final int CONSUMPTION_MULTIPLIER = 100;
    private static final int BOILING_TEMPERATURE = 100;

    private int temperature;
    private int maxTemperature = 1600;
    public final int temperatureEffBuff = 30;
    public final float fuelConsumptionMultiplier = 1.0f;
    private int throttlePercentage = 100;
    private int fuelBurnTicksLeft;
    private int baseFluidOutput;
    private boolean isActive;
    private boolean wasActiveAndNeedsUpdate;
    private boolean hasNoInput;
    private int lastTickFluidOutput;
    private FluidTankList fluidInputInventory;
    private ItemHandlerList itemFuelInventory;
    private FluidTankList outputTank;

    public BronzeBoiler(ResourceLocation metaTileEntityId) {
        super(metaTileEntityId, GARecipeMaps.BOILER_RECIPES);
        this.recipeMapWorkable = new BoilerRecipeMapWorkable(this);
    }

    @Override
    public MetaTileEntity createMetaTileEntity(MetaTileEntityHolder metaTileEntityHolder) {
        return new BronzeBoiler(metaTileEntityId);
    }

    @Override
    protected BlockPattern createStructurePattern() {
        return FactoryBlockPattern.start()
                .aisle("CCC", "CCC", "CCC")
                .aisle("CCC", "CCC", "CCC")
                .aisle("CCC", "CSC", "CCC")
                .where('S', selfPredicate())
                .where('C', statePredicate(getCasingState()).or(abilityPartPredicate(MultiblockAbility.IMPORT_ITEMS)).or(abilityPartPredicate(MultiblockAbility.EXPORT_FLUIDS)).or(abilityPartPredicate(MultiblockAbility.IMPORT_FLUIDS)).or(abilityPartPredicate(MultiblockAbility.INPUT_ENERGY)))
                .build();
    }

    @Override
    public void invalidateStructure() {
        super.invalidateStructure();
        this.temperature = 0;
        this.fluidInputInventory = new FluidTankList(true);
        this.itemFuelInventory = new ItemHandlerList(Collections.emptyList());
        this.outputTank = new FluidTankList(true);
    }

//    @Override
//    public boolean checkRecipe(Recipe recipe, boolean consumeIfSuccess) {
//        int recipeRequiredTemp = recipe.getIntegerProperty("min_temperature");
//        return this.temperature >= recipeRequiredTemp;
//    }

    @Override
    public ICubeRenderer getBaseTexture(IMultiblockPart iMultiblockPart) {
        return GAMetaBlocks.METAL_CASING.get(Bronze);
    }

    protected IBlockState getCasingState() {
        return GAMetaBlocks.getMetalCasingBlockState(Bronze);
    }

    @Override
    protected void formStructure(PatternMatchContext context) {
        super.formStructure(context);
        this.fluidInputInventory = new FluidTankList(true, getAbilities(MultiblockAbility.IMPORT_FLUIDS));
        this.itemFuelInventory = new ItemHandlerList(getAbilities(MultiblockAbility.IMPORT_ITEMS));
        this.outputTank = new FluidTankList(true, getAbilities(MultiblockAbility.EXPORT_FLUIDS));
    }

    @Override
    protected void addDisplayText(List<ITextComponent> textList) {
        super.addDisplayText(textList);
        if (this.isStructureFormed()) {
            textList.add((new TextComponentTranslation("gtadditions.boiler.temperature", temperature)).setStyle((new Style()).setColor(TextFormatting.GREEN)));

            ITextComponent throttleText = new TextComponentTranslation("gregtech.multiblock.large_boiler.throttle", throttlePercentage, (int)(getThrottleEfficiency() * 100));
            withHoverTextTranslate(throttleText, "gregtech.multiblock.large_boiler.throttle.tooltip");
            textList.add(throttleText);

            ITextComponent buttonText = new TextComponentTranslation("gregtech.multiblock.large_boiler.throttle_modify");
            buttonText.appendText(" ");
            buttonText.appendSibling(withButton(new TextComponentString("[-]"), "sub"));
            buttonText.appendText(" ");
            buttonText.appendSibling(withButton(new TextComponentString("[+]"), "add"));
            textList.add(buttonText);
        }
    }

    @Override
    protected void handleDisplayClick(String componentData, Widget.ClickData clickData) {
        super.handleDisplayClick(componentData, clickData);
        int modifier = componentData.equals("add") ? 1 : -1;
        int result = (clickData.isShiftClick ? 1 : 5) * modifier;
        this.throttlePercentage = MathHelper.clamp(throttlePercentage + result, 20, 100);
    }

    private double getThrottleMultiplier() {
        return throttlePercentage / 100.0;
    }

    private double getThrottleEfficiency() {
        return MathHelper.clamp(1.0 + 0.3*Math.log(getThrottleMultiplier()), 0.4, 1.0);
    }

    private double getHeatEfficiencyMultiplier() {
        double temp = temperature / (maxTemperature * 1.0);
        return 1.0 + Math.round(temperatureEffBuff * temperature) / 100.0;
    }

    @Override
    protected void updateFormedValid() {
        if (fuelBurnTicksLeft > 0 && temperature < maxTemperature) {
            --this.fuelBurnTicksLeft;
            if (getTimer() % 5 == 0) {
                this.temperature++;
            }
            if (fuelBurnTicksLeft == 0) {
                this.wasActiveAndNeedsUpdate = true;
            }
        } else if (temperature > 0 && getTimer() % 20 == 0) {
            --this.temperature;
        }
//
//        this.lastTickFluidOutput = 0;
//        if (temperature >= BOILING_TEMPERATURE) {
//            boolean doWaterDrain = getTimer() % 20 == 0;
//            FluidStack drainedWater = fluidInputInventory.drain(ModHandler.getWater(1), doWaterDrain);
//            if (drainedWater == null || drainedWater.amount == 0) {
//                drainedWater = fluidInputInventory.drain(ModHandler.getDistilledWater(1), doWaterDrain);
//            }
//            if (drainedWater != null && drainedWater.amount > 0) {
////                if (temperature > BOILING_TEMPERATURE && hasNoInput) {
////                    float explosionPower = temperature / (float)BOILING_TEMPERATURE * 2.0f;
////                    getWorld().setBlockToAir(getPos());
////                    getWorld().createExplosion(null, getPos().getX() + 0.5, getPos().getY() + 0.5, getPos().getZ() + 0.5,
////                            explosionPower, true);
////                }
//                this.hasNoInput = false;
//                if (temperature >= BOILING_TEMPERATURE) {
//                    double outputMultiplier = temperature / (maxTemperature * 1.0) * getThrottleMultiplier() * getThrottleEfficiency();
//                    int steamOutput = (int) (baseFluidOutput * outputMultiplier);
//                    FluidStack steamStack = ModHandler.getSteam(steamOutput);
//                    outputTank.fill(steamStack, true);
//                    this.lastTickFluidOutput = steamOutput;
//                }
//            } else {
//                this.hasNoInput = true;
//            }
//        } else {
//            this.hasNoInput = false;
//        }

        if (fuelBurnTicksLeft == 0) {
            double heatEfficiency = getHeatEfficiencyMultiplier();
            int fuelMaxBurnTime = (int) Math.round(setupRecipeAndConsumeInputs() * heatEfficiency);
            if (fuelMaxBurnTime > 0) {
                this.fuelBurnTicksLeft = fuelMaxBurnTime;
                if (wasActiveAndNeedsUpdate) {
                    this.wasActiveAndNeedsUpdate = false;
                } else setActive(true);
                markDirty();
            }
        }

        if (wasActiveAndNeedsUpdate) {
            this.wasActiveAndNeedsUpdate = false;
            setActive(false);
        }
    }

    private int setupRecipeAndConsumeInputs() {
        for (IFluidTank fluidTank : fluidInputInventory.getFluidTanks()) {
            FluidStack fuelStack = fluidTank.drain(Integer.MAX_VALUE, false);
            if (fuelStack == null || ModHandler.isWater(fuelStack))
                continue; //ignore empty tanks and water
            FuelRecipe dieselRecipe = RecipeMaps.DIESEL_GENERATOR_FUELS.findRecipe(GTValues.V[9], fuelStack);
            if (dieselRecipe != null) {
                int fuelAmountToConsume = (int) Math.ceil(dieselRecipe.getRecipeFluid().amount * CONSUMPTION_MULTIPLIER * fuelConsumptionMultiplier * getThrottleMultiplier());
                if (fuelStack.amount >= fuelAmountToConsume) {
                    fluidTank.drain(fuelAmountToConsume, true);
                    long recipeVoltage = FuelRecipeLogic.getTieredVoltage(dieselRecipe.getMinVoltage());
                    int voltageMultiplier = (int) Math.max(1L, recipeVoltage / GTValues.V[GTValues.LV]);
                    return (int) Math.ceil(dieselRecipe.getDuration() * CONSUMPTION_MULTIPLIER / 2.0 * voltageMultiplier * getThrottleMultiplier());
                } else continue;
            }
            FuelRecipe denseFuelRecipe = RecipeMaps.SEMI_FLUID_GENERATOR_FUELS.findRecipe(GTValues.V[9], fuelStack);
            if (denseFuelRecipe != null) {
                int fuelAmountToConsume = (int) Math.ceil(denseFuelRecipe.getRecipeFluid().amount * CONSUMPTION_MULTIPLIER * fuelConsumptionMultiplier * getThrottleMultiplier());
                if (fuelStack.amount >= fuelAmountToConsume) {
                    fluidTank.drain(fuelAmountToConsume, true);
                    long recipeVoltage = FuelRecipeLogic.getTieredVoltage(denseFuelRecipe.getMinVoltage());
                    int voltageMultiplier = (int) Math.max(1L, recipeVoltage / GTValues.V[GTValues.LV]);
                    return (int) Math.ceil(denseFuelRecipe.getDuration() * CONSUMPTION_MULTIPLIER * 2 * voltageMultiplier * getThrottleMultiplier());
                }
            }
        }
        for (int slotIndex = 0; slotIndex < itemFuelInventory.getSlots(); slotIndex++) {
            ItemStack itemStack = itemFuelInventory.getStackInSlot(slotIndex);
            int fuelBurnValue = (int) Math.ceil(TileEntityFurnace.getItemBurnTime(itemStack) / (50.0 * fuelConsumptionMultiplier * getThrottleMultiplier()));
            if (fuelBurnValue > 0) {
                if (itemStack.getCount() == 1) {
                    ItemStack containerItem = itemStack.getItem().getContainerItem(itemStack);
                    itemFuelInventory.setStackInSlot(slotIndex, containerItem);
                } else {
                    itemStack.shrink(1);
                    itemFuelInventory.setStackInSlot(slotIndex, itemStack);
                }
                return fuelBurnValue;
            }
        }
        return 0;
    }


    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound data) {
        super.writeToNBT(data);
        data.setInteger("CurrentTemperature", temperature);
        data.setInteger("FuelBurnTicksLeft", fuelBurnTicksLeft);
        data.setBoolean("HasNoWater", hasNoInput);
        data.setInteger("ThrottlePercentage", throttlePercentage);
        return data;
    }

    @Override
    public void readFromNBT(NBTTagCompound data) {
        super.readFromNBT(data);
        this.temperature = data.getInteger("CurrentTemperature");
        this.fuelBurnTicksLeft = data.getInteger("FuelBurnTicksLeft");
        this.hasNoInput = data.getBoolean("HasNoWater");
        if(data.hasKey("ThrottlePercentage")) {
            this.throttlePercentage = data.getInteger("ThrottlePercentage");
        }
        this.isActive = fuelBurnTicksLeft > 0;
    }

    private void setActive(boolean active) {
        this.isActive = active;
        if (!getWorld().isRemote) {
            if (isStructureFormed()) {
                replaceFireboxAsActive(active);
            }
            writeCustomData(100, buf -> buf.writeBoolean(isActive));
            markDirty();
        }
    }

    @Override
    public void renderMetaTileEntity(CCRenderState renderState, Matrix4 translation, IVertexOperation[] pipeline) {
        super.renderMetaTileEntity(renderState, translation, pipeline);
        Textures.MULTIBLOCK_WORKABLE_OVERLAY.render(renderState, translation, pipeline, getFrontFacing(), isActive);
    }

    private void replaceFireboxAsActive(boolean isActive) {
        BlockPos centerPos = getPos().offset(getFrontFacing().getOpposite()).down();
        for (int x = -1; x <= 1; x++) {
            for (int z = -1; z <= 1; z++) {
                BlockPos blockPos = centerPos.add(x, 0, z);
                IBlockState blockState = getWorld().getBlockState(blockPos);
                if (blockState.getBlock() instanceof BlockFireboxCasing) {
                    blockState = blockState.withProperty(BlockFireboxCasing.ACTIVE, isActive);
                    getWorld().setBlockState(blockPos, blockState);
                }
            }
        }
    }

    @Override
    public int getLightValueForPart(IMultiblockPart sourcePart) {
        return sourcePart == null ? 0 : (isActive ? 15 : 0);
    }

    @Override
    public void writeInitialSyncData(PacketBuffer buf) {
        super.writeInitialSyncData(buf);
        buf.writeBoolean(isActive);
    }

    @Override
    public void receiveInitialSyncData(PacketBuffer buf) {
        super.receiveInitialSyncData(buf);
        this.isActive = buf.readBoolean();
    }

    @Override
    public void receiveCustomData(int dataId, PacketBuffer buf) {
        super.receiveCustomData(dataId, buf);
        if (dataId == 100) {
            this.isActive = buf.readBoolean();
        }
    }

    @Override
    public boolean onRightClick(EntityPlayer playerIn, EnumHand hand, EnumFacing facing, CuboidRayTraceResult hitResult) {
        ItemStack itemStack = playerIn.getHeldItem(hand);
        if(!itemStack.isEmpty() && itemStack.hasCapability(GregtechCapabilities.CAPABILITY_MALLET, null)) {
            ISoftHammerItem softHammerItem = itemStack.getCapability(GregtechCapabilities.CAPABILITY_MALLET, null);

            if (getWorld().isRemote) {
                return true;
            }
            if(!softHammerItem.damageItem(DamageValues.DAMAGE_FOR_SOFT_HAMMER, false)) {
                return false;
            }
        }
        return super.onRightClick(playerIn, hand, facing, hitResult);
    }

    public Collection<IFuelInfo> getFuels() {
        if (!isStructureFormed())
            return Collections.emptySet();
        final LinkedHashMap<Object, IFuelInfo> fuels = new LinkedHashMap<Object, IFuelInfo>();
        int fluidCapacity = 0; // fluid capacity is all non water tanks
        for (IFluidTank fluidTank : fluidInputInventory.getFluidTanks()) {
            FluidStack fuelStack = fluidTank.drain(Integer.MAX_VALUE, false);
            if (!ModHandler.isWater(fuelStack))
                fluidCapacity += fluidTank.getCapacity();
        }
        for (IFluidTank fluidTank : fluidInputInventory.getFluidTanks()) {
            FluidStack fuelStack = fluidTank.drain(Integer.MAX_VALUE, false);
            if (fuelStack == null || ModHandler.isWater(fuelStack))
                continue;
            FuelRecipe dieselRecipe = RecipeMaps.DIESEL_GENERATOR_FUELS.findRecipe(GTValues.V[9], fuelStack);
            if (dieselRecipe != null) {
                long recipeVoltage = FuelRecipeLogic.getTieredVoltage(dieselRecipe.getMinVoltage());
                int voltageMultiplier = (int) Math.max(1L, recipeVoltage / GTValues.V[GTValues.LV]);
                int burnTime = (int) Math.ceil(dieselRecipe.getDuration() * CONSUMPTION_MULTIPLIER / 2.0 * voltageMultiplier * getThrottleMultiplier());
                int fuelAmountToConsume = (int) Math.ceil(dieselRecipe.getRecipeFluid().amount * CONSUMPTION_MULTIPLIER * fuelConsumptionMultiplier * getThrottleMultiplier());
                int fuelBurnTime = fuelStack.amount * burnTime / fuelAmountToConsume;
                FluidFuelInfo fluidFuelInfo = (FluidFuelInfo) fuels.get(fuelStack.getUnlocalizedName());
                if (fluidFuelInfo == null) {
                    fluidFuelInfo = new FluidFuelInfo(fuelStack, fuelStack.amount, fluidCapacity, fuelAmountToConsume, fuelBurnTime);
                    fuels.put(fuelStack.getUnlocalizedName(), fluidFuelInfo);
                }
                else {
                    fluidFuelInfo.addFuelRemaining(fuelStack.amount);
                    fluidFuelInfo.addFuelBurnTime(fuelBurnTime);
                }
            }
            FuelRecipe denseFuelRecipe = RecipeMaps.SEMI_FLUID_GENERATOR_FUELS.findRecipe(GTValues.V[9], fuelStack);
            if (denseFuelRecipe != null) {
                long recipeVoltage = FuelRecipeLogic.getTieredVoltage(denseFuelRecipe.getMinVoltage());
                int voltageMultiplier = (int) Math.max(1L, recipeVoltage / GTValues.V[GTValues.LV]);
                int burnTime = (int) Math.ceil(denseFuelRecipe.getDuration() * CONSUMPTION_MULTIPLIER * 2 * voltageMultiplier * getThrottleMultiplier());
                int fuelAmountToConsume = (int) Math.ceil(denseFuelRecipe.getRecipeFluid().amount * CONSUMPTION_MULTIPLIER * fuelConsumptionMultiplier * getThrottleMultiplier());
                int fuelBurnTime = fuelStack.amount * burnTime / fuelAmountToConsume;
                FluidFuelInfo fluidFuelInfo = (FluidFuelInfo) fuels.get(fuelStack.getUnlocalizedName());
                if (fluidFuelInfo == null) {
                    fluidFuelInfo = new FluidFuelInfo(fuelStack, fuelStack.amount, fluidCapacity, fuelAmountToConsume, fuelBurnTime);
                    fuels.put(fuelStack.getUnlocalizedName(), fluidFuelInfo);
                }
                else {
                    fluidFuelInfo.addFuelRemaining(fuelStack.amount);
                    fluidFuelInfo.addFuelBurnTime(fuelBurnTime);
                }
            }
        }
        int itemCapacity = 0; // item capacity is all slots
        for (int slotIndex = 0; slotIndex < itemFuelInventory.getSlots(); slotIndex++) {
            itemCapacity += itemFuelInventory.getSlotLimit(slotIndex);
        }
        for (int slotIndex = 0; slotIndex < itemFuelInventory.getSlots(); slotIndex++) {
            ItemStack itemStack = itemFuelInventory.getStackInSlot(slotIndex);
            int burnTime = (int) Math.ceil(TileEntityFurnace.getItemBurnTime(itemStack) / (fuelConsumptionMultiplier * getThrottleMultiplier()));
            if (burnTime > 0) {
                ItemFuelInfo itemFuelInfo = (ItemFuelInfo) fuels.get(itemStack.getTranslationKey());
                if (itemFuelInfo == null) {
                    itemFuelInfo = new ItemFuelInfo(itemStack, itemStack.getCount(), itemCapacity, 1, itemStack.getCount() * burnTime);
                    fuels.put(itemStack.getTranslationKey(), itemFuelInfo);
                }
                else {
                    itemFuelInfo.addFuelRemaining(itemStack.getCount());
                    itemFuelInfo.addFuelBurnTime(itemStack.getCount() * burnTime);
                }
            }
        }
        return fuels.values();
    }

    public class BoilerRecipeMapWorkable extends GAMultiblockRecipeLogic {


        public BoilerRecipeMapWorkable(RecipeMapMultiblockController tileEntity) {
            super(tileEntity);
        }

        @Override
        protected void trySearchNewRecipe() {
            long maxVoltage = this.getMaxVoltage();
            Recipe currentRecipe = null;
            IItemHandlerModifiable importInventory = this.getInputInventory();
            IMultipleTankHandler importFluids = this.getInputTank();
            if (this.previousRecipe != null && this.previousRecipe.matches(false, importInventory, importFluids)) {
                currentRecipe = this.previousRecipe;
            } else {
                boolean dirty = this.checkRecipeInputsDirty(importInventory, importFluids);
                if (dirty || this.forceRecipeRecheck) {
                    this.forceRecipeRecheck = false;
                    currentRecipe = this.findRecipe(maxVoltage, importInventory, importFluids);
                    if (currentRecipe != null) {
                        this.previousRecipe = currentRecipe;
                    }
                }
            }

            if (currentRecipe != null && 201 >= currentRecipe.getIntegerProperty("min_temperature") && this.setupAndConsumeRecipeInputs(currentRecipe)) {
                this.setupRecipe(currentRecipe);
            }

        }


    }
}
