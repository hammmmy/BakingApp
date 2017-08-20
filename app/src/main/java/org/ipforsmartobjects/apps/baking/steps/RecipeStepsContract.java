package org.ipforsmartobjects.apps.baking.steps;

import android.support.annotation.NonNull;

import org.ipforsmartobjects.apps.baking.data.Step;

import java.util.List;

/**
 * This specifies the contract between the view, presenter, and the model.
 * reference : https://codelabs.developers.google.com/codelabs/android-testing/index.html
 */
public interface RecipeStepsContract {

    interface View {
        void setProgressIndicator(boolean active);

        void showRecipeSteps(List<Step> steps);

        void showErrorView();

        void showEmptyView();

        void showStepDetailUi(long stepId);
    }

    interface Presenter {
        void loadSteps(boolean forceUpdate);

        void openStepDetails(@NonNull Step requestedStep);

    }

}
