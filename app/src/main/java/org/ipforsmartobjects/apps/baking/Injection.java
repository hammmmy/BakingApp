package org.ipforsmartobjects.apps.baking;


import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;

import org.ipforsmartobjects.apps.baking.data.RecipeListingApiImpl;
import org.ipforsmartobjects.apps.baking.data.RecipeRepositories;
import org.ipforsmartobjects.apps.baking.data.RepositoryContract;

/**
 * Enables injection of implementations for {@link RepositoryContract.RecipesRepository} at compile time.
 */
public class Injection {

    // requires support Fragment for support loader
    public static RepositoryContract.RecipesRepository provideRecipesRepository() {
        return RecipeRepositories.getInMemoryRepoInstance(new RecipeListingApiImpl());
    }
}
