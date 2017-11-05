package org.ipforsmartobjects.apps.baking.steppages;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import org.ipforsmartobjects.apps.baking.stepdetail.RecipeStepDetailFragment;

/**
 * Created by Hamid on 11/5/2017.
 */

class RecipeStepScreenSlidePagerAdapter extends FragmentStatePagerAdapter {
    private final int mRecipeId;
    private final int mTotalSteps;

    public RecipeStepScreenSlidePagerAdapter(FragmentManager fm, int recipeId, int totalSteps) {
        super(fm);
        mRecipeId = recipeId;
        mTotalSteps = totalSteps;
    }

    @Override
    public Fragment getItem(int position) {
        Bundle arguments = new Bundle();
        arguments.putLong(RecipeStepDetailFragment.ARG_RECIPE_ID, mRecipeId);
        arguments.putLong(RecipeStepDetailFragment.ARG_STEP_ID, position);
        RecipeStepDetailFragment fragment = new RecipeStepDetailFragment();
        fragment.setArguments(arguments);
        return fragment;
    }

    @Override
    public int getCount() {
        return mTotalSteps;
    }
}

