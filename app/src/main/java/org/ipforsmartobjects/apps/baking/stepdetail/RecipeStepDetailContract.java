package org.ipforsmartobjects.apps.baking.stepdetail;

import org.ipforsmartobjects.apps.baking.data.Step;

/**
 * This specifies the contract between the view, presenter, and the model.
 * reference : https://codelabs.developers.google.com/codelabs/android-testing/index.html
 */
public interface RecipeStepDetailContract {

    interface View {

        void setProgressIndicator(boolean active);

        void showEmptyView();

        void showStep(Step step);
    }

    interface Presenter {
        void openStep(int recipeId, int stepId);
    }
}
