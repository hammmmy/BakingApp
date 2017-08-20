package org.ipforsmartobjects.apps.baking.data;

import android.content.Context;
import android.util.Log;

import org.ipforsmartobjects.apps.baking.util.RecipeListingApiHelper;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Hamid on 3/5/2017.
 */

public class RecipeListingApiImpl implements RecipeListingApiHelper.RecipeListingApi {

    private final RecipeListingApiHelper.RecipeListingApi mApi;

    public RecipeListingApiImpl() {
        // retrofit 2 without Rx code
        mApi = RecipeListingApiHelper.getApi();
    }

    private static final String TAG = "RecipeListingApiImplWit";
    @Override
    public Call<List<Recipe>> getRecipeList() {
        Call<List<Recipe>> popularMovieCall = mApi.getRecipeList();

        popularMovieCall.enqueue(new Callback<List<Recipe>>() {
            @Override
            public void onResponse(Call<List<Recipe>> call, Response<List<Recipe>> response) {
                if (response.isSuccessful()) {
                    final List<Recipe> list = response.body();
                    Log.d(TAG, "onResponse: success");
                } else {
                    Log.d(TAG, "onResponse: failure");
                }
            }

            @Override
            public void onFailure(Call<List<Recipe>> call, Throwable t) {
                Log.d(TAG, "onFailure: ");
            }
        });
        return null;
    }

}
