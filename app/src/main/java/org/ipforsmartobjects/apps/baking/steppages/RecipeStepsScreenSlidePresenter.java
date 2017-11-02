package org.ipforsmartobjects.apps.baking.steppages;

import android.support.annotation.NonNull;

import org.ipforsmartobjects.apps.baking.data.Recipe;
import org.ipforsmartobjects.apps.baking.data.RepositoryContract;
import org.ipforsmartobjects.apps.baking.data.Step;
import org.ipforsmartobjects.apps.baking.stepdetail.RecipeStepDetailContract;
import org.ipforsmartobjects.apps.baking.stepdetail.RecipeStepDetailFragment;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Listens to user actions from the UI ({@link RecipeStepDetailFragment}), retrieves the data and updates
 * the UI as required.
 */
public class RecipeStepsScreenSlidePresenter implements RecipeStepsScreenSlideContract.Presenter {

    private final RepositoryContract.RecipesRepository mRecipesRepository;

    private final RecipeStepsScreenSlideContract.View mRecipeStepDetailView;
    private long mStepId;
    private Step mStep;

    public RecipeStepsScreenSlidePresenter(@NonNull RecipeStepsScreenSlideContract.View movieDetailView,
                                           @NonNull RepositoryContract.RecipesRepository repository) {
        mRecipeStepDetailView = checkNotNull(movieDetailView, "movieDetailView cannot be null!");
        mRecipesRepository = repository;
    }

    @Override
    public void onPreviousClicked() {

    }

    @Override
    public void onNextClicked() {

    }

}
