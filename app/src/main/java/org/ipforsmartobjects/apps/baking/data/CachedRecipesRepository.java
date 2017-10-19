package org.ipforsmartobjects.apps.baking.data;

import android.support.annotation.NonNull;
import android.support.annotation.VisibleForTesting;
import android.support.v4.util.LongSparseArray;

import com.google.common.collect.ImmutableList;

import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Concrete implementation to load movies
 */
public class CachedRecipesRepository implements RepositoryContract.RecipesRepository {

    private final RecipesServiceApi mRecipesServiceApi;
    @VisibleForTesting
    private final
    LongSparseArray<Recipe> mRecipeCache = new LongSparseArray<>();
    @VisibleForTesting
    private
    List<Recipe> mCachedRecipes;


    public CachedRecipesRepository(@NonNull RecipesServiceApi recipesServiceApi) {
        mRecipesServiceApi = checkNotNull(recipesServiceApi);
    }

    @Override
    public void loadRecipes(@NonNull final LoadRecipesCallback callback) {
        checkNotNull(callback);
        // Load from API only if needed.

                if (mCachedRecipes == null) {
                    getRecipes(callback);
                } else {
                    callback.onRecipesLoaded(mCachedRecipes);
                }


    }

    private void getRecipes(@NonNull final LoadRecipesCallback callback) {
        mRecipesServiceApi.getRecipes(new RecipesServiceApi.MoviesServiceCallback<List<Recipe>>() {
            @Override
            public void onLoaded(List<Recipe> recipes) {
                    mCachedRecipes = ImmutableList.copyOf(recipes);
                    callback.onRecipesLoaded(mCachedRecipes);
                    for (Recipe popular : mCachedRecipes) {
                        mRecipeCache.put(popular.getId(), popular);
                    }
            }

            @Override
            public void onLoadingFailed() {
                callback.onLoadingFailed();
            }
        });
    }

    private void getRecipe(final long recipeId, @NonNull final LoadRecipeCallback callback) {
        mRecipesServiceApi.getRecipes(new RecipesServiceApi.MoviesServiceCallback<List<Recipe>>() {
            @Override
            public void onLoaded(List<Recipe> recipes) {
                mCachedRecipes = ImmutableList.copyOf(recipes);

                for (Recipe popular : mCachedRecipes) {
                    mRecipeCache.put(popular.getId(), popular);
                }
                callback.onRecipeLoaded(mRecipeCache.get(recipeId));

            }

            @Override
            public void onLoadingFailed() {
                callback.onLoadingFailed();
            }
        });
    }


    @Override
    public void loadRecipe(final long recipeId, @NonNull final LoadRecipeCallback callback) {
        checkNotNull(recipeId);
        checkNotNull(callback);

        if (mRecipeCache.get(recipeId) != null) {
            // show immediately available data first
            callback.onRecipeLoaded(mRecipeCache.get(recipeId));
        } else {
            getRecipe(recipeId, callback);
        }
    }

    @Override
    public void clearCache() {
        mCachedRecipes = null;
        mRecipeCache.clear();

    }
}
