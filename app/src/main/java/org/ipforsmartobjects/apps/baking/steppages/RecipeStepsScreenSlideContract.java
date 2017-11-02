package org.ipforsmartobjects.apps.baking.steppages;

import org.ipforsmartobjects.apps.baking.data.Step;

/**
 * This specifies the contract between the view, presenter, and the model.
 * reference : https://codelabs.developers.google.com/codelabs/android-testing/index.html
 */
public interface RecipeStepsScreenSlideContract {

    interface View {
        void showPrevious();
        void showNext();
    }

    interface Presenter {
        void onPreviousClicked();
        void onNextClicked();
    }
}
