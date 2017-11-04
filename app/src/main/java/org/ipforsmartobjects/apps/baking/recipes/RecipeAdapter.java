package org.ipforsmartobjects.apps.baking.recipes;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.squareup.picasso.Picasso;


import org.ipforsmartobjects.apps.baking.R;
import org.ipforsmartobjects.apps.baking.data.Recipe;
import org.ipforsmartobjects.apps.baking.databinding.RecipeItemCardBinding;

import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.ViewHolder> {

    private final RecipeCardGridActivity.RecipeItemListener mItemListener;
    private List<Recipe> mRecipes;
    private Context mContext;

    public RecipeAdapter(List<Recipe> Recipes, RecipeCardGridActivity.RecipeItemListener itemListener) {
        setList(Recipes);
        mItemListener = itemListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mContext = parent.getContext();

        LayoutInflater inflater = LayoutInflater.from(mContext);
        RecipeItemCardBinding binding = RecipeItemCardBinding.inflate(inflater, parent, false);
        return new ViewHolder(binding, mItemListener);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        Recipe recipe = mRecipes.get(position);

        if (!TextUtils.isEmpty(recipe.getImage())) {
            Picasso.with(mContext).load(recipe.getImage())
                    .error(R.drawable.ic_recipe_icon)
                    .into(viewHolder.mRecipeViewBinding.recipeIcon);
        }

        viewHolder.mRecipeViewBinding.recipeName.setText(recipe.getName());
        viewHolder.mRecipeViewBinding.recipeServings.setText("" + recipe.getServings());
    }

    public void replaceData(List<Recipe> Recipes) {
        setList(Recipes);
        notifyDataSetChanged();
    }

    private void setList(List<Recipe> Recipes) {
        mRecipes = checkNotNull(Recipes);
    }

    @Override
    public int getItemCount() {
        return mRecipes.size();
    }

    private Recipe getItem(int position) {
        return mRecipes.get(position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        final RecipeItemCardBinding mRecipeViewBinding;
        private final RecipeCardGridActivity.RecipeItemListener mItemListener;

        public ViewHolder(RecipeItemCardBinding binding, RecipeCardGridActivity.RecipeItemListener listener) {
            super(binding.getRoot());
            mItemListener = listener;
            itemView.setOnClickListener(this);
            mRecipeViewBinding = binding;
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            Recipe Recipe = getItem(position);
            mItemListener.onRecipeClick(Recipe);
        }
    }
}
