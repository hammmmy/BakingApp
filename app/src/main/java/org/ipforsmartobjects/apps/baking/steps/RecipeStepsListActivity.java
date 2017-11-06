package org.ipforsmartobjects.apps.baking.steps;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;


import org.ipforsmartobjects.apps.baking.Injection;
import org.ipforsmartobjects.apps.baking.R;
import org.ipforsmartobjects.apps.baking.data.Ingredient;
import org.ipforsmartobjects.apps.baking.data.Step;
import org.ipforsmartobjects.apps.baking.databinding.ActivityRecipeStepListBinding;
import org.ipforsmartobjects.apps.baking.steppages.RecipeStepScreenSlideActivity;
import org.ipforsmartobjects.apps.baking.stepdetail.RecipeStepDetailFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * An activity representing a list of Recipes. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link RecipeStepScreenSlideActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
public class RecipeStepsListActivity extends AppCompatActivity implements RecipeStepsContract.View {

    public static final String ARG_ITEM_ID = "recipe_item_id";
    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private boolean mTwoPane;
    private ActivityRecipeStepListBinding mBinding;
    private int mRecipeId;
    private int mTotalSteps;
    private TextView mEmptyView;
    private TextView mErrorView;
    private View mListViewContainer;
    private View mDetailContainer;
    private RecyclerView mIngredientsList;
    private RecyclerView mStepsList;
    private IngredientsAdapter mIngredientsAdapter;
    private RecipeStepsAdapter mStepsAdapter;
    private final RecipeStepsItemListener mItemsListener = new RecipeStepsItemListener() {
        @Override
        public void onRecipeStepClick(int recipeId, Step clickedStep) {
            mActionsListener.openStepDetails(recipeId, clickedStep);
        }
    };
    private RecipeStepsPresenter mActionsListener;
    private String mRecipeName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(RecipeStepsListActivity.this,
                R.layout.activity_recipe_step_list);

        mTwoPane = getResources().getBoolean(R.bool.is_two_pane);

        Toolbar toolbar = mBinding.toolbar;
        setSupportActionBar(toolbar);
//        toolbar.setTitle(getTitle());

        if (getIntent() != null) {
            mRecipeId = (int) getIntent().getLongExtra(ARG_ITEM_ID, -1);
        }

        mEmptyView = mBinding.stepsContainer.emptyView;
        mErrorView = mBinding.stepsContainer.errorView;
        mListViewContainer = mBinding.stepsContainer.listContainer;
        mIngredientsList = mBinding.stepsContainer.ingredientList;
        mStepsList = mBinding.stepsContainer.stepsList;

        if (mTwoPane) {
            mDetailContainer = mBinding.stepsContainer.recipeDetailContainer;
        }

        mActionsListener = new RecipeStepsPresenter(RecipeStepsListActivity.this,
                Injection.provideRecipesRepository(), mRecipeId);


        setupRecyclerViews();
    }

    @Override
    protected void onResume() {
        mActionsListener.loadSteps(false);
        super.onResume();
    }

    private void setupRecyclerViews() {
        mIngredientsAdapter = new IngredientsAdapter(new ArrayList<Ingredient>());
        mIngredientsList.setAdapter(mIngredientsAdapter);

        mStepsAdapter = new RecipeStepsAdapter(mRecipeId,
                new ArrayList<Step>(), mItemsListener);
        mStepsList.setAdapter(mStepsAdapter);
    }

    @Override
    public void setProgressIndicator(boolean active) {
        mBinding.stepsContainer.progress.setVisibility(active ? View.VISIBLE : View.GONE);
    }

    @Override
    public void showData(String recipeName, List<Step> steps, List<Ingredient> ingredients) {
        mRecipeName = recipeName;
        mBinding.toolbar.setTitle(mRecipeName);
        mListViewContainer.setVisibility(View.VISIBLE);
        mErrorView.setVisibility(View.GONE);
        mEmptyView.setVisibility(View.GONE);
        if (mTwoPane) mDetailContainer.setVisibility(View.VISIBLE);
        mStepsAdapter.replaceData(mRecipeId, steps);
        mIngredientsAdapter.replaceData(ingredients);
        mTotalSteps = steps.size();

    }

    @Override
    public void showEmptyView() {
        mListViewContainer.setVisibility(View.GONE);
        mEmptyView.setVisibility(View.VISIBLE);
        mErrorView.setVisibility(View.GONE);

    }

    @Override
    public void showErrorView() {
        mListViewContainer.setVisibility(View.GONE);
        mEmptyView.setVisibility(View.GONE);
        mErrorView.setVisibility(View.VISIBLE);
    }

    @Override
    public void showStepDetailUi(long recipeId, long stepId) {
        if (mTwoPane) {
            Bundle arguments = new Bundle();
            arguments.putLong(RecipeStepDetailFragment.ARG_RECIPE_ID, recipeId);
            arguments.putLong(RecipeStepDetailFragment.ARG_STEP_ID, stepId);
            RecipeStepDetailFragment fragment = new RecipeStepDetailFragment();
            fragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.recipe_detail_container, fragment)
                    .commit();
        } else {

            Intent intent = new Intent(RecipeStepsListActivity.this, RecipeStepScreenSlideActivity.class);
            intent.putExtra(RecipeStepDetailFragment.ARG_RECIPE_ID, recipeId);
            intent.putExtra(RecipeStepDetailFragment.ARG_STEP_ID, stepId);
            intent.putExtra(RecipeStepDetailFragment.ARG_TOTAL_STEPS, mTotalSteps);
            intent.putExtra(RecipeStepDetailFragment.ARG_RECIPE_NAME, mRecipeName);

            startActivity(intent);
        }

    }

    interface RecipeStepsItemListener {
        void onRecipeStepClick(int recipeId, Step clickedStep);
    }

}
