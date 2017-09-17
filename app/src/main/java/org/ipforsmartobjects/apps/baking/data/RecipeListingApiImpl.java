package org.ipforsmartobjects.apps.baking.data;

import org.ipforsmartobjects.apps.baking.util.RecipeListingApiHelper;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RecipeListingApiImpl implements RecipesServiceApi {

    private final RecipeListingApiHelper.RecipeListingApi mApi;

    public RecipeListingApiImpl() {
        // retrofit 2 without Rx code
        mApi = RecipeListingApiHelper.getApi();
    }

    private static final String TAG = "RecipeListingApiImplWit";

    @Override
    public void getRecipes(final MoviesServiceCallback<List<Recipe>> callback) {
        Call<List<Recipe>> popularMovieCall = mApi.getRecipeList();
        // retrofit 2 without Rx code
        popularMovieCall.enqueue(new Callback<List<Recipe>>() {
            @Override
            public void onResponse(Call<List<Recipe>> call, Response<List<Recipe>> response) {
                if (response.isSuccessful()) {
                    List<Recipe> recipes = response.body();
                    callback.onLoaded(recipes);
                } else {
                    callback.onLoadingFailed();
                }
            }

            @Override
            public void onFailure(Call<List<Recipe>> call, Throwable t) {
                callback.onLoadingFailed();
            }
        });

    }
}
