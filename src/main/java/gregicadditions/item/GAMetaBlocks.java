package gregicadditions.item;

import gregicadditions.Gregicality;
import gregicadditions.blocks.GABlockOre;
import gregicadditions.blocks.GAMetalCasing;
import gregicadditions.item.components.*;
import gregicadditions.pipelike.opticalfiber.BlockOpticalFiber;
import gregicadditions.pipelike.opticalfiber.OpticalFiberSize;
import gregicadditions.pipelike.opticalfiber.tile.TileEntityOpticalFiber;
import gregicadditions.pipelike.opticalfiber.tile.TileEntityOpticalFiberTickable;
import gregicadditions.renderer.OpticalFiberRenderer;
import gregicadditions.utils.GregicalityLogger;
import gregtech.api.recipes.RecipeMaps;
import gregtech.api.recipes.machines.FuelRecipeMap;
import gregtech.api.render.ICubeRenderer;
import gregtech.api.unification.OreDictUnifier;
import gregtech.api.unification.material.Materials;
import gregtech.api.unification.material.type.DustMaterial;
import gregtech.api.unification.material.type.IngotMaterial;
import gregtech.api.unification.material.type.Material;
import gregtech.api.unification.ore.OrePrefix;
import gregtech.api.unification.ore.StoneType;
import gregtech.common.blocks.MetaBlocks;
import gregtech.common.metatileentities.multi.electric.generator.MetaTileEntityLargeTurbine;
import gregtech.common.pipelike.fluidpipe.FluidPipeProperties;
import net.minecraft.block.Block;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.statemap.DefaultStateMapper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.commons.lang3.ArrayUtils;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.stream.Collectors;

import static gregicadditions.ClientProxy.*;
import static gregicadditions.GAMaterials.GENERATE_METAL_CASING;

public class GAMetaBlocks {

    public static GAMultiblockCasing MUTLIBLOCK_CASING;

    public static GAReactorCasing REACTOR_CASING;

    public static GAFusionCasing FUSION_CASING;

    public static GAMachineCasing MACHINE_CASING;

    public static GATransparentCasing TRANSPARENT_CASING;

    public static CellCasing CELL_CASING;

    public static MotorCasing MOTOR_CASING;

    public static ConveyorCasing CONVEYOR_CASING;

    public static EmitterCasing EMITTER_CASING;

    public static FieldGenCasing FIELD_GEN_CASING;

    public static PistonCasing PISTON_CASING;

    public static PumpCasing PUMP_CASING;

    public static RobotArmCasing ROBOT_ARM_CASING;

    public static SensorCasing SENSOR_CASING;

    public static Map<IngotMaterial, GAMetalCasing> METAL_CASING = new HashMap<>();

    public static Collection<GABlockOre> GA_ORES = new HashSet<>();


    public static BlockOpticalFiber OPTICAL_FIBER;


    public static void init() {
        for (Material mat : Material.MATERIAL_REGISTRY) {
            if (mat instanceof DustMaterial && mat.hasFlag(DustMaterial.MatFlags.GENERATE_ORE)) {
                createOreBlock((DustMaterial) mat);
            }
        }
        MUTLIBLOCK_CASING = new GAMultiblockCasing();
        MUTLIBLOCK_CASING.setRegistryName("ga_multiblock_casing");

        REACTOR_CASING = new GAReactorCasing();
        REACTOR_CASING.setRegistryName("ga_reactor_casing");

        FUSION_CASING = new GAFusionCasing();
        FUSION_CASING.setRegistryName("ga_fusion_casing");

        MACHINE_CASING = new GAMachineCasing();
        MACHINE_CASING.setRegistryName("ga_machine_casing");

        TRANSPARENT_CASING = new GATransparentCasing();
        TRANSPARENT_CASING.setRegistryName("ga_transparent_casing");

        CELL_CASING = new CellCasing();
        CELL_CASING.setRegistryName("ga_cell_casing");

        MOTOR_CASING = new MotorCasing();
        MOTOR_CASING.setRegistryName("ga_motor_casing");

        CONVEYOR_CASING = new ConveyorCasing();
        CONVEYOR_CASING.setRegistryName("ga_conveyor_casing");

        FIELD_GEN_CASING = new FieldGenCasing();
        FIELD_GEN_CASING.setRegistryName("ga_field_gen_casing");

        PISTON_CASING = new PistonCasing();
        PISTON_CASING.setRegistryName("ga_piston_casing");

        PUMP_CASING = new PumpCasing();
        PUMP_CASING.setRegistryName("ga_pump_casing");

        ROBOT_ARM_CASING = new RobotArmCasing();
        ROBOT_ARM_CASING.setRegistryName("ga_robot_arm_casing");

        SENSOR_CASING = new SensorCasing();
        SENSOR_CASING.setRegistryName("ga_sensor_casing");

        EMITTER_CASING = new EmitterCasing();
        EMITTER_CASING.setRegistryName("ga_emitter_casing");

        OPTICAL_FIBER = new BlockOpticalFiber();
        OPTICAL_FIBER.setRegistryName("ga_cable");

        MetaBlocks.FLUID_PIPE.addPipeMaterial(Materials.Ultimet, new FluidPipeProperties(1500, 12000, true));
        //MetaBlocks.FLUID_PIPE.addPipeMaterial(GAMaterials.Plasma, new FluidPipeProperties(1000000, 30, true));


        createMachineCasing();
        registerTileEntity();
        EnumHelper.addEnum(MetaTileEntityLargeTurbine.TurbineType.class, "STEAM_OVERRIDE",
                new Class[]{FuelRecipeMap.class, IBlockState.class, ICubeRenderer.class, boolean.class},
                RecipeMaps.STEAM_TURBINE_FUELS, GAMetaBlocks.getMetalCasingBlockState(Materials.Steel), GAMetaBlocks.METAL_CASING.get(Materials.Steel), true);
        EnumHelper.addEnum(MetaTileEntityLargeTurbine.TurbineType.class, "GAS_OVERRIDE",
                new Class[]{FuelRecipeMap.class, IBlockState.class, ICubeRenderer.class, boolean.class},
                RecipeMaps.GAS_TURBINE_FUELS, GAMetaBlocks.getMetalCasingBlockState(Materials.StainlessSteel), GAMetaBlocks.METAL_CASING.get(Materials.StainlessSteel), false);
        EnumHelper.addEnum(MetaTileEntityLargeTurbine.TurbineType.class, "PLASMA_OVERRIDE",
                new Class[]{FuelRecipeMap.class, IBlockState.class, ICubeRenderer.class, boolean.class},
                RecipeMaps.PLASMA_GENERATOR_FUELS, GAMetaBlocks.getMetalCasingBlockState(Materials.TungstenSteel), GAMetaBlocks.METAL_CASING.get(Materials.TungstenSteel), true);

    }

    private static void createOreBlock(DustMaterial material) {
        StoneType[] stoneTypeBuffer = new StoneType[16];
        int generationIndex = 0;
        for (StoneType stoneType : StoneType.STONE_TYPE_REGISTRY) {
            int id = StoneType.STONE_TYPE_REGISTRY.getIDForObject(stoneType), index = id / 16;
            if (index > generationIndex) {
                createOreBlock(material, copyNotNull(stoneTypeBuffer), generationIndex);
                Arrays.fill(stoneTypeBuffer, null);
            }
            stoneTypeBuffer[id % 16] = stoneType;
            generationIndex = index;
        }
        createOreBlock(material, copyNotNull(stoneTypeBuffer), generationIndex);
    }

    private static <T> T[] copyNotNull(T[] src) {
        int nullIndex = ArrayUtils.indexOf(src, null);
        return Arrays.copyOfRange(src, 0, nullIndex == -1 ? src.length : nullIndex);
    }

    private static void createOreBlock(DustMaterial material, StoneType[] stoneTypes, int index) {

        String[] orePrefixes = {"Rich", "Poor", "Pure"};

        for (String orePrefix : orePrefixes) {
            GABlockOre block = new GABlockOre(material, stoneTypes, OrePrefix.valueOf("ore" + orePrefix));
            block.setRegistryName("gregtech:" + orePrefix.toLowerCase() + "_ore_" + material + "_" + index);
            block.setTranslationKey(orePrefix.toLowerCase() + "_ore_block");
            GA_ORES.add(block);
        }

    }


    public static IBlockState getCompressedFromMaterial(Material material) {
        if (MetaBlocks.COMPRESSED.get(material) == null) {
            return null;
        }
        int index = MetaBlocks.COMPRESSED.get(material).variantProperty.getAllowedValues().indexOf(material);
        return MetaBlocks.COMPRESSED.get(material).getStateFromMeta(index);
    }

    @SideOnly(Side.CLIENT)
    public static void registerItemModels() {

        ModelLoader.setCustomMeshDefinition(Item.getItemFromBlock(OPTICAL_FIBER), stack -> OpticalFiberRenderer.MODEL_LOCATION);
        registerItemModel(MUTLIBLOCK_CASING);
        registerItemModel(REACTOR_CASING);
        registerItemModel(FUSION_CASING);
        registerItemModel(MACHINE_CASING);
        registerItemModel(TRANSPARENT_CASING);
        registerItemModel(CELL_CASING);
        registerItemModel(CONVEYOR_CASING);
        registerItemModel(EMITTER_CASING);
        registerItemModel(FIELD_GEN_CASING);
        registerItemModel(MOTOR_CASING);
        registerItemModel(PISTON_CASING);
        registerItemModel(PUMP_CASING);
        registerItemModel(ROBOT_ARM_CASING);
        registerItemModel(SENSOR_CASING);
        METAL_CASING.values().stream().distinct().forEach(GAMetaBlocks::registerItemModel);
        GA_ORES.stream().distinct().forEach(GAMetaBlocks::registerItemModel);
    }

    public static void registerTileEntity() {
        GameRegistry.registerTileEntity(TileEntityOpticalFiber.class, new ResourceLocation(Gregicality.MODID, "cable"));
        GameRegistry.registerTileEntity(TileEntityOpticalFiberTickable.class, new ResourceLocation(Gregicality.MODID, "cable_tickable"));
    }

    @SideOnly(Side.CLIENT)
    public static void registerStateMappers() {
        ModelLoader.setCustomStateMapper(OPTICAL_FIBER, new DefaultStateMapper() {
            @Override
            protected @NotNull ModelResourceLocation getModelResourceLocation(IBlockState state) {
                return OpticalFiberRenderer.MODEL_LOCATION;
            }
        });
    }

    @SideOnly(Side.CLIENT)
    public static void registerItemModel(Block block) {
        for (IBlockState state : block.getBlockState().getValidStates()) {
            //noinspection ConstantConditions
            ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(block),
                    block.getMetaFromState(state),
                    new ModelResourceLocation(block.getRegistryName(),
                            statePropertiesToString(state.getProperties())));
        }
    }

    @SideOnly(Side.CLIENT)
    public static void registerColors() {
        GAMetaBlocks.METAL_CASING.values().stream().distinct().forEach(block -> {
            Minecraft.getMinecraft().getBlockColors().registerBlockColorHandler(METAL_CASING_BLOCK_COLOR, block);
            Minecraft.getMinecraft().getItemColors().registerItemColorHandler(METAL_CASING_ITEM_COLOR, block);
        });

        GAMetaBlocks.GA_ORES.stream().distinct().forEach(block -> {
            Minecraft.getMinecraft().getBlockColors().registerBlockColorHandler(ORE_BLOCK_COLOR, block);
            Minecraft.getMinecraft().getItemColors().registerItemColorHandler(ORE_ITEM_COLOR, block);
        });

    }

    public static void registerOreDict() {
        for (Map.Entry<IngotMaterial, GAMetalCasing> entry : METAL_CASING.entrySet()) {
            IngotMaterial material = entry.getKey();
            GAMetalCasing block = entry.getValue();
            ItemStack itemStack = block.getItem(material);
            OreDictUnifier.registerOre(itemStack, OrePrefix.valueOf("gtMetalCasing"), material);
        }

        for (GABlockOre blockOre : GA_ORES) {
            DustMaterial mat = blockOre.material;
            for (StoneType stoneType : blockOre.STONE_TYPE.getAllowedValues()) {
                ItemStack normalStack = blockOre.getItem(blockOre.getDefaultState().withProperty(blockOre.STONE_TYPE, stoneType));
                OrePrefix orePrefix = stoneType.processingPrefix == OrePrefix.ore ? blockOre.getOrePrefix() :
                        OrePrefix.valueOf(blockOre.getOrePrefix().name() + stoneType.processingPrefix.name().substring(3));
                OreDictUnifier.registerOre(normalStack, orePrefix, mat);
            }
        }

        for (OpticalFiberSize opticalFiberSize : OpticalFiberSize.values()) {
            ItemStack itemStack = OPTICAL_FIBER.getItem(opticalFiberSize);
            GregicalityLogger.logger.info("cable creation {}", itemStack.getDisplayName());
            OreDictUnifier.registerOre(itemStack, opticalFiberSize.getOrePrefix().name());
        }

    }


    public static String statePropertiesToString(Map<IProperty<?>, Comparable<?>> properties) {
        StringBuilder stringbuilder = new StringBuilder();

        List<Map.Entry<IProperty<?>, Comparable<?>>> entries = properties.entrySet().stream().sorted(Comparator.comparing(c -> c.getKey().getName())).collect(Collectors.toList());

        for (Map.Entry<IProperty<?>, Comparable<?>> entry : entries) {
            if (stringbuilder.length() != 0) {
                stringbuilder.append(",");
            }

            IProperty<?> property = entry.getKey();
            stringbuilder.append(property.getName());
            stringbuilder.append("=");
            stringbuilder.append(getPropertyName(property, entry.getValue()));
        }

        if (stringbuilder.length() == 0) {
            stringbuilder.append("normal");
        }

        return stringbuilder.toString();
    }

    @SuppressWarnings("unchecked")
    private static <T extends Comparable<T>> String getPropertyName(IProperty<T> property, Comparable<?> value) {
        return property.getName((T) value);
    }


    private static void createMachineCasing() {
        for (Material material : Material.MATERIAL_REGISTRY) {
            if (material instanceof IngotMaterial && material.hasFlag(GENERATE_METAL_CASING)) {
                GAMetalCasing block = new GAMetalCasing(material);
                block.setRegistryName("gregtech:metal_casing_" + material.toString());
                METAL_CASING.put((IngotMaterial) material, block);
            }
        }
    }

    public static IBlockState getMetalCasingBlockState(Material material) {
        return METAL_CASING.get(material).getStateFromMaterial(material);
    }


}
