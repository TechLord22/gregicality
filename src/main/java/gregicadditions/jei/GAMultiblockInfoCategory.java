package gregicadditions.jei;

import gregicadditions.jei.multi.*;
import gregicadditions.jei.multi.advance.*;
import gregicadditions.jei.multi.mega.MegaDistillationTowerInfo;
import gregicadditions.jei.multi.mega.MegaBlastFurnaceInfo;
import gregicadditions.jei.multi.mega.MegaVacuumFreezerInfo;
import gregicadditions.jei.multi.miner.LargeMinerInfo;
import gregicadditions.jei.multi.miner.VoidMinerInfo;
import gregicadditions.jei.multi.miner.VoidMinerInfo2;
import gregicadditions.jei.multi.miner.VoidMinerInfo3;
import gregicadditions.jei.multi.nuclear.GasCentrifugeInfo;
import gregicadditions.jei.multi.nuclear.HotCoolantTurbineInfo;
import gregicadditions.jei.multi.nuclear.NuclearReactorInfo;
import gregicadditions.jei.multi.override.*;
import gregicadditions.jei.multi.quantum.QubitComputerInfo;
import gregicadditions.jei.multi.simple.*;
import gregicadditions.machines.GATileEntities;
import gregtech.integration.jei.multiblock.MultiblockInfoRecipeWrapper;
import gregtech.integration.jei.multiblock.infos.LargeTurbineInfo;
import mezz.jei.api.IGuiHelper;
import mezz.jei.api.IJeiHelpers;
import mezz.jei.api.IModRegistry;
import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IRecipeCategory;
import mezz.jei.gui.recipes.RecipeLayout;
import net.minecraft.client.resources.I18n;

import java.util.ArrayList;
import java.util.List;

public class GAMultiblockInfoCategory implements IRecipeCategory<MultiblockInfoRecipeWrapper> {
    private final IDrawable background;
    private final IGuiHelper guiHelper;

    public GAMultiblockInfoCategory(IJeiHelpers helpers) {
        this.guiHelper = helpers.getGuiHelper();
        this.background = guiHelper.createBlankDrawable(176, 166);
    }

    public static void registerRecipes(IModRegistry registry) {
        List<MultiblockInfoRecipeWrapper> recipeWrappers = new ArrayList<MultiblockInfoRecipeWrapper>() {
            {
                add(new MultiblockInfoRecipeWrapper(new CentralMonitorInfo()));
                add(new MultiblockInfoRecipeWrapper(new ElectricBlastFurnaceInfo()));
                add(new MultiblockInfoRecipeWrapper(new CrackerUnitInfo()));
                add(new MultiblockInfoRecipeWrapper(new LargeCombustionEngineInfo()));
                add(new MultiblockInfoRecipeWrapper(new DistillationTowerInfo()));
                add(new MultiblockInfoRecipeWrapper(new ImplosionCompressorInfo()));
                add(new MultiblockInfoRecipeWrapper(new MultiSmelterInfo()));
                add(new MultiblockInfoRecipeWrapper(new VacuumFreezerInfo()));
                add(new MultiblockInfoRecipeWrapper(new PyrolyseOvenInfo()));
                add(new MultiblockInfoRecipeWrapper(new AssemblyLineInfo()));
                add(new MultiblockInfoRecipeWrapper(new FusionReactor1Info()));
                add(new MultiblockInfoRecipeWrapper(new FusionReactor2Info()));
                add(new MultiblockInfoRecipeWrapper(new FusionReactor3Info()));
                add(new MultiblockInfoRecipeWrapper(new ProcessingArrayInfo()));
                add(new MultiblockInfoRecipeWrapper(new LargeThermalCentrifugeInfo()));
                add(new MultiblockInfoRecipeWrapper(new LargeElectrolyzerInfo()));
                add(new MultiblockInfoRecipeWrapper(new LargeCentrifugeInfo()));
                add(new MultiblockInfoRecipeWrapper(new LargeCuttingInfo()));
                add(new MultiblockInfoRecipeWrapper(new LargeMixerInfo()));
                add(new MultiblockInfoRecipeWrapper(new LargeMultiUseInfo()));
                add(new MultiblockInfoRecipeWrapper(new LargeMaceratorInfo()));
                add(new MultiblockInfoRecipeWrapper(new LargeSifterInfo()));
                add(new MultiblockInfoRecipeWrapper(new LargeWashingPlantInfo()));
                add(new MultiblockInfoRecipeWrapper(new LargeWiremillInfo()));
                add(new MultiblockInfoRecipeWrapper(new LargeChemicalReactorInfo()));
                add(new MultiblockInfoRecipeWrapper(new LargeExtruderInfo()));
                add(new MultiblockInfoRecipeWrapper(new VolcanusInfo()));
                add(new MultiblockInfoRecipeWrapper(new LargeAssemblerInfo()));
                add(new MultiblockInfoRecipeWrapper(new LargeBenderAndFormingInfo()));
                add(new MultiblockInfoRecipeWrapper(new AdvancedDistillationTowerInfo()));
                add(new MultiblockInfoRecipeWrapper(new LargeMinerInfo(GATileEntities.LARGE_MINER[0])));
                add(new MultiblockInfoRecipeWrapper(new LargeMinerInfo(GATileEntities.LARGE_MINER[1])));
                add(new MultiblockInfoRecipeWrapper(new LargeMinerInfo(GATileEntities.LARGE_MINER[2])));
                add(new MultiblockInfoRecipeWrapper(new LargeTurbineInfo(GATileEntities.LARGE_STEAM_TURBINE)));
                add(new MultiblockInfoRecipeWrapper(new LargeTurbineInfo(GATileEntities.LARGE_GAS_TURBINE)));
                add(new MultiblockInfoRecipeWrapper(new LargeTurbineInfo(GATileEntities.LARGE_PLASMA_TURBINE)));
                add(new MultiblockInfoRecipeWrapper(new HotCoolantTurbineInfo(GATileEntities.HOT_COOLANT_TURBINE)));
                add(new MultiblockInfoRecipeWrapper(new NuclearReactorInfo(GATileEntities.NUCLEAR_REACTOR)));
                add(new MultiblockInfoRecipeWrapper(new NuclearReactorInfo(GATileEntities.NUCLEAR_BREEDER)));
                add(new MultiblockInfoRecipeWrapper(new LargeCircuitAssemblyLineInfo()));
                add(new MultiblockInfoRecipeWrapper(new VoidMinerInfo()));
                add(new MultiblockInfoRecipeWrapper(new LargeTransformerInfo()));
                add(new MultiblockInfoRecipeWrapper(new IndustrialPrimitiveBlastFurnaceInfo()));
                add(new MultiblockInfoRecipeWrapper(new CryogenicFreezerInfo()));
                add(new MultiblockInfoRecipeWrapper(new ChemicalPlantInfo()));
                add(new MultiblockInfoRecipeWrapper(new LargeRocketEngineInfo()));
                add(new MultiblockInfoRecipeWrapper(new AlloyBlastFurnaceInfo()));
                add(new MultiblockInfoRecipeWrapper(new LargeForgeHammerInfo()));
                add(new MultiblockInfoRecipeWrapper(new LargeNaquadahReactorInfo()));
                add(new MultiblockInfoRecipeWrapper(new BatteryTowerInfo()));
                add(new MultiblockInfoRecipeWrapper(new HyperReactor1Info()));
                add(new MultiblockInfoRecipeWrapper(new HyperReactor2Info()));
                add(new MultiblockInfoRecipeWrapper(new HyperReactor3Info()));
                add(new MultiblockInfoRecipeWrapper(new FusionReactor4Info()));
                add(new MultiblockInfoRecipeWrapper(new GasCentrifugeInfo()));
                add(new MultiblockInfoRecipeWrapper(new QubitComputerInfo()));
                add(new MultiblockInfoRecipeWrapper(new DrillingRigInfo()));
                add(new MultiblockInfoRecipeWrapper(new StellarForgeInfo()));
                add(new MultiblockInfoRecipeWrapper(new LargeEngraverInfo()));
                add(new MultiblockInfoRecipeWrapper(new VoidMinerInfo2()));
                add(new MultiblockInfoRecipeWrapper(new VoidMinerInfo3()));
                add(new MultiblockInfoRecipeWrapper(new BioReactorInfo()));
                add(new MultiblockInfoRecipeWrapper(new PlasmaCondenserInfo()));
                add(new MultiblockInfoRecipeWrapper(new LargePackagerInfo()));
                add(new MultiblockInfoRecipeWrapper(new SteamGrinderInfo()));
                add(new MultiblockInfoRecipeWrapper(new SteamOvenInfo()));
                add(new MultiblockInfoRecipeWrapper(new CosmicRayDetectorInfo()));
                add(new MultiblockInfoRecipeWrapper(new ElectricImplosionInfo()));
                add(new MultiblockInfoRecipeWrapper(new MegaDistillationTowerInfo()));
                add(new MultiblockInfoRecipeWrapper(new MegaBlastFurnaceInfo()));
                add(new MultiblockInfoRecipeWrapper(new MegaVacuumFreezerInfo()));
                add(new MultiblockInfoRecipeWrapper(new LargeArcFurnaceInfo()));
                add(new MultiblockInfoRecipeWrapper(new LargeBreweryInfo()));
                add(new MultiblockInfoRecipeWrapper(new LargeCanningMachineInfo()));
                add(new MultiblockInfoRecipeWrapper(new AdvancedChemicalReactorInfo()));
                add(new MultiblockInfoRecipeWrapper(new LargeElectromagnetInfo()));
                add(new MultiblockInfoRecipeWrapper(new LargeExtractorInfo()));
            }
        };

        for (MultiblockInfoRecipeWrapper recipeWrapper : recipeWrappers) {
            registry.addRecipes(new ArrayList<MultiblockInfoRecipeWrapper>() {{ add(recipeWrapper); }}, "gregtech:multiblock_info");
        }
    }

    @Override
    public String getUid() {
        return "gtadditions:multiblock_info2";
    }

    @Override
    public String getTitle() {
        return I18n.format("gregtech.multiblock.title");
    }

    @Override
    public String getModName() {
        return "gtadditions";
    }

    @Override
    public IDrawable getBackground() {
        return this.background;
    }

    @Override
    public void setRecipe(IRecipeLayout recipeLayout, MultiblockInfoRecipeWrapper recipeWrapper, IIngredients ingredients) {
        recipeWrapper.setRecipeLayout((RecipeLayout) recipeLayout, guiHelper);
    }
}
