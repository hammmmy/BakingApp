package org.ipforsmartobjects.apps.baking.steppages;

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
