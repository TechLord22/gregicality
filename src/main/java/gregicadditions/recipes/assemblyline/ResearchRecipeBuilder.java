package gregicadditions.recipes.assemblyline;

import com.google.common.collect.ImmutableMap;
import gregicadditions.machines.multi.impl.ResearchInputItem;
import gregicadditions.utils.GALog;
import gregtech.api.recipes.Recipe;
import gregtech.api.recipes.RecipeBuilder;
import gregtech.api.recipes.RecipeMap;
import gregtech.api.util.EnumValidationResult;
import gregtech.api.util.ValidationResult;
import net.minecraft.item.ItemStack;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.ArrayList;
import java.util.List;

/**
 * @see Recipe
 */

public class ResearchRecipeBuilder extends RecipeBuilder<ResearchRecipeBuilder> {

    private int qubit;
    private final List<ResearchInputItem> researchInputItems = new ArrayList<>();

    public ResearchRecipeBuilder() {
    }

    public ResearchRecipeBuilder(Recipe recipe, RecipeMap<ResearchRecipeBuilder> recipeMap) {
        super(recipe, recipeMap);
        this.qubit = recipe.getIntegerProperty("qubitConsume");
    }

    public ResearchRecipeBuilder(RecipeBuilder<ResearchRecipeBuilder> recipeBuilder) {
        super(recipeBuilder);
    }

    @Override
    public ResearchRecipeBuilder copy() {
        return new ResearchRecipeBuilder(this);
    }

    @Override
    public boolean applyProperty(String key, Object value) {
        if (key.equals("qubit")) {
            this.qubit(((Number) value).intValue());
            return true;
        }
        return false;
    }

    public ResearchRecipeBuilder inputs(ResearchInputItem item) {
        if (item.getBaseAmount() <= 0) {
            GALog.logger.error("Item input amount for ResearchItem must be greater than zero", new IllegalArgumentException());
            recipeStatus = EnumValidationResult.INVALID;
        }
        this.researchInputItems.add(item);
        return this;
    }

    public ResearchRecipeBuilder qubit(int qubit) {
        if (qubit <= 0) {
            GALog.logger.error("qubit cannot be less than or equal to 0", new IllegalArgumentException());
            recipeStatus = EnumValidationResult.INVALID;
        }
        this.qubit = qubit;
        return this;
    }

    public ValidationResult<Recipe> build() {
        return ValidationResult.newResult(finalizeAndValidate(),
                new AssemblyLineRecipe(inputs, outputs, chancedOutputs, fluidInputs, fluidOutputs,
                        ImmutableMap.of("qubitConsume", qubit),
                        duration, EUt, hidden, researchInputItems));
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .appendSuper(super.toString())
                .append("qubitConsume", qubit)
                .append("research", researchInputItems.get(0))
                .toString();
    }


}
