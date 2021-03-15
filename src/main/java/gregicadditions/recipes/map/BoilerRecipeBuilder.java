package gregicadditions.recipes.map;

import com.google.common.collect.ImmutableMap;
import gregicadditions.utils.GALog;
import gregtech.api.recipes.Recipe;
import gregtech.api.recipes.RecipeBuilder;
import gregtech.api.recipes.RecipeMap;
import gregtech.api.util.EnumValidationResult;
import gregtech.api.util.GTUtility;
import gregtech.api.util.ValidationResult;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class BoilerRecipeBuilder extends RecipeBuilder<BoilerRecipeBuilder> {

    private int minTemperature;

    public BoilerRecipeBuilder() {
    }

    public BoilerRecipeBuilder(Recipe recipe, RecipeMap<BoilerRecipeBuilder> recipeMap) {
        super(recipe, recipeMap);
    }

    public BoilerRecipeBuilder(RecipeBuilder<BoilerRecipeBuilder> recipeBuilder) {
        super(recipeBuilder);
    }

    public BoilerRecipeBuilder minTemperature(int minTemperature) {
        if (minTemperature <= 0) {
            GALog.logger.error("Minimum Temperature cannot be less than or equal to 0", new IllegalArgumentException());
            recipeStatus = EnumValidationResult.INVALID;
        }
        this.minTemperature = minTemperature;
        return this;
    }

    @Override
    public BoilerRecipeBuilder copy() {
        return new BoilerRecipeBuilder(this);
    }

    public ValidationResult<Recipe> build() {
        return ValidationResult.newResult(finalizeAndValidate(),
                new Recipe(inputs, outputs, chancedOutputs, fluidInputs, fluidOutputs,
                        ImmutableMap.of("min_temperature", minTemperature),
                        duration, EUt, hidden));
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .appendSuper(super.toString())
                .append("min_temperature", minTemperature)
                .toString();
    }

    @Override
    protected EnumValidationResult validate() {
        if (this.recipeMap == null) {
            GALog.logger.error("RecipeMap cannot be null", new IllegalArgumentException());
            this.recipeStatus = EnumValidationResult.INVALID;
        }

        if (!GTUtility.isBetweenInclusive(this.recipeMap.getMinInputs(), this.recipeMap.getMaxInputs(), this.inputs.size())) {
            GALog.logger.error("Invalid amount of recipe inputs. Actual: {}. Should be between {} and {} inclusive.", this.inputs.size(), this.recipeMap.getMinInputs(), this.recipeMap.getMaxInputs());
            GALog.logger.error("Stacktrace:", new IllegalArgumentException());
            this.recipeStatus = EnumValidationResult.INVALID;
        }

        if (!GTUtility.isBetweenInclusive(this.recipeMap.getMinOutputs(), this.recipeMap.getMaxOutputs(), this.outputs.size() + this.chancedOutputs.size())) {
            GALog.logger.error("Invalid amount of recipe outputs. Actual: {}. Should be between {} and {} inclusive.", this.outputs.size() + this.chancedOutputs.size(), this.recipeMap.getMinOutputs(), this.recipeMap.getMaxOutputs());
            GALog.logger.error("Stacktrace:", new IllegalArgumentException());
            this.recipeStatus = EnumValidationResult.INVALID;
        }

        if (!GTUtility.isBetweenInclusive(this.recipeMap.getMinFluidInputs(), this.recipeMap.getMaxFluidInputs(), this.fluidInputs.size())) {
            GALog.logger.error("Invalid amount of recipe fluid inputs. Actual: {}. Should be between {} and {} inclusive.", this.fluidInputs.size(), this.recipeMap.getMinFluidInputs(), this.recipeMap.getMaxFluidInputs());
            GALog.logger.error("Stacktrace:", new IllegalArgumentException());
            this.recipeStatus = EnumValidationResult.INVALID;
        }

        if (!GTUtility.isBetweenInclusive(this.recipeMap.getMinFluidOutputs(), this.recipeMap.getMaxFluidOutputs(), this.fluidOutputs.size())) {
            GALog.logger.error("Invalid amount of recipe fluid outputs. Actual: {}. Should be between {} and {} inclusive.", this.fluidOutputs.size(), this.recipeMap.getMinFluidOutputs(), this.recipeMap.getMaxFluidOutputs());
            GALog.logger.error("Stacktrace:", new IllegalArgumentException());
            this.recipeStatus = EnumValidationResult.INVALID;
        }

        if (this.duration <= 0) {
            GALog.logger.error("Duration cannot be less or equal to 0", new IllegalArgumentException());
            this.recipeStatus = EnumValidationResult.INVALID;
        }

        if (this.recipeStatus == EnumValidationResult.INVALID) {
            GALog.logger.error("Invalid recipe, read the errors above: {}", this);
        }

        return this.recipeStatus;
    }
}
