package org.ipforsmartobjects.apps.baking.util;

import org.ipforsmartobjects.apps.baking.data.Recipe;


import java.util.List;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;


public class RecipeListingApiHelper {

    private static final String BASE_URL = "https://d17h27t6h515a5.cloudfront.net/";
    private static final String BAKING_JSON = "topher/2017/May/59121517_baking/baking.json";


    // use Retrofit 2 without Rx
    public static RecipeListingApi getApi() {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        return retrofit.create(RecipeListingApi.class);
    }

    public interface RecipeListingApi {
        @GET(BAKING_JSON)
        Call<List<Recipe>> getRecipeList();
    }

}
