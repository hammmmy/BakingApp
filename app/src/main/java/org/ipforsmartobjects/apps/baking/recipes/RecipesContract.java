package org.ipforsmartobjects.apps.baking.recipes;

import android.support.annotation.NonNull;

import org.ipforsmartobjects.apps.baking.data.Recipe;

import java.util.List;

/**
 * This specifies the contract between the view, presenter, and the model.
 * reference : https://codelabs.developers.google.com/codelabs/android-testing/index.html
 */
public interface RecipesContract {

    interface View {
        void setProgressIndicator(boolean active);

        void showRecipes(List<Recipe> recipes);

        void showErrorView();

        void showEmptyView();

        void showRecipeSteps(long recipeId);
    }

    interface Presenter {
        void loadRecipes(boolean forceUpdate);

        void openRecipeSteps(@NonNull Recipe requestedRecipe);
    }

}
