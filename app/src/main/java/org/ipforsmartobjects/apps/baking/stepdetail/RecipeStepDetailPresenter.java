package org.ipforsmartobjects.apps.baking.stepdetail;

import android.support.annotation.NonNull;

import org.ipforsmartobjects.apps.baking.data.Recipe;
import org.ipforsmartobjects.apps.baking.data.RepositoryContract;
import org.ipforsmartobjects.apps.baking.data.Step;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Listens to user actions from the UI ({@link RecipeStepDetailFragment}), retrieves the data and updates
 * the UI as required.
 */
public class RecipeStepDetailPresenter implements RecipeStepDetailContract.Presenter {

    private final RepositoryContract.RecipesRepository mRecipesRepository;

    private final RecipeStepDetailContract.View mRecipeStepDetailView;
    private Step mStep;

    public RecipeStepDetailPresenter(@NonNull RecipeStepDetailContract.View movieDetailView,
                                     @NonNull RepositoryContract.RecipesRepository repository) {
        mRecipeStepDetailView = checkNotNull(movieDetailView, "movieDetailView cannot be null!");
        mRecipesRepository = repository;
    }

    @Override
    public void openStep(int recipeId, final int stepId) {
        if (stepId == -1) {
            mRecipeStepDetailView.showEmptyView();
            return;
        }

        mRecipeStepDetailView.setProgressIndicator(true);
        mRecipesRepository.loadRecipe(recipeId, new RepositoryContract.RecipesRepository.LoadRecipeCallback() {
            @Override
            public void onRecipeLoaded(Recipe recipe) {
                mRecipeStepDetailView.setProgressIndicator(false);
                if (null == recipe) {
                    mRecipeStepDetailView.showEmptyView();
                } else {
                    mStep = recipe.getSteps().get(stepId);
                    mRecipeStepDetailView.showStep(mStep);
                }

            }

            @Override
            public void onLoadingFailed() {
                mRecipeStepDetailView.setProgressIndicator(false);
                mRecipeStepDetailView.showEmptyView();

            }
        });

    }


}
