package org.ipforsmartobjects.apps.baking.data;

import android.support.annotation.NonNull;

import java.util.List;

public interface RepositoryContract {
    interface RecipesRepository {
        void loadRecipes(@NonNull LoadRecipesCallback callback);

        void loadRecipe(long recipeId, @NonNull LoadRecipeCallback callback); // get recipe stored in cache

        void clearCache();

        interface LoadRecipesCallback {
            void onRecipesLoaded(List<Recipe> recipes);

            void onLoadingFailed();
        }

        interface LoadRecipeCallback {
            void onRecipeLoaded(Recipe recipe);

            void onLoadingFailed();
        }
    }
}
