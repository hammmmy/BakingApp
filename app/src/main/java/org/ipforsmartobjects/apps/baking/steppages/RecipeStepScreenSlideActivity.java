package org.ipforsmartobjects.apps.baking.steppages;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.NavUtils;
import android.view.MenuItem;

import org.ipforsmartobjects.apps.baking.Injection;
import org.ipforsmartobjects.apps.baking.R;
import org.ipforsmartobjects.apps.baking.data.Step;
import org.ipforsmartobjects.apps.baking.databinding.ActivityRecipeStepScreenSlideBinding;
import org.ipforsmartobjects.apps.baking.stepdetail.RecipeStepDetailFragment;
import org.ipforsmartobjects.apps.baking.steps.RecipeStepsListActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * An activity representing a single Recipe detail screen. This
 * activity is only used on narrow width devices. On tablet-size devices,
 * item details are presented side-by-side with a list of items
 * in a {@link RecipeStepsListActivity}.
 */
public class RecipeStepScreenSlideActivity extends AppCompatActivity implements RecipeStepsScreenSlideContract.View {

    private ViewPager mPager;
    private ActivityRecipeStepScreenSlideBinding mBinding;
    private PagerAdapter mPagerAdapter;
    private long mRecipeId;
    private long mStepId;
    private RecipeStepsScreenSlidePresenter mActionsListener;
    private int mTotalSteps;
    private AppCompatButton mPreviousButton;
    private AppCompatButton mNextButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(RecipeStepScreenSlideActivity.this,
                R.layout.activity_recipe_step_screen_slide);

        // Show the Up button in the action bar.
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        Intent intent = getIntent();
        if (intent != null) {
            mRecipeId = intent.getLongExtra(RecipeStepDetailFragment.ARG_RECIPE_ID, -1);
            mStepId = intent.getLongExtra(RecipeStepDetailFragment.ARG_STEP_ID, -1);
            mTotalSteps = intent.getIntExtra(RecipeStepDetailFragment.ARG_TOTAL_STEPS, -1);
        }
        mPager = mBinding.pager;
        mPagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager());
        mPager.setAdapter(mPagerAdapter);

        mPager.setCurrentItem((int) mStepId);

        mPreviousButton = mBinding.previousButton;
        mNextButton = mBinding.nextButton;
        mPreviousButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mActionsListener.onPreviousClicked();
            }
        });

        mNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mActionsListener.onNextClicked();
            }
        });

        setButtonVisibility();
        mPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                setButtonVisibility();
            }
        });

        mActionsListener = new RecipeStepsScreenSlidePresenter(RecipeStepScreenSlideActivity.this,
                Injection.provideRecipesRepository());


    }

    private void setButtonVisibility() {
        mNextButton.setVisibility((mPager.getCurrentItem() == mPagerAdapter.getCount() - 1) ?
                View.GONE : View.VISIBLE);
        mPreviousButton.setVisibility((mPager.getCurrentItem() == 0) ?
                View.GONE : View.VISIBLE);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            // This ID represents the Home or Up button. In the case of this
            // activity, the Up button is shown. Use NavUtils to allow users
            // to navigate up one level in the application structure. For
            // more details, see the Navigation pattern on Android Design:
            //
            // http://developer.android.com/design/patterns/navigation.html#up-vs-back
            //
            NavUtils.navigateUpTo(this, new Intent(this, RecipeStepsListActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void showPrevious() {
        mPager.setCurrentItem(mPager.getCurrentItem() - 1);
    }

    @Override
    public void showNext() {
        mPager.setCurrentItem(mPager.getCurrentItem() + 1);
    }

    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {
        public ScreenSlidePagerAdapter(FragmentManager fm) {
            super(fm);
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

}
