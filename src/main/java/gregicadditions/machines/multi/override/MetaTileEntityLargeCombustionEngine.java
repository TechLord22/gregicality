package gregicadditions.machines.multi.override;

import gregicadditions.capabilities.GregicAdditionsCapabilities;
import gregicadditions.machines.multi.GAFuelRecipeLogic;
import gregtech.api.capability.IEnergyContainer;
import gregtech.api.capability.IMultipleTankHandler;
import gregtech.api.capability.impl.FuelRecipeLogic;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.metatileentity.MetaTileEntityHolder;
import gregtech.api.metatileentity.multiblock.IMultiblockPart;
import gregtech.api.metatileentity.multiblock.MultiblockAbility;
import gregtech.api.multiblock.BlockPattern;
import gregtech.api.multiblock.FactoryBlockPattern;
import gregtech.api.recipes.machines.FuelRecipeMap;
import gregtech.api.recipes.recipes.FuelRecipe;
import gregtech.api.render.ICubeRenderer;
import gregtech.api.unification.material.Materials;
import gregtech.common.blocks.BlockMetalCasing;
import gregtech.common.blocks.BlockTurbineCasing;
import gregtech.common.blocks.MetaBlocks;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.FluidStack;

import java.util.function.Supplier;

import static gregtech.api.render.Textures.STABLE_TITANIUM_CASING;

public class MetaTileEntityLargeCombustionEngine extends gregtech.common.metatileentities.multi.electric.generator.MetaTileEntityDieselEngine {
	public MetaTileEntityLargeCombustionEngine(ResourceLocation metaTileEntityId) {
		super(metaTileEntityId);
	}

	public MetaTileEntity createMetaTileEntity(MetaTileEntityHolder holder) {
		return new MetaTileEntityLargeCombustionEngine(this.metaTileEntityId);
	}

	@Override
	protected BlockPattern createStructurePattern() {
		return FactoryBlockPattern.start()
				.aisle("XXX", "XDX", "XXX")
				.aisle("XHX", "HGH", "XHX")
				.aisle("XHX", "HGH", "XHX")
				.aisle("AAA", "AYA", "AAA")
				.where('X', statePredicate(this.getCasingState()))
				.where('G', statePredicate(MetaBlocks.TURBINE_CASING.getState(BlockTurbineCasing.TurbineCasingType.TITANIUM_GEARBOX)))
				.where('H', statePredicate(this.getCasingState()).or(abilityPartPredicate(MultiblockAbility.IMPORT_FLUIDS, GregicAdditionsCapabilities.MAINTENANCE_HATCH)))
				.where('D', abilityPartPredicate(MultiblockAbility.OUTPUT_ENERGY))
				.where('A', this.intakeCasingPredicate())
				.where('Y', this.selfPredicate())
				.build();
	}

	@Override
	protected FuelRecipeLogic createWorkable(long maxVoltage) {
		return new LargeCombustionEngineWorkable(this, this.recipeMap, () -> {
			return this.energyContainer;
		}, () -> {
			return this.importFluidHandler;
		}, maxVoltage);
	}

	@Override
	public ICubeRenderer getBaseTexture(IMultiblockPart sourcePart) {
		return STABLE_TITANIUM_CASING;
	}

	@Override
	public IBlockState getCasingState() {
		return MetaBlocks.METAL_CASING.getState(BlockMetalCasing.MetalCasingType.TITANIUM_STABLE);
	}

	public static class LargeCombustionEngineWorkable extends GAFuelRecipeLogic {
		private final int maxCycleLength = 20;
		private int currentCycle = 0;
		private boolean isUsingOxygen = false;

		public LargeCombustionEngineWorkable(MetaTileEntity metaTileEntity, FuelRecipeMap recipeMap, Supplier<IEnergyContainer> energyContainer, Supplier<IMultipleTankHandler> fluidTank, long maxVoltage) {
			super(metaTileEntity, recipeMap, energyContainer, fluidTank, maxVoltage);
		}

		public FluidStack getFuelStack() {
			if (this.previousRecipe == null) {
				return null;
			} else {
				FluidStack fuelStack = this.previousRecipe.getRecipeFluid();
				return this.fluidTank.get().drain(new FluidStack(fuelStack.getFluid(), Integer.MAX_VALUE), false);
			}
		}

		protected boolean checkRecipe(FuelRecipe recipe) {
			FluidStack lubricantStack = Materials.Lubricant.getFluid(2);
			FluidStack drainStack = this.fluidTank.get().drain(lubricantStack, false);
			return drainStack != null && drainStack.amount >= 2 || this.currentCycle < 20;
		}

		protected int calculateFuelAmount(FuelRecipe currentRecipe) {
			FluidStack oxygenStack = Materials.Oxygen.getFluid(2);
			FluidStack drainOxygenStack = ((IMultipleTankHandler)this.fluidTank.get()).drain(oxygenStack, false);
			this.isUsingOxygen = drainOxygenStack != null && drainOxygenStack.amount >= 2;
			return super.calculateFuelAmount(currentRecipe) * (this.isUsingOxygen ? 2 : 1);
		}

		protected long startRecipe(FuelRecipe currentRecipe, int fuelAmountUsed, int recipeDuration) {
			FluidStack oxygenStack;
			if (this.currentCycle >= 20) {
				oxygenStack = Materials.Lubricant.getFluid(2);
				this.fluidTank.get().drain(oxygenStack, true);
				this.currentCycle = 0;
			} else {
				++this.currentCycle;
			}

			if (this.isUsingOxygen) {
				oxygenStack = Materials.Oxygen.getFluid(2);
				this.fluidTank.get().drain(oxygenStack, true);
			}

			return this.maxVoltage * (long)(this.isUsingOxygen ? 3 : 1);
		}

		public NBTTagCompound serializeNBT() {
			NBTTagCompound compound = super.serializeNBT();
			compound.setInteger("Cycle", this.currentCycle);
			return compound;
		}

		public void deserializeNBT(NBTTagCompound compound) {
			super.deserializeNBT(compound);
			this.currentCycle = compound.getInteger("Cycle");
		}
	}
}
