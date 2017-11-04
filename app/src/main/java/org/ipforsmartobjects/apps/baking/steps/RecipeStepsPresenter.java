package org.ipforsmartobjects.apps.baking.steps;

import android.support.annotation.NonNull;

import org.ipforsmartobjects.apps.baking.data.Recipe;
import org.ipforsmartobjects.apps.baking.data.RepositoryContract;
import org.ipforsmartobjects.apps.baking.data.Step;


import static com.google.common.base.Preconditions.checkNotNull;

public class RecipeStepsPresenter implements RecipeStepsContract.Presenter {
    private final RepositoryContract.RecipesRepository mRecipesRepository;
    private final RecipeStepsContract.View mRecipeStepsView;
    private final int mRecipeId;

    public RecipeStepsPresenter(@NonNull RecipeStepsContract.View recipeStepsView,
                                @NonNull RepositoryContract.RecipesRepository repository,
                                int recipeId) {
        mRecipeStepsView = checkNotNull(recipeStepsView, "RecipesView cannot be null");
        mRecipesRepository = repository;
        mRecipeId = recipeId;
    }

    @Override
    public void loadSteps(boolean forceUpdate) {
        mRecipeStepsView.setProgressIndicator(true);

        if (forceUpdate) {
            mRecipesRepository.clearCache();
        }

        mRecipesRepository.loadRecipe(mRecipeId, new RepositoryContract.RecipesRepository.LoadRecipeCallback() {
            @Override
            public void onRecipeLoaded(Recipe recipe) {
                mRecipeStepsView.setProgressIndicator(false);
                mRecipeStepsView.showData(recipe.getName(), recipe.getSteps(), recipe.getIngredients());
            }

            @Override
            public void onLoadingFailed() {
                mRecipeStepsView.setProgressIndicator(false);
                mRecipeStepsView.showErrorView();
            }
        });

    }

    @Override
    public void openStepDetails(int recipeId, @NonNull Step requestedStep) {
        mRecipeStepsView.showStepDetailUi(recipeId, requestedStep.getId());
    }
}
