package org.ipforsmartobjects.apps.baking.data;

import java.util.List;

public interface RecipesServiceApi {

    void getRecipes(MoviesServiceCallback<List<Recipe>> callback);

    interface MoviesServiceCallback<T> {

        void onLoaded(T movies);

        void onLoadingFailed();
    }
}