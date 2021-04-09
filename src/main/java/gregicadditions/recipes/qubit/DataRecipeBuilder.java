package gregicadditions.recipes.qubit;

import com.google.common.collect.ImmutableMap;
import gregicadditions.utils.GALog;
import gregtech.api.recipes.CountableIngredient;
import gregtech.api.recipes.Recipe;
import gregtech.api.recipes.RecipeBuilder;
import gregtech.api.recipes.RecipeMap;
import gregtech.api.util.EnumValidationResult;
import gregtech.api.util.GTLog;
import gregtech.api.util.ValidationResult;
import net.minecraft.item.ItemStack;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * @see Recipe
 */

public class DataRecipeBuilder extends RecipeBuilder<DataRecipeBuilder> {

    private int qubit;
    protected ItemStack data;

    public DataRecipeBuilder() {
    }

    public DataRecipeBuilder(Recipe recipe, RecipeMap<DataRecipeBuilder> recipeMap) {
        super(recipe, recipeMap);
        this.qubit = recipe.getIntegerProperty("qubitConsume");
        this.data = ItemStack.EMPTY;
    }

    public DataRecipeBuilder(RecipeBuilder<DataRecipeBuilder> recipeBuilder) {
        super(recipeBuilder);
    }

    @Override
    public DataRecipeBuilder copy() {
        return new DataRecipeBuilder(this);
    }

    @Override
    public boolean applyProperty(String key, Object value) {
        if (key.equals("qubit")) {
            this.qubit(((Number) value).intValue());
            return true;
        }
        return false;
    }

    @Override
    public boolean applyProperty(String key, ItemStack data) {
        if (key.equals("data")) {
            this.data = data;
            return true;
        }
        return false;
    }


    public DataRecipeBuilder qubit(int qubit) {
        if (qubit <= 0) {
            GALog.logger.error("qubit cannot be less than or equal to 0", new IllegalArgumentException());
            recipeStatus = EnumValidationResult.INVALID;
        }
        this.qubit = qubit;
        return this;
    }

    public DataRecipeBuilder data(ItemStack data) {
        this.data = data;
        return this;
    }

    @Override
    public void buildAndRegister() {
        if (this.inputs.size() > 16) {
            GALog.logger.error("inputs cannot be greater than 16", new IllegalArgumentException());
            recipeStatus = EnumValidationResult.INVALID;
        }

        if (data != null) {
            recipeMap.addRecipe(this.notConsumable(data).build());
        } else {
            recipeMap.addRecipe(this.build());
        }
    }

    public ValidationResult<Recipe> build() {
        return ValidationResult.newResult(finalizeAndValidate(),
                new Recipe(inputs, outputs, chancedOutputs, fluidInputs, fluidOutputs,
                        ImmutableMap.of("qubitConsume", qubit),
                        duration, EUt, hidden));
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .appendSuper(super.toString())
                .append("qubitConsume", qubit)
                .append("data", data.getDisplayName())
                .toString();
    }


}
