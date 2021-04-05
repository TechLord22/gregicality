package gregicadditions.capabilities.impl;

import gregicadditions.GAValues;
import gregtech.api.capability.IMultipleTankHandler;
import gregtech.api.capability.impl.AbstractRecipeLogic;
import gregtech.api.recipes.Recipe;
import gregtech.api.recipes.RecipeMap;
import gregtech.api.util.GTUtility;
import net.minecraftforge.items.IItemHandlerModifiable;

public class NoEUMultiblockRecipeLogic extends AbstractRecipeLogic {

    private int chancedTier = GAValues.EV;

    public NoEUMultiblockRecipeLogic(RecipeMapNoEUMultiblockController tileEntity, RecipeMap<?> recipeMap) {
        super(tileEntity, tileEntity.recipeMap);
        allowOverclocking = false;
    }

    public NoEUMultiblockRecipeLogic(RecipeMapNoEUMultiblockController tileEntity, RecipeMap<?> recipeMap,  int chancedTier) {
        super(tileEntity, tileEntity.recipeMap);
        allowOverclocking = false;
        this.chancedTier = chancedTier;
    }

    @Override
    public void update() {
        RecipeMapNoEUMultiblockController controller = (RecipeMapNoEUMultiblockController) metaTileEntity;
        if (isActive && !controller.isStructureFormed()) {
            progressTime = 0;
            wasActiveAndNeedsUpdate = true;
        }

        super.update();
    }

    @Override
    protected void updateRecipeProgress() {
        //as recipe starts with progress on 1 this has to be > only not => to compensate for it
        if (++progressTime > maxProgressTime) {
            completeRecipe();
        }
    }

    public void updateWorkable() {
        super.update();
    }

    @Override
    protected long getEnergyStored() {
        return Long.MAX_VALUE;
    }

    @Override
    protected long getEnergyCapacity() {
        return Long.MAX_VALUE;
    }

    @Override
    protected boolean drawEnergy(int i) {
        return false;
    }

    @Override
    protected long getMaxVoltage() {
        return Long.MAX_VALUE;
    }

    @Override
    protected IItemHandlerModifiable getInputInventory() {
        RecipeMapNoEUMultiblockController controller = (RecipeMapNoEUMultiblockController) metaTileEntity;
        return controller.getInputInventory();
    }

    @Override
    protected IItemHandlerModifiable getOutputInventory() {
        RecipeMapNoEUMultiblockController controller = (RecipeMapNoEUMultiblockController) metaTileEntity;
        return controller.getOutputInventory();
    }

    @Override
    protected IMultipleTankHandler getInputTank() {
        RecipeMapNoEUMultiblockController controller = (RecipeMapNoEUMultiblockController) metaTileEntity;
        return controller.getInputFluidInventory();
    }

    @Override
    protected IMultipleTankHandler getOutputTank() {
        RecipeMapNoEUMultiblockController controller = (RecipeMapNoEUMultiblockController) metaTileEntity;
        return controller.getOutputFluidInventory();
    }

    @Override
    protected boolean setupAndConsumeRecipeInputs(Recipe recipe) {
        RecipeMapNoEUMultiblockController controller = (RecipeMapNoEUMultiblockController) metaTileEntity;
        if (controller.checkRecipe(recipe, false) &&
                super.setupAndConsumeRecipeInputs(recipe)) {
            controller.checkRecipe(recipe, true);
            return true;
        } else return false;
    }

    @Override
    protected void setupRecipe(Recipe recipe) {
        this.progressTime = 1;
        setMaxProgress(recipe.getDuration());
        this.recipeEUt = 0;
        this.fluidOutputs = GTUtility.copyFluidList(recipe.getFluidOutputs());
        this.itemOutputs = GTUtility.copyStackList(recipe.getResultItemOutputs(getOutputInventory().getSlots(), random, chancedTier));
        if (this.wasActiveAndNeedsUpdate) {
            this.wasActiveAndNeedsUpdate = false;
        } else {
            this.setActive(true);
        }
    }

    @Override
    public boolean isHasNotEnoughEnergy() {
        return false;
    }

    @Override
    public boolean isAllowOverclocking() {
        return false;
    }

}
