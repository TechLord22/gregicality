package gregicadditions.recipes;

import gregicadditions.GAConfig;
import gregicadditions.item.GAExplosive;
import gregicadditions.item.GAMetaBlocks;
import gregicadditions.materials.SimpleDustMaterialStack;
import gregicadditions.recipes.categories.*;
import gregicadditions.recipes.categories.circuits.CircuitRecipes;
import gregicadditions.recipes.categories.machines.MachineCraftingRecipes;
import gregicadditions.recipes.chain.*;
import gregicadditions.recipes.helper.HelperMethods;
import gregicadditions.recipes.impl.LargeRecipeBuilder;
import gregicadditions.utils.GALog;
import gregtech.api.GTValues;
import gregtech.api.items.toolitem.ToolMetaItem;
import gregtech.api.recipes.*;
import gregtech.api.recipes.ingredients.IntCircuitIngredient;
import gregtech.api.unification.OreDictUnifier;
import gregtech.api.unification.material.type.*;
import gregtech.api.unification.ore.OrePrefix;
import gregtech.api.unification.stack.MaterialStack;
import gregtech.api.unification.stack.UnificationEntry;
import gregtech.common.items.MetaItems;
import gregtech.common.items.behaviors.TurbineRotorBehavior;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.oredict.OreDictionary;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static gregicadditions.GAEnums.GAOrePrefix.*;
import static gregicadditions.GAEnums.GAOrePrefix.plateDouble;
import static gregicadditions.GAMaterials.*;
import static gregicadditions.item.GAMetaItems.*;
import static gregicadditions.recipes.GARecipeMaps.*;
import static gregicadditions.recipes.helper.HelperMethods.*;
import static gregtech.api.GTValues.L;
import static gregtech.api.recipes.RecipeMaps.*;
import static gregtech.api.recipes.ingredients.IntCircuitIngredient.getIntegratedCircuit;
import static gregtech.api.unification.material.MarkerMaterials.Color.White;
import static gregtech.api.unification.material.Materials.*;
import static gregtech.api.unification.material.type.DustMaterial.MatFlags.NO_SMASHING;
import static gregtech.api.unification.material.type.GemMaterial.MatFlags.CRYSTALLISABLE;
import static gregtech.api.unification.material.type.IngotMaterial.MatFlags.GENERATE_SMALL_GEAR;
import static gregtech.api.unification.material.type.Material.MatFlags.*;
import static gregtech.api.unification.ore.OrePrefix.*;

/**
 * Primary Recipe Registration Class
 */
public class RecipeHandler {

    /**
     * Used for Gem Implosion
     */
    private static final ItemStack[] EXPLOSIVES = new ItemStack[]{
            GAMetaBlocks.EXPLOSIVE.getItemVariant(GAExplosive.ExplosiveType.POWDER_BARREL, 48),
            new ItemStack(Blocks.TNT, 24),
            MetaItems.DYNAMITE.getStackForm(12),
            GAMetaBlocks.EXPLOSIVE.getItemVariant(GAExplosive.ExplosiveType.ITNT, 6)};

    /**
     * GT Material Handler registration.
     * This probably isn't the method you are looking for.
     */
    public static void register() {

        gtMetalCasing.addProcessingHandler(IngotMaterial.class, RecipeHandler::processMetalCasing);
        turbineBlade.addProcessingHandler(IngotMaterial.class, RecipeHandler::processTurbine);
        ingot.addProcessingHandler(IngotMaterial.class, RecipeHandler::processIngot);
        plateDense.addProcessingHandler(IngotMaterial.class, RecipeHandler::processDensePlate);

        if (GAConfig.GT6.BendingCurvedPlates && GAConfig.GT6.BendingCylinders)
            plateCurved.addProcessingHandler(IngotMaterial.class, RecipeHandler::processPlateCurved);

        if (GAConfig.GT6.addRounds)
            round.addProcessingHandler(IngotMaterial.class, RecipeHandler::processRound);

        plateDouble.addProcessingHandler(IngotMaterial.class, RecipeHandler::processDoublePlate);


        if (GAConfig.GT6.BendingRings && GAConfig.GT6.BendingCylinders)
            ring.addProcessingHandler(IngotMaterial.class, RecipeHandler::processRing);

        dustSmall.addProcessingHandler(DustMaterial.class, RecipeHandler::processSmallDust);
        if (GAConfig.Misc.PackagerDustRecipes) {
            dustTiny.addProcessingHandler(DustMaterial.class, RecipeHandler::processTinyDust);
            nugget.addProcessingHandler(IngotMaterial.class, RecipeHandler::processNugget);
        }

        if (GAConfig.GT5U.stickGT5U)
            stick.addProcessingHandler(DustMaterial.class, RecipeHandler::processStick);

        dust.addProcessingHandler(GemMaterial.class, RecipeHandler::processGem);
        foil.addProcessingHandler(IngotMaterial.class, RecipeHandler::processFoil);
        pipeTiny.addProcessingHandler(IngotMaterial.class, RecipeHandler::processTinyPipe);
        pipeLarge.addProcessingHandler(IngotMaterial.class, RecipeHandler::processLargePipe);
        rotor.addProcessingHandler(IngotMaterial.class, RecipeHandler::processRotor);
        lens.addProcessingHandler(GemMaterial.class, RecipeHandler::processLens);
        spring.addProcessingHandler(IngotMaterial.class, RecipeHandler::processSpring);
        springSmall.addProcessingHandler(IngotMaterial.class, RecipeHandler::processSpringSmall);
        gearSmall.addProcessingHandler(IngotMaterial.class, RecipeHandler::processGearSmall);
    }


    /**
     * Main Method for initializing recipe chains,
     * located in the "recipes/chain" directory.
     */
    public static void initChains() {
        GoldChain.init();
        NaquadahChain.init();
        NuclearChain.init();
        PlasticChain.init();
        PlatinumSludgeGroupChain.init();
        TungstenChain.init();
        REEChain.init();
        Batteries.init();
        RheniumChain.init();
        UHVMaterials.init();
        PEEKChain.init();
        ZylonChain.init();
        FullereneChain.init();
        BariumChain.init();
        UraniumChain.init();
        VanadiumChain.init();
        IodineChain.init();
        ZirconChain.init();
        ZincChain.init();
        AluminiumChain.init();
        AmmoniaChain.init();
        ChromiumChain.init();
        LithiumChain.init();
        BrineChain.init();
        FusionElementsChain.init();
        NanotubeChain.init();
        VariousChains.init();
        SuperconductorsSMDChain.init();
        FusionComponents.init();
        NiobiumTantalumChain.init();
        Dyes.init();
        SensorEmitter.init();
        SeleniumChain.init();
        WormholeGeneratorChain.init();
        CosmicChain.init();
        UltimateMaterials.init();
        DigitalInterfaces.init();
        InsulationWireAssemblyChain.init();
        ArcFurnaceOxidation.init();
        WetwareChain.init();
        OpticalChain.init();
    }

    /**
     * Main Recipe Addition Method for recipes outside of autogenerated
     * recipes and chemistry chains.
     */
    public static void initRecipes() {
        RecipeOverride.init();
        CircuitRecipes.init();
        MachineCraftingRecipes.init();
        ComponentRecipes.init();
        MetaItemRecipes.init();
        CasingRecipes.init();
        SuperconductorRecipes.init();
        MiscRecipes.init();
    }

    /**
     * Large Machine Recipes.
     * These are run after other RecipeMaps are finalized
     * since they often take recipes from other maps.
     */
    public static void registerLargeMachineRecipes() {

        registerLargeMachineRecipes(CHEMICAL_RECIPES, LARGE_CHEMICAL_RECIPES);
        registerLargeMachineRecipes(LASER_ENGRAVER_RECIPES, LARGE_ENGRAVER_RECIPES);
        registerLargeMachineRecipes(CENTRIFUGE_RECIPES, LARGE_CENTRIFUGE_RECIPES);

        registerLargeForgeHammerRecipes();
        registerLargeMixerRecipes();
        registerAlloyBlastRecipes();
        registerChemicalPlantRecipes();
        registerGreenHouseRecipes();
    }

    /**
     * Ingot Material Handler. Generates:
     *
     * + Mixer Recipes for GTCE Materials we add
     * + Bending Cylinder Recipes
     * + GT6 Wrench Recipes (plates over ingots)
     */
    private static void processIngot(OrePrefix ingot, IngotMaterial material) {

        // Ingot Composition
        processIngotComposition(material);

        // Tools
        if (!material.hasFlag(NO_SMASHING) && material.toolDurability > 0) {

            // GT6 Expensive Wrenches (Plates over Ingots)
            if (GAConfig.GT6.ExpensiveWrenches) {

                removeRecipeByName(String.format("gtadditions:wrench_%s", material.toString()));

                ModHandler.addShapedRecipe(String.format("ga_wrench_%s", material.toString()), MetaItems.WRENCH.getStackForm(material),
                        "XhX", "XXX", " X ",
                        'X', new UnificationEntry(plate, material));
            }

            // Bending Cylinders
            if (GAConfig.GT6.BendingCylinders) {

                ModHandler.addShapedRecipe(String.format("cylinder_%s", material.toString()), ((ToolMetaItem<?>.MetaToolValueItem) BENDING_CYLINDER).getStackForm(material),
                        "sfh", "XXX", "XXX",
                        'X', new UnificationEntry(ingot, material));

                ModHandler.addShapedRecipe(String.format("small_cylinder_%s", material.toString()), ((ToolMetaItem<?>.MetaToolValueItem) SMALL_BENDING_CYLINDER).getStackForm(material),
                        "sfh", "XXX",
                        'X', new UnificationEntry(ingot, material));
            }
        }
    }

    /**
     * Generates Mixer (or Large Mixer) recipes for GTCE Materials we add.
     */
    private static void processIngotComposition(IngotMaterial material) {
        if (material.materialComponents.size() <= 1 || material.blastFurnaceTemperature == 0 || material.hasFlag(DISABLE_AUTOGENERATED_MIXER_RECIPE))
            return;

        int fluidCount = (int) material.materialComponents.stream().filter(mat -> mat.material instanceof FluidMaterial && !(mat.material instanceof DustMaterial)).count();
        int itemCount = material.materialComponents.size() - fluidCount;

        // Try to fit in Mixer
        RecipeBuilder<?> builder;
        if (itemCount <= MIXER_RECIPES.getMaxInputs()
                && fluidCount <= MIXER_RECIPES.getMaxFluidInputs())

            builder = MIXER_RECIPES.recipeBuilder();

        // Try to fit in Large Mixer
        else if (itemCount <= LARGE_MIXER_RECIPES.getMaxInputs()
                && fluidCount <= LARGE_MIXER_RECIPES.getMaxFluidInputs())

            builder = LARGE_MIXER_RECIPES.recipeBuilder()
                    .notConsumable(new IntCircuitIngredient(material.materialComponents.size()));

        // Cannot fit in either
        else {
            GALog.logger.warn("Material {} has too many material components to generate a recipe in either normal or large mixer.", material.getUnlocalizedName());
            return;
        }

        int totalMaterial = 0;
        for (MaterialStack stack : material.materialComponents) {
            int amount = (int) stack.amount;

            if (stack.material instanceof DustMaterial)
                builder.input(dust, stack.material, amount);

            else if (stack.material instanceof FluidMaterial)
                builder.fluidInputs(((FluidMaterial) stack.material).getFluid(1000 * amount));

            else if (stack instanceof SimpleDustMaterialStack)
                builder.inputs(((SimpleDustMaterialStack) stack).simpleDustMaterial.getItemStack(amount));

            totalMaterial += amount;
        }

        builder.output(dust, material, totalMaterial)
               .duration((int) (material.getAverageMass() * totalMaterial))
               .EUt(30)
               .buildAndRegister();
    }

    /**
     * Gem Material Handler. Generates:
     *
     * + Laser Engraver Gem Recipes
     * + Implosion Compressor Gem Recipes
     * + Gem Hammer Recipes
     *
     * - Removes GTCE Gem Implosion Recipes
     */
    private static void processGem(OrePrefix dustPrefix, GemMaterial material) {

        // Gem Implosion Recipes
        if (!material.hasFlag(CRYSTALLISABLE) && !material.hasFlag(EXPLOSIVE) && !material.hasFlag(FLAMMABLE)) {

            RecipeBuilder<?> builder;

            removeRecipesByInputs(IMPLOSION_RECIPES, OreDictUnifier.get(dustPrefix, material, 4), new ItemStack(Blocks.TNT, 2));

            // It isn't uncommon for some GemMaterials to disable one or more of these prefixes.
            // Lets just check for both to be safe
            boolean hasFlawless = !OreDictUnifier.get(gemFlawless, material).isEmpty();
            boolean hasExquisite = !OreDictUnifier.get(gemExquisite, material).isEmpty();

            // Laser Engraver Recipes
            if (hasFlawless)

                // Gem -> Flawless
                LASER_ENGRAVER_RECIPES.recipeBuilder().duration(2400).EUt(2000)
                        .input(gem, material, 4)
                        .notConsumable(craftingLens, White)
                        .output(gemFlawless, material)
                        .buildAndRegister();

            if (hasExquisite)

                // Flawless -> Exquisite
                LASER_ENGRAVER_RECIPES.recipeBuilder().duration(2400).EUt(2000)
                        .input(gemFlawless, material, 4)
                        .notConsumable(craftingLens, White)
                        .output(gemExquisite, material)
                        .buildAndRegister();

            // Implosion Compressor Recipes
            for (ItemStack explosive : EXPLOSIVES) {

                // Dust -> Gem
                builder = IMPLOSION_RECIPES.recipeBuilder()
                        .input(dust, material, 4)
                        .output(gem, material, 3)
                        .output(dustTiny, DarkAsh, 2);
                builder .applyProperty("explosives", explosive);
                builder .buildAndRegister();

                if (hasFlawless) {

                    // Gem -> Flawless
                    builder = IMPLOSION_RECIPES.recipeBuilder()
                            .input(gem, material, 3)
                            .output(gemFlawless, material)
                            .output(dustTiny, DarkAsh, 2);
                    builder .applyProperty("explosives", explosive);
                    builder .buildAndRegister();
                }

                if (hasExquisite) {

                    // Flawless -> Exquisite
                    builder = IMPLOSION_RECIPES.recipeBuilder()
                            .input(gemFlawless, material, 3)
                            .output(gemExquisite, material)
                            .output(dustTiny, DarkAsh, 2);
                    builder .applyProperty("explosives", explosive);
                    builder .buildAndRegister();
                }
            }
        }

        // Gem Hammer Recipes
        if (!OreDictUnifier.get(toolHeadHammer, material).isEmpty()) {

            ModHandler.addMirroredShapedRecipe(String.format("gem_hammer_%s", material.toString()), MetaItems.HARD_HAMMER.getStackForm(material),
                    "GG ", "GGS", "GG ",
                    'G', new UnificationEntry(gem, material),
                    'S', new UnificationEntry(stick, Wood));
        }
    }

    /**
     * Rod Material Handler. Generates:
     *
     * + GT5U Lathe Recipes if enabled (1 rod + 2 small dust)
     *
     * - Removes old Lathe Rod Recipes if enabled
     */
    private static void processStick(OrePrefix stickPrefix, DustMaterial material) {

        // GT5U Lathe recipes
        if (material instanceof GemMaterial || material instanceof IngotMaterial) {
            OrePrefix prefix = material instanceof IngotMaterial ? ingot : gem;

            removeRecipesByInputs(LATHE_RECIPES, OreDictUnifier.get(prefix, material));

            LATHE_RECIPES.recipeBuilder().EUt(16).duration((int) Math.max(material.getAverageMass() * 2, 1))
                    .input(prefix, material)
                    .output(stickPrefix, material)
                    .output(dustSmall, material, 2)
                    .buildAndRegister();
        }
    }

    /**
     * Tiny Dust Material Handler. Generates:
     *
     * + Schematic Recipes in favor of Integrated Circuit Packager Recipes
     */
    private static void processTinyDust(OrePrefix dustTiny, DustMaterial material) {

        removeRecipesByInputs(PACKER_RECIPES, OreDictUnifier.get(dustTiny, material, 9), getIntegratedCircuit(1));

        PACKER_RECIPES.recipeBuilder().duration(100).EUt(4)
                .input(dustTiny, material, 9)
                .notConsumable(SCHEMATIC_DUST.getStackForm())
                .output(dust, material)
                .buildAndRegister();
    }

    /**
     * Small Dust Material Handler. Generates:
     *
     * + Overrides GTCE Small Dust Uncrafting Recipe to favor GT5's
     * + Schematic Recipes in favor of Integrated Circuit Packager Recipes
     */
    private static void processSmallDust(OrePrefix dustSmall, DustMaterial material) {

        // Small Dust Uncrafting Recipe Fix
        if (!OreDictUnifier.get(dustSmall, material).isEmpty()) {

            removeRecipeByName(String.format("gtadditions:small_dust_disassembling_%s", material.toString()));

            ModHandler.addShapedRecipe("dust_small_" + material.toString(), OreDictUnifier.get(dustSmall, material, 4),
                    " D", "  ", 'D',
                    new UnificationEntry(dust, material));
        }

        // Packager Small Dust Recipes
        // NOTE This config is checked here instead of in "register()" because this method always needs to be hit
        // in order to fix the Small Dust Uncrafting recipes
        if (GAConfig.Misc.PackagerDustRecipes) {

            removeRecipesByInputs(PACKER_RECIPES, OreDictUnifier.get(dustSmall, material, 4), getIntegratedCircuit(2));

            PACKER_RECIPES.recipeBuilder().duration(100).EUt(4)
                    .input(dustSmall, material, 4)
                    .notConsumable(SCHEMATIC_DUST.getStackForm())
                    .output(dust, material)
                    .buildAndRegister();
        }
    }

    /**
     * Nugget Material Handler. Generates:
     *
     * + Schematic Packing and Unpacking Recipes instead of Integrated Circuits if enabled
     *
     */
    private static void processNugget(OrePrefix nugget, IngotMaterial material) {

        // Packer
        removeRecipesByInputs(PACKER_RECIPES, OreDictUnifier.get(nugget, material, 9), getIntegratedCircuit(1));

        PACKER_RECIPES.recipeBuilder().duration(100).EUt(4)
                .input(nugget, material, 9)
                .notConsumable(SCHEMATIC_3X3.getStackForm())
                .output(ingot, material)
                .buildAndRegister();

        // Unpacker
        removeRecipesByInputs(UNPACKER_RECIPES, OreDictUnifier.get(ingot, material, 1), getIntegratedCircuit(1));

        UNPACKER_RECIPES.recipeBuilder().duration(100).EUt(4)
                .input(ingot, material)
                .notConsumable(SCHEMATIC_3X3.getStackForm())
                .output(nugget, material, 9)
                .buildAndRegister();
    }

    /**
     * Ring Material Handler. Generates:
     *
     * + Bending Cylinder Ring Recipes if enabled
     *
     * - Removes old Handcrafting Ring Recipes if enabled
     */
    private static void processRing(OrePrefix ring, IngotMaterial material) {
        if (!material.hasFlag(NO_SMASHING)) {

            removeCraftingRecipes(OreDictUnifier.get(ring, material));

            ModHandler.addShapedRecipe(String.format("rod_to_ring_%s", material.toString()), OreDictUnifier.get(ring, material),
                    "hS", " C",
                    'S', new UnificationEntry(stick, material),
                    'C', "craftingToolBendingCylinderSmall");
        }
    }

    /**
     * Foil Material Handler. Generates:
     *
     * + Bending Cylinder Foil Recipes if enabled
     * + Cluster Mill Foil Recipes if enabled
     *
     * - Removes Bender Foils if Cluster Mill is enabled
     */
    private static void processFoil(OrePrefix foil, IngotMaterial material) {
        if (!OreDictUnifier.get(foil, material).isEmpty()) {

            // Handcrafting foils
            if (!material.hasFlag(NO_SMASHING)) {

                if (GAConfig.GT6.BendingFoils) {

                    ModHandler.addShapedRecipe(String.format("foil_%s", material.toString()), OreDictUnifier.get(foil, material, 2),
                            "hPC",
                            'P', new UnificationEntry(plate, material),
                            'C', "craftingToolBendingCylinder");

                } else {

                    ModHandler.addShapedRecipe(String.format("foil_%s", material.toString()), OreDictUnifier.get(foil, material, 2),
                            "hP ",
                            'P', new UnificationEntry(plate, material));
                }
            }

            // Cluster Mill foils
            if (GAConfig.GT6.BendingFoilsAutomatic) {

                removeRecipesByInputs(BENDER_RECIPES, OreDictUnifier.get(plate, material), getIntegratedCircuit(0));

                CLUSTER_MILL_RECIPES.recipeBuilder().EUt(24).duration((int) material.getMass())
                        .input(plate, material)
                        .output(foil, material, 4)
                        .buildAndRegister();
            }
        }
    }

    /**
     * Round Material Handler. Generates:
     *
     * + Round Handcrafting Recipes
     * + Round Lathe Recipes
     * + Round Unification Recipes
     */
    private static void processRound(OrePrefix round, IngotMaterial material) {
        if (!material.hasFlag(NO_SMASHING)) {

            ModHandler.addShapedRecipe(String.format("round_%s", material.toString()), OreDictUnifier.get(round, material),
                    "fN", "Nh",
                    'N', new UnificationEntry(nugget, material));

            ModHandler.addShapedRecipe(String.format("round_from_ingot_%s", material.toString()), OreDictUnifier.get(round, material, 4),
                    "fIh",
                    'I', new UnificationEntry(ingot, material));
        }

        LATHE_RECIPES.recipeBuilder().EUt(8).duration(100)
                .input(nugget, material)
                .output(round, material)
                .buildAndRegister();

        int voltageMultiplier = material.blastFurnaceTemperature == 0 ? 1 : material.blastFurnaceTemperature > 2000 ? 16 : 4;

        // Unification Recipes
        MACERATOR_RECIPES.recipeBuilder().EUt(8 * voltageMultiplier).duration(16)
                .input(round, material)
                .output(dustTiny, material)
                .buildAndRegister();

        FLUID_EXTRACTION_RECIPES.recipeBuilder().EUt(32 * voltageMultiplier).duration(2)
                .input(round, material)
                .fluidOutputs(material.getFluid(L / 9))
                .buildAndRegister();

        ARC_FURNACE_RECIPES.recipeBuilder().EUt(30 * voltageMultiplier).duration(16)
                .input(round, material)
                .output(nugget, material.arcSmeltInto == null ? material : material.arcSmeltInto)
                .buildAndRegister();
    }

    /**
     * Double PLate Material Handler. Generates:
     *
     * + Plate to Double Plate Hand Recipes
     * + Double Plate Forge Hammer Recipes
     * + Double Plate Unification Recipes
     */
    private static void processDoublePlate(OrePrefix doublePlate, IngotMaterial material) {
        if (!material.hasFlag(NO_SMASHING)) {
            ModHandler.addShapedRecipe(String.format("plate_double_%s", material.toString()), OreDictUnifier.get(doublePlate, material),
                    "h", "P", "P",
                    'P', new UnificationEntry(plate, material));
        }

        BENDER_RECIPES.recipeBuilder().EUt(30).duration(200)
                .input(plate, material, 2)
                .output(doublePlate, material)
                .circuitMeta(2)
                .buildAndRegister();

        int voltageMultiplier = material.blastFurnaceTemperature == 0 ? 1 : material.blastFurnaceTemperature > 2000 ? 16 : 4;

        // Unification Recipes
        MACERATOR_RECIPES.recipeBuilder().EUt(8 * voltageMultiplier).duration(60)
                .input(doublePlate, material)
                .output(dust, material, 2)
                .buildAndRegister();

        FLUID_EXTRACTION_RECIPES.recipeBuilder().EUt(32 * voltageMultiplier).duration(160)
                .input(doublePlate, material)
                .fluidOutputs(material.getFluid(L * 2))
                .buildAndRegister();

        ARC_FURNACE_RECIPES.recipeBuilder().EUt(30 * voltageMultiplier).duration(16)
                .input(doublePlate, material)
                .output(ingot, material.arcSmeltInto == null ? material : material.arcSmeltInto, 2)
                .buildAndRegister();
    }

    /**
     * Dense PLate Material Handler. Generates:
     *
     * + Plate/Ingot to Dense Plate Bender Recipes with circuit 9
     * - Plate/Ingot to Dense Plate Bender Recipes with circuit 2/5
     */
    private static void processDensePlate(OrePrefix densePlate, IngotMaterial material) {
        removeRecipesByInputs(BENDER_RECIPES, OreDictUnifier.get(ingot, material, 9), getIntegratedCircuit(5));
        removeRecipesByInputs(BENDER_RECIPES, OreDictUnifier.get(plate, material, 9), getIntegratedCircuit(2));

        BENDER_RECIPES.recipeBuilder().duration((int) material.getMass() * 4).EUt(96)
                .input(plate, material, 9)
                .output(densePlate, material, 1)
                .circuitMeta(9)
                .buildAndRegister();
        BENDER_RECIPES.recipeBuilder().duration((int) material.getMass() * 9).EUt(96)
                .input(ingot, material, 9)
                .output(densePlate, material, 1)
                .circuitMeta(9)
                .buildAndRegister();
    }


    /**
     * Curved Plate Material Handler. Generates:
     *
     * + Curved Plate Recipes if enabled, Handcrafting and Machine
     * + Curved Plate Rotor Recipes if enabled
     * + Curved Plate Pipe Recipes if enabled
     * + Curved Plate Unification Recipes
     */
    private static void processPlateCurved(OrePrefix plateCurved, IngotMaterial material) {

        // Register Curved Plate recipes
        if (!material.hasFlag(NO_SMASHING)) {

            ModHandler.addShapedRecipe(String.format("curved_plate_%s", material.toString()), OreDictUnifier.get(plateCurved, material),
                    "h", "P", "C",
                    'P', new UnificationEntry(plate, material),
                    'C', "craftingToolBendingCylinder");

            ModHandler.addShapedRecipe(String.format("flatten_plate_%s", material.toString()), OreDictUnifier.get(plate, material),
                    "h", "C",
                    'C', new UnificationEntry(plateCurved, material));
        }

        BENDER_RECIPES.recipeBuilder().EUt(24).duration((int) material.getMass())
                .input(plate, material)
                .circuitMeta(1)
                .output(plateCurved, material)
                .buildAndRegister();

        BENDER_RECIPES.recipeBuilder().EUt(24).duration((int) material.getMass())
                .input(plateCurved, material)
                .circuitMeta(0)
                .output(plate, material)
                .buildAndRegister();

        // Register Curved Plate Pipe Recipes
        if (!OreDictUnifier.get(pipeMedium, material).isEmpty()) {

            if (GAConfig.GT6.BendingPipes && !OreDictUnifier.get(plateCurved, material).isEmpty()) {

                removeCraftingRecipes(OreDictUnifier.get(pipeSmall, material, 4));
                removeCraftingRecipes(OreDictUnifier.get(pipeMedium, material, 2));

                ModHandler.addShapedRecipe(String.format("pipe_ga_tiny_%s", material.toString()), OreDictUnifier.get(pipeTiny, material, 8),
                        "PPP", "hCw", "PPP",
                        'P', new UnificationEntry(plateCurved, material),
                        'C', "craftingToolBendingCylinder");

                ModHandler.addShapedRecipe(String.format("pipe_ga_small_%s", material.toString()), OreDictUnifier.get(pipeSmall, material, 4),
                        "PwP", "PCP", "PhP",
                        'P', new UnificationEntry(plateCurved, material),
                        'C', "craftingToolBendingCylinder");

                ModHandler.addShapedRecipe(String.format("pipe_ga_%s", material.toString()), OreDictUnifier.get(pipeMedium, material, 2),
                        "PPP", "wCh", "PPP",
                        'P', new UnificationEntry(plateCurved, material),
                        'C', "craftingToolBendingCylinder");

                ModHandler.addShapedRecipe(String.format("pipe_ga_large_%s", material.toString()), OreDictUnifier.get(pipeLarge, material),
                        "PhP", "PCP", "PwP",
                        'P', new UnificationEntry(plateCurved, material),
                        'C', "craftingToolBendingCylinder");
            }
        }

        // Unification Recipes
        int voltageMultiplier = material.blastFurnaceTemperature == 0 ? 1 : material.blastFurnaceTemperature > 2000 ? 16 : 4;

        // Uncrafting Recipes
        MACERATOR_RECIPES.recipeBuilder().EUt(8 * voltageMultiplier).duration(30)
                .input(plateCurved, material)
                .output(dust, material)
                .buildAndRegister();

        FLUID_EXTRACTION_RECIPES.recipeBuilder().EUt(32 * voltageMultiplier).duration(80)
                .input(plateCurved, material)
                .fluidOutputs(material.getFluid(L))
                .buildAndRegister();

        ARC_FURNACE_RECIPES.recipeBuilder().EUt(30 * voltageMultiplier).duration(85)
                .input(plateCurved, material)
                .output(ingot, material.arcSmeltInto == null ? material : material.arcSmeltInto)
                .buildAndRegister();
    }

    /**
     * Rotor Material Handler. Generates:
     *
     * + Curved Plate Rotor Recipes if enabled
     * + Assembler Rotor Recipe that GTCE removed
     */
    private static void processRotor(OrePrefix ingot, IngotMaterial material) {
        if (!OreDictUnifier.get(rotor, material).isEmpty()) {

            if (GAConfig.GT6.BendingRotors) {

                // Register Curved Plate Rotor Recipes
                removeCraftingRecipes(OreDictUnifier.get(rotor, material));

                ModHandler.addShapedRecipe(String.format("ga_rotor_%s", material.toString()), OreDictUnifier.get(rotor, material),
                        "ChC", "SRf", "CdC",
                        'C', new UnificationEntry(plateCurved, material),
                        'S', new UnificationEntry(screw, material),
                        'R', new UnificationEntry(ring, material));

                ASSEMBLER_RECIPES.recipeBuilder().duration(240).EUt(24)
                        .input(plateCurved, material, 4)
                        .input(ring, material)
                        .fluidInputs(SolderingAlloy.getFluid(32))
                        .output(rotor, material)
                        .buildAndRegister();
            } else {

                ASSEMBLER_RECIPES.recipeBuilder().duration(240).EUt(24)
                        .input(plate, material, 4)
                        .input(ring, material)
                        .fluidInputs(SolderingAlloy.getFluid(32))
                        .output(rotor, material)
                        .buildAndRegister();
            }
        }
    }

    /**
     * Tiny Pipe Material Handler. Generates:
     *
     * + Tiny Pipe Handcrafting Recipes, if Curved Plate recipes are disabled.
     *       This is needed because GTCE does not generate these recipes normally.
     */
    private static void processTinyPipe(OrePrefix prefix, IngotMaterial material) {
        if (!GAConfig.GT6.BendingPipes)

            ModHandler.addShapedRecipe(String.format("pipe_ga_tiny_%s", material.toString()), OreDictUnifier.get(pipeTiny, material, 8),
                    "PPP", "h w", "PPP",
                    'P', new UnificationEntry(plate, material));
    }

    /**
     * Large Pipe Material Handler. Generates:
     *
     * + Large Pipe Handcrafting Recipes, if Curved Plate recipes are disabled.
     *       This is needed because GTCE does not generate these recipes normally.
     */
    private static void processLargePipe(OrePrefix prefix, IngotMaterial material) {
        if (!GAConfig.GT6.BendingPipes)

            ModHandler.addShapedRecipe(String.format("pipe_ga_large_%s", material.toString()), OreDictUnifier.get(pipeLarge, material),
                    "PhP", "P P", "PwP",
                    'P', new UnificationEntry(plate, material));
    }

    /**
     * Metal Casing Material Handler. Generates:
     *
     * + Autogenerated Metal Casing Handcrafting and Assembler Recipes
     * + Casing Unification Recipes
     */
    private static void processMetalCasing(OrePrefix prefix, IngotMaterial material) {

        if (material.hasFlag(GENERATE_METAL_CASING)) {
            ItemStack metalCasingStack = OreDictUnifier.get(prefix, material, 3);

            ModHandler.addShapedRecipe(String.format("autogen_metal_casing_%s", material), metalCasingStack,
                    "PhP", "PBP", "PwP",
                    'P', new UnificationEntry(plate, material),
                    'B', new UnificationEntry(frameGt, material));

            ASSEMBLER_RECIPES.recipeBuilder().EUt(16).duration(50)
                    .input(plate, material, 6)
                    .input(frameGt, material)
                    .circuitMeta(0)
                    .outputs(metalCasingStack)
                    .buildAndRegister();

            int voltageMultiplier = material.blastFurnaceTemperature == 0 ? 1 : material.blastFurnaceTemperature > 2000 ? 16 : 4;

            ARC_FURNACE_RECIPES.recipeBuilder().EUt(30 * voltageMultiplier).duration(382)
                    .input(gtMetalCasing, material)
                    .output(ingot, material, 6)
                    .output(nugget, material, 3)
                    .buildAndRegister();

            FLUID_EXTRACTION_RECIPES.recipeBuilder().EUt(32 * voltageMultiplier).duration(510)
                    .input(gtMetalCasing, material)
                    .fluidOutputs(material.getFluid(918))
                    .buildAndRegister();

            MACERATOR_RECIPES.recipeBuilder().EUt(8 * voltageMultiplier).duration(191)
                    .input(gtMetalCasing, material)
                    .output(dust, material, 6)
                    .output(dustTiny, material, 3)
                    .buildAndRegister();
        }
    }

    /**
     * Turbine Material Handler. Generates:
     *
     * + Small, Medium, Large, and Huge Turbine Assembler Recipes
     * + Turbine Blade Forming Press Recipes
     *
     * - Removes GTCE Recipes for Turbines and Blades, as well as the Handcrafting Turbine Blades Recipes
     */
    private static void processTurbine(OrePrefix toolPrefix, IngotMaterial material) {

        // Remove GTCE Turbine/Blade recipes
        removeRecipesByInputs(ASSEMBLER_RECIPES, OreDictUnifier.get(plate, material, 5), OreDictUnifier.get(screw, material, 2), getIntegratedCircuit(10));
        removeRecipesByInputs(ASSEMBLER_RECIPES, OreDictUnifier.get(stickLong, Titanium), OreDictUnifier.get(turbineBlade, material, 8));
        removeRecipeByName(String.format("gtadditions:turbine_blade_%s", material));

        // Generate the Turbine item with proper stats
        ItemStack hugeTurbineRotorStackForm = HUGE_TURBINE_ROTOR.getStackForm();
        ItemStack largeTurbineRotorStackForm = LARGE_TURBINE_ROTOR.getStackForm();
        ItemStack mediumTurbineRotorStackForm = MEDIUM_TURBINE_ROTOR.getStackForm();
        ItemStack smallTurbineRotorStackForm = SMALL_TURBINE_ROTOR.getStackForm();

        TurbineRotorBehavior.getInstanceFor(smallTurbineRotorStackForm).setPartMaterial(smallTurbineRotorStackForm, material);
        TurbineRotorBehavior.getInstanceFor(mediumTurbineRotorStackForm).setPartMaterial(mediumTurbineRotorStackForm, material);
        TurbineRotorBehavior.getInstanceFor(largeTurbineRotorStackForm).setPartMaterial(largeTurbineRotorStackForm, material);
        TurbineRotorBehavior.getInstanceFor(hugeTurbineRotorStackForm).setPartMaterial(hugeTurbineRotorStackForm, material);

        // Turbine Recipes
        ASSEMBLER_RECIPES.recipeBuilder().duration(200).EUt(400)
                .circuitMeta(0)
                .input(turbineBlade, material, 4)
                .input(stickLong, Titanium)
                .outputs(smallTurbineRotorStackForm)
                .buildAndRegister();

        ASSEMBLER_RECIPES.recipeBuilder().duration(400).EUt(800)
                .circuitMeta(1)
                .input(turbineBlade, material, 8)
                .input(stickLong, Tungsten)
                .outputs(mediumTurbineRotorStackForm)
                .buildAndRegister();

        ASSEMBLER_RECIPES.recipeBuilder().duration(800).EUt(1600)
                .circuitMeta(2)
                .input(turbineBlade, material, 16)
                .input(stickLong, Osmium)
                .outputs(largeTurbineRotorStackForm)
                .buildAndRegister();

        ASSEMBLER_RECIPES.recipeBuilder().duration(1600).EUt(3200)
                .circuitMeta(3)
                .input(turbineBlade, material, 32)
                .input(stickLong, Rutherfordium)
                .outputs(hugeTurbineRotorStackForm)
                .buildAndRegister();

        // Turbine Blade recipe
        FORMING_PRESS_RECIPES.recipeBuilder().duration(20).EUt(256)
                .input(plateDouble, material, 5)
                .input(screw, material, 2)
                .output(toolPrefix, material)
                .buildAndRegister();
    }

    /**
     * Lens Material Handler. Generates:
     *
     * + Exquisite Gem -> Lens Recipes
     *
     * - Plate -> Lens Recipes
     */
    private static void processLens(OrePrefix gem, GemMaterial material) {

        removeRecipesByInputs(LATHE_RECIPES, OreDictUnifier.get(plate, material));

        if (!OreDictUnifier.get(gemExquisite, material).isEmpty()) {
            LATHE_RECIPES.recipeBuilder().duration(2400).EUt(30)
                    .input(gemExquisite, material)
                    .output(lens, material)
                    .output(dust, material, 2)
                    .buildAndRegister();
        } else {
            LATHE_RECIPES.recipeBuilder().duration(2400).EUt(30)
                    .input(plate, material)
                    .output(lens, material)
                    .output(dustTiny, material, 2)
                    .buildAndRegister();
        }
    }

    /**
     * Spring Material Handler. Generates:
     *
     * + Crafting Table Recipe for Spring
     */
    private static void processSpring(OrePrefix prefix, IngotMaterial material) {

        ModHandler.addShapedRecipe(String.format("spring_%s", material.toString()), OreDictUnifier.get(spring, material),
                " s ", "fRx", " R ",
                'R', new UnificationEntry(stickLong, material));
    }

    /**
     * Small Spring Material Handler. Generates:
     *
     * + Crafting Table Recipe for Small Spring
     * + Bender Recipe for Small Spring
     *
     * - GTCE Fine Wire to Small Spring Recipe
     */
    private static void processSpringSmall(OrePrefix prefix, IngotMaterial material) {

        if (material.cableProperties != null)
            removeRecipesByInputs(BENDER_RECIPES, OreDictUnifier.get(wireGtSingle, material)); // todo double check that this works


        ModHandler.addShapedRecipe(String.format("spring_small_%s", material.toString()), OreDictUnifier.get(springSmall, material),
                " s ", "fRx",
                'R', new UnificationEntry(stick, material));

        BENDER_RECIPES.recipeBuilder().duration(100).EUt(8)
                .input(stick, material)
                .output(springSmall, material, 2)
                .circuitMeta(1)
                .buildAndRegister();
    }

    /**
     * Small Gear Material Handler. Generates:
     *
     * + Classic Crafting Table Recipe
     * + Extruder Recipe for Small Gears
     *
     * - Removes Forge Hammer Recipe for Small Gears
     */
    private static void processGearSmall(OrePrefix prefix, IngotMaterial material) {

        if (material.hasFlag(GENERATE_SMALL_GEAR)) {
            removeRecipeByName(String.format("gtadditions:small_gear_%s", material.toString()));

            ModHandler.addShapedRecipe(String.format("small_gear_%s", material.toString()), OreDictUnifier.get(gearSmall, material),
                    " R ", "hPx", " R ",
                    'R', new UnificationEntry(stick, material),
                    'P', new UnificationEntry(plate, material));

            removeRecipesByInputs(FORGE_HAMMER_RECIPES, OreDictUnifier.get(plate, material, 2));

            EXTRUDER_RECIPES.recipeBuilder().duration((int) material.getAverageMass()).EUt(120)
                    .input(ingot, material)
                    .notConsumable(SHAPE_EXTRUDER_SMALL_GEAR.getStackForm())
                    .output(gearSmall, material)
                    .buildAndRegister();
        }
    }

    private static void registerLargeMachineRecipes(RecipeMap<?> mapToCopy, RecipeMap<LargeRecipeBuilder> mapToForm) {

        for (Recipe recipe : mapToCopy.getRecipeList()) {

            LargeRecipeBuilder largeRecipeBuilder = mapToForm.recipeBuilder()
                    .EUt(recipe.getEUt())
                    .duration(recipe.getDuration())
                    .inputsIngredients(recipe.getInputs())
                    .outputs(recipe.getOutputs())
                    .fluidInputs(recipe.getFluidInputs())
                    .fluidOutputs(recipe.getFluidOutputs());

            // TODO Giving better way to do this in GTCE
            for (Recipe.ChanceEntry entry : recipe.getChancedOutputs())
                largeRecipeBuilder.chancedOutput(entry.getItemStack(), entry.getChance(), entry.getBoostPerTier());

            largeRecipeBuilder.buildAndRegister();
        }
    }

    private static void registerLargeForgeHammerRecipes() {
        FORGE_HAMMER_RECIPES.getRecipeList().forEach(recipe -> {
            LargeRecipeBuilder builder = LARGE_FORGE_HAMMER_RECIPES.recipeBuilder()
                    .EUt(recipe.getEUt())
                    .duration(recipe.getDuration())
                    .fluidInputs(Lubricant.getFluid(2))
                    .inputsIngredients(recipe.getInputs())
                    .outputs(recipe.getOutputs())
                    .fluidOutputs(recipe.getFluidOutputs());

            recipe.getChancedOutputs().forEach(chanceEntry -> builder.chancedOutput(chanceEntry.getItemStack(), chanceEntry.getChance(), chanceEntry.getBoostPerTier()));
            builder.buildAndRegister();
        });
    }

    /**
     * Large Mixer Recipe Creation.
     * Copies the Mixer RecipeMap.
     *
     * This RecipeMap also applies a circuit to every recipe to avoid conflicts.
     */
    private static void registerLargeMixerRecipes() {
        MIXER_RECIPES.getRecipeList().forEach(recipe -> {
            List<CountableIngredient> inputList = new ArrayList<>();
            IntCircuitIngredient circuitIngredient = null;

            // Make sure 2 circuits do not end up in the recipe
            for (CountableIngredient input : recipe.getInputs()) {
                if (!(input.getIngredient() instanceof IntCircuitIngredient))
                    inputList.add(input);
                else
                    circuitIngredient = (IntCircuitIngredient) input.getIngredient();
            }

            if (circuitIngredient == null)
                circuitIngredient = new IntCircuitIngredient(inputList.size() + recipe.getFluidInputs().size());

            LARGE_MIXER_RECIPES.recipeBuilder()
                    .notConsumable(circuitIngredient)
                    .EUt(recipe.getEUt())
                    .duration(recipe.getDuration())
                    .fluidInputs(recipe.getFluidInputs())
                    .inputsIngredients(inputList)
                    .outputs(recipe.getOutputs())
                    .fluidOutputs(recipe.getFluidOutputs())
                    .buildAndRegister();
        });
    }

    /**
     * Alloy Blast Furnace Recipe creation.
     * Uses the Large Mixer RecipeMap to look up compositions of alloys.
     *
     * This Recipe Registration MUST be run after the Large Mixer recipe addition.
     */
    private static void registerAlloyBlastRecipes() {
        for (Material material : Material.MATERIAL_REGISTRY) {
            if (!(material instanceof IngotMaterial))
                continue;
            IngotMaterial ingotMaterial = (IngotMaterial) material;
            if (ingotMaterial.blastFurnaceTemperature == 0)
                continue;

            // This could use some code cleanup
            LARGE_MIXER_RECIPES.getRecipeList().stream()
                    .filter(recipe -> recipe.getOutputs().size() == 1)
                    .filter(recipe -> recipe.getFluidOutputs().size() == 0)
                    .filter(recipe -> recipe.getOutputs().get(0).isItemEqualIgnoreDurability(OreDictUnifier.get(dust, ingotMaterial)))
                    .findFirst()
                    .ifPresent(recipe -> {
                        ItemStack itemStack = recipe.getOutputs().get(0);
                        IngotMaterial ingot = ((IngotMaterial) (OreDictUnifier.getUnificationEntry(itemStack).material));
                        int duration = Math.max(1, (int) (ingot.getAverageMass() * ingot.blastFurnaceTemperature / 50L));
                        BLAST_ALLOY_RECIPES.recipeBuilder()
                                .duration(duration * 80 / 100)
                                .EUt(120 * itemStack.getCount())
                                .fluidInputs(recipe.getFluidInputs())
                                .inputsIngredients(recipe.getInputs())
                                .fluidOutputs(ingot.getFluid(itemStack.getCount() * GTValues.L)).buildAndRegister();
                    });
        }

        // Soldering Alloy
        BLAST_ALLOY_RECIPES.recipeBuilder().duration(775).EUt(1200)
                .input(dust, Tin, 9)
                .input(dust, Antimony)
                .fluidOutputs(SolderingAlloy.getFluid(L * 10))
                .buildAndRegister();

        // Red Alloy
        BLAST_ALLOY_RECIPES.recipeBuilder().duration(473).EUt(240)
                .input(dust, Redstone, 3)
                .input(dust, Copper)
                .fluidOutputs(RedAlloy.getFluid(L * 4))
                .buildAndRegister();

        // Magnalium
        BLAST_ALLOY_RECIPES.recipeBuilder().duration(320).EUt(360)
                .input(dust, Aluminium, 2)
                .input(dust, Magnesium)
                .fluidOutputs(Magnalium.getFluid(L * 3))
                .buildAndRegister();

        // Tin Alloy
        BLAST_ALLOY_RECIPES.recipeBuilder().duration(556).EUt(174)
                .input(dust, Tin)
                .input(dust, Iron)
                .fluidOutputs(TinAlloy.getFluid(L * 2))
                .buildAndRegister();

        BLAST_ALLOY_RECIPES.recipeBuilder().duration(556).EUt(174)
                .input(dust, Tin)
                .input(dust, WroughtIron)
                .fluidOutputs(TinAlloy.getFluid(L * 2))
                .buildAndRegister();

        // Battery Alloy
        BLAST_ALLOY_RECIPES.recipeBuilder().duration(512).EUt(600)
                .input(dust, Lead, 4)
                .input(dust, Antimony)
                .fluidOutputs(BatteryAlloy.getFluid(L * 5))
                .buildAndRegister();

        // Reactor Steel
        BLAST_ALLOY_RECIPES.recipeBuilder().duration(12000).EUt(120)
                .notConsumable(new IntCircuitIngredient(5))
                .input(dust, Iron, 15)
                .input(dust, Niobium, 1)
                .input(dust, Vanadium, 4)
                .input(dust, Carbon, 2)
                .fluidInputs(Argon.getFluid(1000))
                .fluidOutputs(ReactorSteel.getFluid(L * 22))
                .buildAndRegister();
    }

    /**
     * Chemical Plant Recipe creation.
     * Copies the Brewer RecipeMap loosely.
     */
    private static void registerChemicalPlantRecipes() {
        BREWING_RECIPES.getRecipeList().forEach(recipe -> {
            FluidStack fluidInput = recipe.getFluidInputs().get(0).copy();
            fluidInput.amount = (fluidInput.amount * 10 * 125 / 100);
            CountableIngredient itemInput = new CountableIngredient(recipe.getInputs().get(0).getIngredient(), recipe.getInputs().get(0).getCount() * 10);
            FluidStack fluidOutput = FermentationBase.getFluid(recipe.getFluidOutputs().get(0).amount * 10);

            CHEMICAL_PLANT_RECIPES.recipeBuilder()
                    .EUt(recipe.getEUt() * 10)
                    .duration(recipe.getDuration() * 10)
                    .fluidInputs(fluidInput)
                    .inputsIngredients(Collections.singleton(itemInput))
                    .fluidOutputs(fluidOutput)
                    .buildAndRegister();
        });
    }

    /**
     * Green House Recipe creation.
     */
    private static void registerGreenHouseRecipes() {

        final List<Object> fertilizers = Arrays.asList(
            null, 1000,
            new ItemStack(Items.DYE, 1, 15), 900,
            OreDictUnifier.get(dust, OrganicFertilizer), 600
        );

        final List<Item> inputs = Arrays.asList(
                Items.CARROT,
                Item.getItemFromBlock(Blocks.CACTUS),
                Items.REEDS,
                Item.getItemFromBlock(Blocks.BROWN_MUSHROOM),
                Item.getItemFromBlock(Blocks.RED_MUSHROOM),
                Items.BEETROOT);

        // Create Recipes for Vanilla crops
        for (int i = 0; i < fertilizers.size() / 2; i++) {

            List<RecipeBuilder<?>> recipes = new ArrayList<>();

            ItemStack fertilizer = (ItemStack) fertilizers.get(i * 2);
            int duration = (int) fertilizers.get(i * 2 + 1);

            // Potato
            recipes.add(GREEN_HOUSE_RECIPES.recipeBuilder().duration(duration)
                    .circuitMeta(i)
                    .fluidInputs(Water.getFluid(2000))
                    .notConsumable(new ItemStack(Items.POTATO))
                    .outputs(new ItemStack(Items.POTATO, i))
                    .chancedOutput(new ItemStack(Items.POISONOUS_POTATO, 1), 100, 50));

            // Melon
            GREEN_HOUSE_RECIPES.recipeBuilder().duration(1000)
                    .circuitMeta(i)
                    .fluidInputs(Water.getFluid(2000))
                    .notConsumable(new ItemStack(Items.MELON_SEEDS))
                    .outputs(new ItemStack(Items.MELON, i))
                    .chancedOutput(new ItemStack(Items.MELON_SEEDS), 100, 50)
                    .buildAndRegister();

            // Pumpkin
            GREEN_HOUSE_RECIPES.recipeBuilder().duration(1000)
                    .circuitMeta(i)
                    .fluidInputs(Water.getFluid(2000))
                    .notConsumable(new ItemStack(Items.PUMPKIN_SEEDS))
                    .outputs(new ItemStack(Blocks.PUMPKIN, i))
                    .chancedOutput(new ItemStack(Items.PUMPKIN_SEEDS), 100, 50)
                    .buildAndRegister();

            for (Item input : inputs) {

                recipes.add(GREEN_HOUSE_RECIPES.recipeBuilder().duration(duration)
                        .circuitMeta(i)
                        .fluidInputs(Water.getFluid(2000))
                        .notConsumable(new ItemStack(input))
                        .outputs(new ItemStack(input, i)));
            }

            if (fertilizer != null)
                recipes.stream()
                       .map(r -> r.inputs(fertilizer))
                       .forEach(RecipeBuilder::buildAndRegister);
        }

        // Search for seeds in the OreDictionary to find other recipes to add
        Arrays.stream(OreDictionary.getOreNames()).filter(name -> name.startsWith("seed")).forEach(name -> {

            String oreName = name.substring(4);

            if (oreName.length() <= 0)
                return;

            String seedName = "seed" + titleCase(oreName);
            String cropName = "essence" + titleCase(oreName);

            List<ItemStack> registeredSeeds = OreDictionary.getOres(seedName, false);
            List<ItemStack> registeredCrops = OreDictionary.getOres(cropName, false);

            if (registeredSeeds.isEmpty() || registeredCrops.isEmpty())
                return;

            ItemStack seed = registeredSeeds.get(0).copy();
            ItemStack essence = registeredCrops.get(0).copy();

            GREEN_HOUSE_RECIPES.recipeBuilder().duration(1000)
                    .fluidInputs(Water.getFluid(2000))
                    .circuitMeta(0)
                    .notConsumable(seed)
                    .outputs(essence)
                    .chancedOutput(seed, 100, 50)
                    .buildAndRegister();

            essence = registeredCrops.get(0).copy();
            essence.setCount(3);
            GREEN_HOUSE_RECIPES.recipeBuilder().duration(600)
                    .fluidInputs(Water.getFluid(2000))
                    .circuitMeta(2)
                    .notConsumable(seed)
                    .input(dust, OrganicFertilizer)
                    .outputs(essence)
                    .chancedOutput(seed, 100, 50)
                    .buildAndRegister();
        });
    }

    /**
     * Recipe Registration method for any recipes that need to iterate over
     * ALL registered materials. Generates:
     *
     * - Large Centrifuge autogenerated recipes
     * - Matter Replication Recipes
     */
    public static void runRecipeGeneration() {
        for (Material material : Material.MATERIAL_REGISTRY) {

            // Decomposition Recipes
            if (material instanceof FluidMaterial) {
                OrePrefix prefix = material instanceof DustMaterial ? dust : null;
                processDecomposition(prefix, (FluidMaterial) material);
            }

            // Matter Replication Recipes
            if (material.element != null) {
                matterReplication(material);
            }
        }
    }

    // TODO This method needs work
    private static void processDecomposition(OrePrefix decomposePrefix, FluidMaterial material) {
        if (material.materialComponents.isEmpty() || !material.hasFlag(DECOMPOSITION_BY_CENTRIFUGING) ||
                //disable decomposition if explicitly disabled for this material or for one of it's components
                material.hasFlag(DISABLE_DECOMPOSITION)) return;

        ArrayList<ItemStack> outputs = new ArrayList<>();
        ArrayList<FluidStack> fluidOutputs = new ArrayList<>();
        int totalInputAmount = 0;

        //compute outputs
        for (MaterialStack component : material.materialComponents) {
            totalInputAmount += component.amount;
            if (component.material instanceof DustMaterial) {
                outputs.add(OreDictUnifier.get(dust, component.material, (int) component.amount));
            } else if (component.material instanceof FluidMaterial) {
                FluidMaterial componentMaterial = (FluidMaterial) component.material;
                fluidOutputs.add(componentMaterial.getFluid((int) (1000 * component.amount)));
            }
        }

        //generate builder
        RecipeBuilder<?> builder;
        if (!material.hasFlag(DECOMPOSITION_BY_ELECTROLYZING)) {
            builder = LARGE_CENTRIFUGE_RECIPES.recipeBuilder()
                    .duration((int) Math.ceil(material.getAverageMass() * totalInputAmount * 1.5))
                    .EUt(30);
        } else {
            return;
        }
        builder.outputs(outputs);
        builder.fluidOutputs(fluidOutputs);

        //finish builder
        if (decomposePrefix != null) {
            builder.input(decomposePrefix, material, totalInputAmount);
        } else {
            builder.fluidInputs(material.getFluid(1000 * totalInputAmount));
        }
        if (material.hasFlag(DECOMPOSITION_REQUIRES_HYDROGEN)) {
            builder.fluidInputs(Hydrogen.getFluid(1000 * totalInputAmount));
        }

        //register recipe
        builder.buildAndRegister();
    }

    /**
     * Matter Replication Recipe Generation. Formerly was its own file.
     */
    private static void matterReplication(Material material) {

        int mass = (int) material.getMass();
        FluidStack uuFluid = mass % 2 == 0 ?
                BosonicUUMatter.getFluid(mass) :
                FermionicUUMatter.getFluid(mass);

        if (((FluidMaterial) material).getMaterialFluid() != null) {

            int amount = material instanceof IngotMaterial ? GTValues.L : 1000;

            MASS_FAB_RECIPES.recipeBuilder()
                    .fluidInputs(((FluidMaterial) material).getFluid(amount))
                    .fluidOutputs(uuFluid)
                    .fluidOutputs(FreeElectronGas.getFluid(mass))
                    .duration(mass * GAConfig.Misc.replicationTimeFactor)
                    .EUt(32)
                    .buildAndRegister();

            if (!material.hasFlag(DISABLE_REPLICATION)) {

                REPLICATOR_RECIPES.recipeBuilder()
                        .fluidOutputs(((FluidMaterial) material).getFluid(amount))
                        .notConsumable(((FluidMaterial) material).getFluid(amount))
                        .fluidInputs(uuFluid)
                        .fluidInputs(FreeElectronGas.getFluid(mass))
                        .duration(mass * GAConfig.Misc.replicationTimeFactor)
                        .EUt(32)
                        .buildAndRegister();

                REPLICATOR_RECIPES.recipeBuilder()
                        .fluidOutputs(((FluidMaterial) material).getFluid(amount))
                        .notConsumable(((FluidMaterial) material).getFluid(amount))
                        .fluidInputs(UUMatter.getFluid(mass))
                        .duration(mass * GAConfig.Misc.replicationTimeFactor)
                        .EUt(32)
                        .buildAndRegister();
            }
        } else {

            MASS_FAB_RECIPES.recipeBuilder()
                    .input(dust, material)
                    .fluidOutputs(uuFluid)
                    .fluidOutputs(FreeElectronGas.getFluid(mass))
                    .duration(mass * GAConfig.Misc.replicationTimeFactor)
                    .EUt(32)
                    .buildAndRegister();

            if (!material.hasFlag(DISABLE_REPLICATION)) {

                REPLICATOR_RECIPES.recipeBuilder()
                        .output(dust, material)
                        .notConsumable(dust, material)
                        .fluidInputs(uuFluid)
                        .fluidInputs(FreeElectronGas.getFluid(mass))
                        .duration(mass * GAConfig.Misc.replicationTimeFactor)
                        .EUt(32)
                        .buildAndRegister();

                REPLICATOR_RECIPES.recipeBuilder()
                        .output(dust, material)
                        .notConsumable(dust, material)
                        .fluidInputs(UUMatter.getFluid(mass))
                        .duration(mass * GAConfig.Misc.replicationTimeFactor)
                        .EUt(32)
                        .buildAndRegister();
            }
        }
    }

    /**
     * Recipe Generation for any recipes that need to iterate over the entire Crafting Table recipe registry. Generates:
     *
     * - Compressor Block Crafting Recipes if enabled (Removes Handcrafting Recipes)
     * - Packer Block Compression Recipes if not enabled
     *
     * - Packer 3x3 Recipes that are not Ingots or Dusts (like Mystical Agriculture recipes)
     * - Packer 2x2 Recipes that are not Ingots or Dusts
     *
     * - Forge Hammer Block Uncrafting Recipes if enabled (Removes Hand Uncrafting Recipes)
     * - Unpacker Block Uncrafting Recipes if not enabled
     */
    public static void generatedRecipes() {

        List<ResourceLocation> recipesToRemove = new ArrayList<>();
        for (IRecipe recipe : CraftingManager.REGISTRY) {

            if (recipe.getRecipeOutput().isEmpty())
                continue;

            ItemStack stack = getTopLeft(recipe);
            if (stack == null)
                continue;

            if (recipe.getIngredients().size() == 9) {

                if (outputIsNot(recipe, Blocks.AIR) && isSingleIngredient(recipe)) {

                    // Remove 3x3 Block Handcrafting Recipes
                    if (GAConfig.GT5U.Remove3x3BlockRecipes)
                        recipesToRemove.add(recipe.getRegistryName());

                    if (GAConfig.GT5U.GenerateCompressorRecipes
                            && !recipe.getIngredients().get(0).test(new ItemStack(Items.WHEAT))) {

                        // Add Compressor Blockcrafting Recipes (excluding Wheat)
                        COMPRESSOR_RECIPES.recipeBuilder().duration(400).EUt(2)
                                .inputs(CountableIngredient.from(stack, 9))
                                .outputs(recipe.getRecipeOutput())
                                .buildAndRegister();

                    } else {

                        // Add Packager Recipes if Compressor Recipes are not generated
                        PACKER_RECIPES.recipeBuilder().duration(100).EUt(4)
                                .inputs(CountableIngredient.from(stack, 9))
                                .notConsumable(SCHEMATIC_3X3.getStackForm())
                                .outputs(recipe.getRecipeOutput())
                                .buildAndRegister();
                    }

                    if (!recipesToRemove.contains(recipe.getRegistryName())
                            && !hasPrefix(recipe.getRecipeOutput(), "dust", "dustTiny")
                            && !hasPrefix(recipe.getRecipeOutput(), "ingot")
                            && recipe.getRecipeOutput().getCount() == 1
                            && GAConfig.Misc.Packager3x3Recipes) {

                        // Add Packager 3x3 Recipes, excluding dusts and ingots
                        PACKER_RECIPES.recipeBuilder().duration(100).EUt(4)
                                .inputs(CountableIngredient.from(stack, 9))
                                .notConsumable(SCHEMATIC_3X3.getStackForm())
                                .outputs(recipe.getRecipeOutput())
                                .buildAndRegister();
                    }
                }
            }

            if (recipe.getIngredients().size() == 4) {

                if (outputIsNot(recipe, Blocks.QUARTZ_BLOCK) && isSingleIngredient(recipe)) {

                    if (!recipesToRemove.contains(recipe.getRegistryName())
                            && !hasPrefix(recipe.getRecipeOutput(), "dust", "dustSmall")
                            && recipe.getRecipeOutput().getCount() == 1
                            && GAConfig.Misc.Packager2x2Recipes) {

                        // Add Packager 2x2 Recipes, excluding dusts
                        PACKER_RECIPES.recipeBuilder().duration(100).EUt(4)
                                .inputs(CountableIngredient.from(stack, 4))
                                .notConsumable(SCHEMATIC_2X2.getStackForm())
                                .outputs(recipe.getRecipeOutput())
                                .buildAndRegister();
                    }
                }
            }

            if (recipe.getIngredients().size() == 1 && recipe.getRecipeOutput().getCount() == 9) {

                if (!hasPrefix(recipe.getIngredients().get(0).getMatchingStacks()[0], "ingot")) {

                    if (outputIsNot(recipe, Blocks.AIR, Blocks.SLIME_BLOCK)) {

                        // Remove Block Uncrafting Recipes
                        if (GAConfig.GT5U.RemoveBlockUncraftingRecipes)
                            recipesToRemove.add(recipe.getRegistryName());

                        if (!hasPrefix(recipe.getRecipeOutput(), "ingot")) {

                            // Add Forge Hammer block recipes, excluding ingots
                            FORGE_HAMMER_RECIPES.recipeBuilder().duration(100).EUt(24)
                                    .inputs(stack)
                                    .outputs(recipe.getRecipeOutput())
                                    .buildAndRegister();
                        }
                    }

                    if (!recipesToRemove.contains(recipe.getRegistryName())
                            && GAConfig.Misc.Unpackager3x3Recipes) {

                        // Add Unpackager 3x3 recipes, excluding ingots
                        UNPACKER_RECIPES.recipeBuilder().duration(100).EUt(8)
                                .inputs(stack)
                                .notConsumable(SCHEMATIC_3X3.getStackForm())
                                .outputs(recipe.getRecipeOutput())
                                .buildAndRegister();
                    }
                }
            }
        }
        recipesToRemove.forEach(HelperMethods::removeRecipeByName);
    }
}
