package org.ipforsmartobjects.apps.baking.data;

import static com.google.common.base.Preconditions.checkNotNull;

public class RecipeRepositories {
    private static RepositoryContract.RecipesRepository repository = null;

    private RecipeRepositories() {
        // no instance
    }

    public static RepositoryContract.RecipesRepository getInMemoryRepoInstance(RecipeListingApiImpl recipesServiceApi) {
        checkNotNull(recipesServiceApi);
        if (null == repository) {
            repository = new CachedRecipesRepository(recipesServiceApi);
        }
        return repository;
    }
}
