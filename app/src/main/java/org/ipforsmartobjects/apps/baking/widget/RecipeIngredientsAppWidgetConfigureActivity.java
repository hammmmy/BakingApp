package org.ipforsmartobjects.apps.baking.widget;

import android.app.Activity;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.ipforsmartobjects.apps.baking.Injection;
import org.ipforsmartobjects.apps.baking.R;
import org.ipforsmartobjects.apps.baking.data.Ingredient;
import org.ipforsmartobjects.apps.baking.data.Recipe;
import org.ipforsmartobjects.apps.baking.databinding.RecipeIngredientsAppWidgetConfigureBinding;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * The configuration screen for the {@link RecipeIngredientsAppWidget RecipeIngredientsAppWidget} AppWidget.
 */
public class RecipeIngredientsAppWidgetConfigureActivity extends Activity implements IngredientsWidgetConfigContract.View {

    private static final String PREFS_NAME = "org.ipforsmartobjects.apps.baking.widget.RecipeIngredientsAppWidget";
    private static final String PREF_PREFIX_KEY = "appwidget_";
    private static final String INGREDIENTS_PREF_PREFIX_KEY = "ingredients_appwidget_";
    private int mAppWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID;
    private Spinner mRecipeNamesSpinner;
    private final HashMap<String, List<Ingredient>> mIngredientsMap = new HashMap<>();
    private IngredientsWidgetConfigPresenter mActionsListener;
    private final View.OnClickListener mOnClickListener = new View.OnClickListener() {
        public void onClick(View v) {
            String selectedRecipeName = mRecipeNamesSpinner.getSelectedItem().toString();
            mActionsListener.saveWidgetDetails(selectedRecipeName, mIngredientsMap.get(selectedRecipeName));
        }
    };
    private RecipeIngredientsAppWidgetConfigureBinding mBinding;



    public RecipeIngredientsAppWidgetConfigureActivity() {
        super();
    }

    // Write the prefix to the SharedPreferences object for this widget
    private static void saveTitlePref(Context context, int appWidgetId, String text) {
        SharedPreferences.Editor prefs = context.getSharedPreferences(PREFS_NAME, 0).edit();
        prefs.putString(PREF_PREFIX_KEY + appWidgetId, text);
        prefs.apply();
    }

    // Write the prefix to the SharedPreferences object for this widget
    private static void saveIngredientsPref(Context context, int appWidgetId, String text) {
        SharedPreferences.Editor prefs = context.getSharedPreferences(PREFS_NAME, 0).edit();
        prefs.putString(INGREDIENTS_PREF_PREFIX_KEY + appWidgetId, text);
        prefs.apply();
    }

    // Read the prefix from the SharedPreferences object for this widget.
    // If there is no preference saved, get the default from a resource
    static String loadTitlePref(Context context, int appWidgetId) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, 0);
        String titleValue = prefs.getString(PREF_PREFIX_KEY + appWidgetId, null);
        if (titleValue != null) {
            return titleValue;
        } else {
            return context.getString(R.string.appwidget_text);
        }
    }

    private static ArrayList<Ingredient> fromJson(String jsonString) {
        Type type = new TypeToken<ArrayList<Ingredient>>(){}.getType();
        return new Gson().fromJson(jsonString, type);
    }
    static ArrayList<Ingredient> loadIngredientsPref(Context context, int appWidgetId) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, 0);
        String ingredientsListJson = prefs.getString(INGREDIENTS_PREF_PREFIX_KEY + appWidgetId, null);
        if (ingredientsListJson != null) {
            return fromJson(ingredientsListJson);
        } else {
            return new ArrayList<>();
        }
    }

    static void deleteTitlePref(Context context, int appWidgetId) {
        SharedPreferences.Editor prefs = context.getSharedPreferences(PREFS_NAME, 0).edit();
        prefs.remove(PREF_PREFIX_KEY + appWidgetId);
        prefs.apply();
    }

    static void deleteIngredientsPref(Context context, int appWidgetId) {
        SharedPreferences.Editor prefs = context.getSharedPreferences(PREFS_NAME, 0).edit();
        prefs.remove(INGREDIENTS_PREF_PREFIX_KEY + appWidgetId);
        prefs.apply();
    }

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);

        // Set the result to CANCELED.  This will cause the widget host to cancel
        // out of the widget placement if the user presses the back button.
        setResult(RESULT_CANCELED);

        mBinding = DataBindingUtil.setContentView(RecipeIngredientsAppWidgetConfigureActivity.this,
                R.layout.recipe_ingredients_app_widget_configure);
        mRecipeNamesSpinner = mBinding.recipeSelector;

        mBinding.addButton.setOnClickListener(mOnClickListener);

        mActionsListener = new IngredientsWidgetConfigPresenter(this, Injection.provideRecipesRepository());

        mActionsListener.loadRecipes(false);
        // Find the widget id from the intent.
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null) {
            mAppWidgetId = extras.getInt(
                    AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
        }

        // If this activity was started with an intent without an app widget ID, finish with an error.
        if (mAppWidgetId == AppWidgetManager.INVALID_APPWIDGET_ID) {
            finish();
            return;
        }
    }


    @Override
    public void disableSubmitButton(boolean disable) {
        mBinding.addButton.setEnabled(!disable);
    }

    @Override
    public void showRecipes(List<Recipe> recipes) {
        ArrayList<String> recipeNames = new ArrayList<>();

        for (Recipe recipe : recipes) {
            recipeNames.add(recipe.getName());
            mIngredientsMap.put(recipe.getName(), recipe.getIngredients());
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, recipeNames);
        mRecipeNamesSpinner.setAdapter(adapter);
    }

    @Override
    public void saveWidgetDetails(String recipeName, String ingredients) {

        final Context context = RecipeIngredientsAppWidgetConfigureActivity.this;

        // When the button is clicked, store the string locally
        saveTitlePref(context, mAppWidgetId, recipeName);
        saveIngredientsPref(context, mAppWidgetId, ingredients);

        // It is the responsibility of the configuration activity to update the app widget
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        RecipeIngredientsAppWidget.updateAppWidget(context, appWidgetManager, mAppWidgetId);

        // Make sure we pass back the original appWidgetId
        Intent resultValue = new Intent();
        resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, mAppWidgetId);
        setResult(RESULT_OK, resultValue);
        finish();
    }
}

