package org.ipforsmartobjects.apps.baking.recipes;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;

import org.ipforsmartobjects.apps.baking.data.Recipe;
import org.ipforsmartobjects.apps.baking.data.RepositoryContract;

import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

public class RecipesPresenter implements RecipesContract.Presenter {
    private final RepositoryContract.RecipesRepository mRecipesRepository;
    private final RecipesContract.View mRecipesView;

    public RecipesPresenter(@NonNull RecipesContract.View RecipesView,
                            @NonNull RepositoryContract.RecipesRepository repository) {
        mRecipesView = checkNotNull(RecipesView, "RecipesView cannot be null");
        mRecipesRepository = repository;
    }

    @Override
    public void loadRecipes(boolean forceUpdate) {
        mRecipesView.setProgressIndicator(true);

            if (forceUpdate) {
                mRecipesRepository.clearCache();
            }

            mRecipesRepository.loadRecipes(new RepositoryContract.RecipesRepository.LoadRecipesCallback() {
                @Override
                public void onRecipesLoaded(List<Recipe> Recipes) {
                    mRecipesView.setProgressIndicator(false);
                    mRecipesView.showRecipes(Recipes);
                }

                @Override
                public void onLoadingFailed() {
                    mRecipesView.setProgressIndicator(false);
                    mRecipesView.showErrorView();
                }
            });
        }

    @Override
    public void openRecipeSteps(@NonNull Recipe requestedRecipe) {
        checkNotNull(requestedRecipe, "requestedRecipe cannot be null!");
        mRecipesView.showRecipeSteps(requestedRecipe.getId());
    }
    
}
