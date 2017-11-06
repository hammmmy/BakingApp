package org.ipforsmartobjects.apps.baking.widget;

import android.support.annotation.NonNull;

import com.google.gson.Gson;

import org.ipforsmartobjects.apps.baking.data.Ingredient;
import org.ipforsmartobjects.apps.baking.data.Recipe;
import org.ipforsmartobjects.apps.baking.data.RepositoryContract;

import java.util.ArrayList;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

public class IngredientsWidgetConfigPresenter implements IngredientsWidgetConfigContract.Presenter {
    private final RepositoryContract.RecipesRepository mRecipesRepository;
    private final IngredientsWidgetConfigContract.View mRecipesView;

    public IngredientsWidgetConfigPresenter(@NonNull IngredientsWidgetConfigContract.View RecipesView,
                                            @NonNull RepositoryContract.RecipesRepository repository) {
        mRecipesView = checkNotNull(RecipesView, "RecipesView cannot be null");
        mRecipesRepository = repository;
    }

    @Override
    public void loadRecipes(boolean forceUpdate) {
//        mRecipesView.setProgressIndicator(true);
        mRecipesView.disableSubmitButton(true);

            if (forceUpdate) {
                mRecipesRepository.clearCache();
            }

            mRecipesRepository.loadRecipes(new RepositoryContract.RecipesRepository.LoadRecipesCallback() {
                @Override
                public void onRecipesLoaded(List<Recipe> Recipes) {
//                    mRecipesView.setProgressIndicator(false);
                    mRecipesView.disableSubmitButton(false);
                    mRecipesView.showRecipes(Recipes);
                }

                @Override
                public void onLoadingFailed() {
//                    mRecipesView.setProgressIndicator(false);
                    mRecipesView.disableSubmitButton(false);
//                    mRecipesView.showErrorView();
                }
            });
        }

    private static String toJson(ArrayList<Ingredient> jsonObject) {
        return new Gson().toJson(jsonObject);
    }



    @Override
    public void saveWidgetDetails(String recipeName, @NonNull List<Ingredient> ingredients) {
        String json = toJson((ArrayList<Ingredient>)ingredients);
        mRecipesView.saveWidgetDetails(recipeName, json);
    }

    
}
