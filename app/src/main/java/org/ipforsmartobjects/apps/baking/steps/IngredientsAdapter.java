package org.ipforsmartobjects.apps.baking.steps;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import org.ipforsmartobjects.apps.baking.data.Ingredient;
import org.ipforsmartobjects.apps.baking.databinding.RecipeIngredientListItemBinding;

import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

public class IngredientsAdapter extends RecyclerView.Adapter<IngredientsAdapter.ViewHolder> {

    private List<Ingredient> mIngredients;
    private Context mContext;

    public IngredientsAdapter(List<Ingredient> ingredients) {
        setList(ingredients);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mContext = parent.getContext();

        LayoutInflater inflater = LayoutInflater.from(mContext);
        RecipeIngredientListItemBinding binding = RecipeIngredientListItemBinding.inflate(inflater, parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        Ingredient ingredient = mIngredients.get(position);

        viewHolder.mIngredientViewBinding.ingredient.setText(ingredient.getIngredient());
        viewHolder.mIngredientViewBinding.ingredientQuantity.setText("" + ingredient.getQuantity());
        viewHolder.mIngredientViewBinding.ingredientMeasure.setText(ingredient.getMeasure());
    }

    public void replaceData(List<Ingredient> ingredients) {
        setList(ingredients);
        notifyDataSetChanged();
    }

    private void setList(List<Ingredient> ingredients) {
        mIngredients = checkNotNull(ingredients);
    }

    @Override
    public int getItemCount() {
        return mIngredients.size();
    }

    private Ingredient getItem(int position) {
        return mIngredients.get(position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        final RecipeIngredientListItemBinding mIngredientViewBinding;


        public ViewHolder(RecipeIngredientListItemBinding binding) {
            super(binding.getRoot());
            mIngredientViewBinding = binding;
        }
    }
}
