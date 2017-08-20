package org.ipforsmartobjects.apps.baking.util;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.ipforsmartobjects.apps.baking.data.Recipe;


import java.util.List;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;


public class RecipeListingApiHelper {

    private static final String BASE_URL = "https://d17h27t6h515a5.cloudfront.net/topher/2017/May/59121517_baking/baking.json";


    // use Retrofit 2 without Rx
    public static RecipeListingApi getApi() {
        // change to camelCase
        Gson camelCaseGson = new GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .create();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(camelCaseGson))
                .build();
        return retrofit.create(RecipeListingApi.class);
    }

    public interface RecipeListingApi {
        @GET
        Call<List<Recipe>> getRecipeList();
    }

}
