package org.ipforsmartobjects.apps.baking.stepdetail;

import android.app.Activity;
import android.support.design.widget.CollapsingToolbarLayout;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.ipforsmartobjects.apps.baking.R;
import org.ipforsmartobjects.apps.baking.data.Step;
import org.ipforsmartobjects.apps.baking.databinding.RecipeStepDetailBinding;
import org.ipforsmartobjects.apps.baking.steppages.RecipeStepScreenSlideActivity;
import org.ipforsmartobjects.apps.baking.steps.RecipeStepsListActivity;

/**
 * A fragment representing a single Recipe detail screen.
 * This fragment is either contained in a {@link RecipeStepsListActivity}
 * in two-pane mode (on tablets) or a {@link RecipeStepScreenSlideActivity}
 * on handsets.
 */
public class RecipeStepDetailFragment extends Fragment implements RecipeStepDetailContract.View {

    public static final String ARG_STEP_ID = "step_id";

    public static final String ARG_RECIPE_ID = "recipe_id";

    public static final String ARG_TOTAL_STEPS = "total_steps";


    private int mRecipeId;
    private int mStepId;
    private RecipeStepDetailBinding mBinding;
    private View mProgressBar;
    private View mDetailView;
    private View mEmptyView;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public RecipeStepDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments().containsKey(ARG_STEP_ID)) {
            // TODO: 9/24/2017 add logic to parse and show the detail
            mRecipeId = (int) getArguments().getLong(ARG_RECIPE_ID);
            mStepId = (int) getArguments().getLong(ARG_STEP_ID);
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mBinding = RecipeStepDetailBinding.inflate(inflater, container, false);


        mProgressBar = mBinding.progress;
        mDetailView = mBinding.recipeDetailContainer;
        mEmptyView = mBinding.emptyView;
        return mBinding.getRoot();
    }

    @Override
    public void setProgressIndicator(boolean active) {
        mProgressBar.setVisibility(active ? View.VISIBLE : View.GONE);
    }

    @Override
    public void showEmptyView() {
        mDetailView.setVisibility(View.GONE);
        mEmptyView.setVisibility(View.VISIBLE);
    }

    @Override
    public void showRecipeStepsDetail(Step step) {
        mDetailView.setVisibility(View.VISIBLE);
        mEmptyView.setVisibility(View.GONE);

    }

    @Override
    public void openVideo(String videoURL) {

    }
}
