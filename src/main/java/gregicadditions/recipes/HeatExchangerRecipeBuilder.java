package gregicadditions.recipes;

import com.google.common.collect.ImmutableMap;
import gregicadditions.utils.GALog;
import gregtech.api.recipes.Recipe;
import gregtech.api.recipes.RecipeBuilder;
import gregtech.api.recipes.RecipeMap;
import gregtech.api.util.EnumValidationResult;
import gregtech.api.util.GTUtility;
import gregtech.api.util.ValidationResult;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class HeatExchangerRecipeBuilder extends RecipeBuilder<HeatExchangerRecipeBuilder> {

    private int inputTemperature;
    private int outputTemperature;
    private boolean isHeating;

    public HeatExchangerRecipeBuilder(Recipe recipe, RecipeMap<HeatExchangerRecipeBuilder> recipeMap) {
        super(recipe, recipeMap);
        this.inputTemperature = recipe.getIntegerProperty("input_temperature");
        this.outputTemperature = recipe.getProperty("output_temperature");
        this.isHeating = recipe.getProperty("is_heating");
    }

    public HeatExchangerRecipeBuilder() {

    }

    public HeatExchangerRecipeBuilder(RecipeBuilder<HeatExchangerRecipeBuilder> recipeBuilder) { super(recipeBuilder); }

    public HeatExchangerRecipeBuilder(RecipeBuilder<HeatExchangerRecipeBuilder> recipeBuilder, int inputTemperature, int outputTemperature, boolean isHeating) {
        super(recipeBuilder);
        this.inputTemperature = inputTemperature;
        this.outputTemperature = outputTemperature;
        this.isHeating = isHeating;
    }

    @Override
    public HeatExchangerRecipeBuilder copy() {
        return new HeatExchangerRecipeBuilder(this, this.inputTemperature, this.outputTemperature, this.isHeating);
    }

    @Override
    public boolean applyProperty(String key, Object value) {
        switch (key) {
            case "input_temperature":
                this.inputTemperature(((Number) value).intValue());
                return true;
            case "output_temperature":
                this.outputTemperature(((Number) value).intValue());
                return true;
            case "is_heating":
                this.isHeating((Boolean) value);
        }
        return false;
    }

    public HeatExchangerRecipeBuilder inputTemperature(int inputTemperature) {
        this.inputTemperature = inputTemperature;
        return this;
    }

    public HeatExchangerRecipeBuilder outputTemperature(int outputTemperature) {
        this.outputTemperature = outputTemperature;
        return this;
    }

    public HeatExchangerRecipeBuilder isHeating(boolean isHeating) {
        this.isHeating = isHeating;
        return this;
    }

    @Override
    public ValidationResult<Recipe> build() {
        return ValidationResult.newResult(finalizeAndValidate(),
                new Recipe(inputs, outputs, chancedOutputs, fluidInputs, fluidOutputs,
                        ImmutableMap.of("input_temperature", this.inputTemperature, "output_temperature", this.outputTemperature, "is_heating", this.isHeating),
                        duration, EUt, hidden));
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

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .appendSuper(super.toString())
                .append("input_temperature", inputTemperature)
                .append("recipe_temperature", outputTemperature)
                .append("is_heating", isHeating)
                .toString();
    }
}
