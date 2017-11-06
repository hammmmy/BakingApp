package org.ipforsmartobjects.apps.baking.steppages;

import android.content.res.Configuration;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.AppCompatButton;
import android.view.View;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.NavUtils;
import android.view.MenuItem;

import org.ipforsmartobjects.apps.baking.Injection;
import org.ipforsmartobjects.apps.baking.R;
import org.ipforsmartobjects.apps.baking.databinding.ActivityRecipeStepScreenSlideBinding;
import org.ipforsmartobjects.apps.baking.stepdetail.RecipeStepDetailFragment;
import org.ipforsmartobjects.apps.baking.steps.RecipeStepsListActivity;

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
    private int mRecipeId;
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
            mRecipeId = (int) intent.getLongExtra(RecipeStepDetailFragment.ARG_RECIPE_ID, -1);
            mStepId = intent.getLongExtra(RecipeStepDetailFragment.ARG_STEP_ID, -1);
            mTotalSteps = intent.getIntExtra(RecipeStepDetailFragment.ARG_TOTAL_STEPS, -1);
        }

        mPreviousButton = mBinding.previousButton;
        mNextButton = mBinding.nextButton;

        mPager = mBinding.pager;
        mPagerAdapter = new RecipeStepScreenSlidePagerAdapter(getSupportFragmentManager(),
                mRecipeId, mTotalSteps);
        mPager.setAdapter(mPagerAdapter);

        mPager.setCurrentItem((int) mStepId);

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

    private void setLayoutVisibility(boolean visible) {
        ActionBar actionBar = getSupportActionBar();
        if(null != actionBar && !visible) {
            actionBar.hide();
        }
        mPreviousButton.setVisibility(visible ? View.VISIBLE : View.GONE);
        mNextButton.setVisibility(visible ? View.VISIBLE : View.GONE);
    }
    private void setButtonVisibility() {
        int orientation = getResources().getConfiguration().orientation;
        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            setLayoutVisibility(false);
        } else {
            setLayoutVisibility(true);
            mNextButton.setVisibility((mPager.getCurrentItem() == mPagerAdapter.getCount() - 1) ?
                    View.GONE : View.VISIBLE);
            mPreviousButton.setVisibility((mPager.getCurrentItem() == 0) ?
                    View.GONE : View.VISIBLE);
        }
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

}
