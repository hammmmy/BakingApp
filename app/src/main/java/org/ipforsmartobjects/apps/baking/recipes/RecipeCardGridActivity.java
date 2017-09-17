package org.ipforsmartobjects.apps.baking.recipes;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import org.ipforsmartobjects.apps.baking.R;
import org.ipforsmartobjects.apps.baking.data.Recipe;
import org.ipforsmartobjects.apps.baking.databinding.ActivityRecipeCardGridBinding;
import org.ipforsmartobjects.apps.baking.steps.RecipeStepsListActivity;

import java.util.ArrayList;
import java.util.List;

public class RecipeCardGridActivity extends AppCompatActivity implements RecipesContract.View{

    private View mListView;
    private ActivityRecipeCardGridBinding mBinding;
    private RecipeAdapter mListAdapter;
    private RecipesContract.Presenter mActionsListener;
    private TextView mEmptyView;

    private final RecipeItemListener mItemListener = new RecipeItemListener() {
        @Override
        public void onRecipeClick(Recipe clickedRecipe) {
            mActionsListener.openRecipeSteps(clickedRecipe);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(RecipeCardGridActivity.this,
                R.layout.activity_recipe_card_grid);

        setSupportActionBar(mBinding.toolbar);

        RecyclerView recyclerView = mBinding.recipeGridLayoutContainer.recipeGrid;
        assert recyclerView != null;
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemViewCacheSize(20); // for faster scroll (?)
        setupRecyclerView(recyclerView);
        mEmptyView = mBinding.recipeGridLayoutContainer.emptyView;
    }

    private void setupRecyclerView(@NonNull RecyclerView recyclerView) {
        mListAdapter = new RecipeAdapter(new ArrayList<Recipe>(0), mItemListener);
        recyclerView.setAdapter(mListAdapter);
    }

    @Override
    public void setProgressIndicator(boolean active) {
        mBinding.recipeGridLayoutContainer.progress.setVisibility(active ? View.VISIBLE : View.GONE);
    }

    @Override
    public void showRecipes(List<Recipe> recipes) {
        mListView.setVisibility(View.VISIBLE);
        mEmptyView.setVisibility(View.GONE);
        mListAdapter.replaceData(recipes);

    }

    @Override
    public void showEmptyView() {
        mListView.setVisibility(View.GONE);
        mEmptyView.setVisibility(View.VISIBLE);
    }

    @Override
    public void showRecipeSteps(long recipeId) {
        Intent intent = new Intent(RecipeCardGridActivity.this, RecipeStepsListActivity.class);
        intent.putExtra(RecipeStepsListActivity.ARG_ITEM_ID, recipeId);
        ActivityOptionsCompat trans = ActivityOptionsCompat.makeSceneTransitionAnimation(RecipeCardGridActivity.this);
        ActivityCompat.startActivity(RecipeCardGridActivity.this, intent, trans.toBundle());

    }

    interface RecipeItemListener {
        void onRecipeClick(Recipe clickedRecipe);
    }


}
