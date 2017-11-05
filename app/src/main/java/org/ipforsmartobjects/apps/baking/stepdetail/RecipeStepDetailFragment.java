package org.ipforsmartobjects.apps.baking.stepdetail;

import android.app.Activity;
import android.support.design.widget.CollapsingToolbarLayout;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.ipforsmartobjects.apps.baking.Injection;
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

    public static final String ARG_RECIPE_NAME = "recipe_name";

    private int mRecipeId;
    private int mStepId;
    private RecipeStepDetailBinding mBinding;
    private View mProgressBar;
    private View mDetailView;
    private View mEmptyView;
    private RecipeStepDetailPresenter mActionsListener;
    private CollapsingToolbarLayout mAppBarLayout;

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
            mRecipeId = (int) getArguments().getLong(ARG_RECIPE_ID);
            mStepId = (int) getArguments().getLong(ARG_STEP_ID);
        }

        mActionsListener = new RecipeStepDetailPresenter(this,
                Injection.provideRecipesRepository());
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mBinding = RecipeStepDetailBinding.inflate(inflater, container, false);


        mProgressBar = mBinding.stepLayoutContainer.progress;
        mDetailView = mBinding.stepLayoutContainer.recipeDetailContainer;
        mEmptyView = mBinding.stepLayoutContainer.emptyView;
//        mAppBarLayout = mBinding.toolbarLayout;

        ((AppCompatActivity) getActivity()).setSupportActionBar(mBinding.toolbar);

        mActionsListener.openStep(mRecipeId, mStepId);

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
    public void showStep(Step step) {
        mDetailView.setVisibility(View.VISIBLE);
        mEmptyView.setVisibility(View.GONE);

        mBinding.toolbar.setTitle(step.getShortDescription());
//        if (mAppBarLayout != null) {
//            mAppBarLayout.setTitle(step.getShortDescription());
//        }
//        mBinding.detailToolbar.setTitle(step.getShortDescription());

        mBinding.stepLayoutContainer.stepDescription.setText(step.getDescription());

        if (!TextUtils.isEmpty(step.getThumbnailURL())) {
            Picasso.with(getActivity()).load(step.getThumbnailURL())
                    .error(android.R.drawable.ic_menu_report_image)
                    .into(mBinding.stepLayoutContainer.stepVideoThumbnail);

        }


    }

    @Override
    public void openVideo(String videoURL) {

    }
}
