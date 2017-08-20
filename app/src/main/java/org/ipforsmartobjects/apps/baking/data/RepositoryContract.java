package org.ipforsmartobjects.apps.baking.data;

import android.support.annotation.NonNull;

import java.util.List;

public interface RepositoryContract {
    interface RecipesRepository {
        void loadRecipes(@NonNull LoadRecipesCallback callback);

        void getRecipe(long recipeId, @NonNull GetRecipeCallback callback); // get recipe stored in cache

        void clearCache();

        interface LoadRecipesCallback {
            void onRecipeLoaded(List<Recipe> recipes);

            void onLoadingFailed();
        }

        interface GetRecipeCallback {
            void onRecipeLoaded(Recipe recipe);

            void onLoadingFailed();
        }
    }
}
