package org.ipforsmartobjects.apps.baking.widget;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.widget.RemoteViews;

import org.ipforsmartobjects.apps.baking.R;
import org.ipforsmartobjects.apps.baking.data.Ingredient;

import java.util.ArrayList;

/**
 * Implementation of App Widget functionality.
 * App Widget Configuration implemented in {@link RecipeIngredientsAppWidgetConfigureActivity RecipeIngredientsAppWidgetConfigureActivity}
 */
public class RecipeIngredientsAppWidget extends AppWidgetProvider {

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        CharSequence widgetText = RecipeIngredientsAppWidgetConfigureActivity.loadTitlePref(context, appWidgetId);
        ArrayList<Ingredient> ingredients = RecipeIngredientsAppWidgetConfigureActivity.loadIngredientsPref(context, appWidgetId);

        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.recipe_ingredients_app_widget_view);
        views.setTextViewText(R.id.recipe_name, widgetText);
        views.removeAllViews(R.id.ingredients_container);

        for (Ingredient ingredient : ingredients) {
            RemoteViews ingredientView = new RemoteViews(context.getPackageName(),
                    R.layout.recipe_ingredients_app_widget_item);

            String ingredientStr = new StringBuilder().append(ingredient.getIngredient()).append(" ")
                    .append(ingredient.getQuantity()).append(" ").append(ingredient.getMeasure()).toString();

            ingredientView.setTextViewText(R.id.ingredient_name, ingredientStr);
            views.addView(R.id.ingredients_container, ingredientView);
        }

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        // When the user deletes the widget, delete the preference associated with it.
        for (int appWidgetId : appWidgetIds) {
            RecipeIngredientsAppWidgetConfigureActivity.deleteTitlePref(context, appWidgetId);
            RecipeIngredientsAppWidgetConfigureActivity.deleteIngredientsPref(context, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}

