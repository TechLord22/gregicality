package gregicadditions.machines.multi.override;

import gregicadditions.GAConfig;
import gregicadditions.GAUtility;
import gregicadditions.GAValues;
import gregicadditions.capabilities.GregicAdditionsCapabilities;
import gregicadditions.capabilities.impl.GAMultiblockRecipeLogic;
import gregicadditions.capabilities.impl.GARecipeMapMultiblockController;
import gregicadditions.item.GAHeatingCoil;
import gregicadditions.item.GAMetaBlocks;
import gregtech.api.capability.IEnergyContainer;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.metatileentity.MetaTileEntityHolder;
import gregtech.api.metatileentity.multiblock.IMultiblockPart;
import gregtech.api.metatileentity.multiblock.MultiblockAbility;
import gregtech.api.metatileentity.multiblock.RecipeMapMultiblockController;
import gregtech.api.multiblock.BlockPattern;
import gregtech.api.multiblock.BlockWorldState;
import gregtech.api.multiblock.FactoryBlockPattern;
import gregtech.api.multiblock.PatternMatchContext;
import gregtech.api.recipes.Recipe;
import gregtech.api.recipes.RecipeMaps;
import gregtech.api.render.ICubeRenderer;
import gregtech.api.render.OrientedOverlayRenderer;
import gregtech.api.render.Textures;
import gregtech.api.unification.material.Materials;
import gregtech.common.blocks.BlockMetalCasing;
import gregtech.common.blocks.BlockWireCoil;
import gregtech.common.blocks.MetaBlocks;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.event.HoverEvent;

import javax.annotation.Nonnull;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

import static gregtech.api.render.Textures.HEAT_PROOF_CASING;
import static gregtech.api.unification.material.Materials.Invar;

public class MetaTileEntityElectricBlastFurnace extends GARecipeMapMultiblockController {
	public MetaTileEntityElectricBlastFurnace(ResourceLocation metaTileEntityId) {
		super(metaTileEntityId, RecipeMaps.BLAST_RECIPES, true, true);
		this.recipeMapWorkable = new GAMultiblockRecipeLogic(this);
	}

	public MetaTileEntity createMetaTileEntity(MetaTileEntityHolder holder) {
		return new MetaTileEntityElectricBlastFurnace(this.metaTileEntityId);
	}

	private int blastFurnaceTemperature;

	private static final MultiblockAbility<?>[] ALLOWED_ABILITIES = {
			MultiblockAbility.IMPORT_ITEMS, MultiblockAbility.IMPORT_FLUIDS,
			MultiblockAbility.EXPORT_ITEMS, MultiblockAbility.EXPORT_FLUIDS,
			MultiblockAbility.INPUT_ENERGY, GregicAdditionsCapabilities.MAINTENANCE_CAPABILITY
	};

	@Override
	public ICubeRenderer getBaseTexture(IMultiblockPart sourcePart) {
		return HEAT_PROOF_CASING;
	}

	@Override
	protected BlockPattern createStructurePattern() {
		return FactoryBlockPattern.start()
				.aisle("XXX", "CCC", "CCC", "XXX")
				.aisle("XXX", "C#C", "C#C", "XMX")
				.aisle("XSX", "CCC", "CCC", "XXX")
				.setAmountAtLeast('L', 8)
				.where('S', selfPredicate())
				.where('L', statePredicate(getCasingState()))
				.where('X', statePredicate(getCasingState()).or(abilityPartPredicate(ALLOWED_ABILITIES)))
				.where('C', heatingCoilPredicate().or(heatingCoilPredicate2()))
                .where('M', abilityPartPredicate(GregicAdditionsCapabilities.MUFFLER_HATCH))
				.where('#', isAirPredicate())
				.build();
	}

	public static Predicate<BlockWorldState> heatingCoilPredicate() {
		return blockWorldState -> {
			IBlockState blockState = blockWorldState.getBlockState();
			if (!(blockState.getBlock() instanceof BlockWireCoil))
				return false;
			BlockWireCoil blockWireCoil = (BlockWireCoil) blockState.getBlock();
			BlockWireCoil.CoilType coilType = blockWireCoil.getState(blockState);
			if (Arrays.asList(GAConfig.multis.heatingCoils.gtceHeatingCoilsBlacklist).contains(coilType.getName()))
				return false;
			int blastFurnaceTemperature = coilType.getCoilTemperature();
			int currentTemperature = blockWorldState.getMatchContext().getOrPut("blastFurnaceTemperature", blastFurnaceTemperature);
			return currentTemperature == blastFurnaceTemperature;
		};
	}

	public static Predicate<BlockWorldState> heatingCoilPredicate2() {
		return blockWorldState -> {
			IBlockState blockState = blockWorldState.getBlockState();
			if (!(blockState.getBlock() instanceof GAHeatingCoil))
				return false;
			GAHeatingCoil blockWireCoil = (GAHeatingCoil) blockState.getBlock();
			GAHeatingCoil.CoilType coilType = blockWireCoil.getState(blockState);
			if (Arrays.asList(GAConfig.multis.heatingCoils.gregicalityheatingCoilsBlacklist).contains(coilType.getName()))
				return false;
			int blastFurnaceTemperature = coilType.getCoilTemperature();
			int currentTemperature = blockWorldState.getMatchContext().getOrPut("blastFurnaceTemperature", blastFurnaceTemperature);
			return currentTemperature == blastFurnaceTemperature;
		};
	}

	@Override
	protected void addDisplayText(List<ITextComponent> textList) {
		super.addDisplayText(textList);
		if (isStructureFormed()) {
			textList.add(new TextComponentTranslation("gregtech.multiblock.blast_furnace.max_temperature", blastFurnaceTemperature));
		}
	}

	@Override
	protected void formStructure(PatternMatchContext context) {
		super.formStructure(context);
		blastFurnaceTemperature = context.getOrDefault("blastFurnaceTemperature", 0);
	}

	@Override
	public void invalidateStructure() {
		super.invalidateStructure();
		this.blastFurnaceTemperature = 0;
	}

	@Override
	public boolean checkRecipe(Recipe recipe, boolean consumeIfSuccess) {
		int recipeRequiredTemp = recipe.getIntegerProperty("blast_furnace_temperature");
		return this.blastFurnaceTemperature >= recipeRequiredTemp;
	}

	public IBlockState getCasingState() {
		return MetaBlocks.METAL_CASING.getState(BlockMetalCasing.MetalCasingType.INVAR_HEATPROOF);
	}
}
