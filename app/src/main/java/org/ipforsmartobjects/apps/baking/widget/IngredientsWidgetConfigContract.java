package org.ipforsmartobjects.apps.baking.widget;

import android.support.annotation.NonNull;

import org.ipforsmartobjects.apps.baking.data.Ingredient;
import org.ipforsmartobjects.apps.baking.data.Recipe;

import java.util.List;

/**
 * This specifies the contract between the view, presenter, and the model.
 * reference : https://codelabs.developers.google.com/codelabs/android-testing/index.html
 */
public interface IngredientsWidgetConfigContract {

    interface View {
        void disableSubmitButton(boolean disable);
        void showRecipes(List<Recipe> recipes);

        void saveWidgetDetails(String recipeName, String ingredients);
    }

    interface Presenter {
        void loadRecipes(boolean forceUpdate);

        void saveWidgetDetails(String recipeName, @NonNull List<Ingredient> ingredients);
    }

}
