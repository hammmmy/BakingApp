package org.ipforsmartobjects.apps.baking.steppages;

import android.support.annotation.NonNull;

import org.ipforsmartobjects.apps.baking.data.RepositoryContract;
import org.ipforsmartobjects.apps.baking.stepdetail.RecipeStepDetailFragment;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Listens to user actions from the UI ({@link RecipeStepDetailFragment}), retrieves the data and updates
 * the UI as required.
 */
public class RecipeStepsScreenSlidePresenter implements RecipeStepsScreenSlideContract.Presenter {

    private final RecipeStepsScreenSlideContract.View mRecipeStepsScreenSlideView;

    public RecipeStepsScreenSlidePresenter(@NonNull RecipeStepsScreenSlideContract.View recipeStepsScreenSlideView,
                                           @NonNull RepositoryContract.RecipesRepository repository) {
        mRecipeStepsScreenSlideView = checkNotNull(recipeStepsScreenSlideView, "recipeStepsScreenSlideView cannot be null!");
    }

    @Override
    public void onPreviousClicked() {
        mRecipeStepsScreenSlideView.showPrevious();
    }

    @Override
    public void onNextClicked() {
        mRecipeStepsScreenSlideView.showNext();
    }

}
