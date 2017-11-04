package org.ipforsmartobjects.apps.baking.steps;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.ipforsmartobjects.apps.baking.data.Step;
import org.ipforsmartobjects.apps.baking.databinding.RecipeStepItemBinding;

import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

public class RecipeStepsAdapter extends RecyclerView.Adapter<RecipeStepsAdapter.ViewHolder> {

    private final RecipeStepsListActivity.RecipeStepsItemListener mItemListener;
    private int mRecipeId;
    private List<Step> mSteps;
    private Context mContext;

    public RecipeStepsAdapter(int recipeId, List<Step> steps, RecipeStepsListActivity.RecipeStepsItemListener itemListener) {
        setList(recipeId, steps);
        mItemListener = itemListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mContext = parent.getContext();

        LayoutInflater inflater = LayoutInflater.from(mContext);
        RecipeStepItemBinding binding = RecipeStepItemBinding.inflate(inflater, parent, false);
        return new ViewHolder(binding, mItemListener);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        Step step = mSteps.get(position);
        viewHolder.mRecipeViewBinding.stepId.setText(""+step.getId()+". ");

        viewHolder.mRecipeViewBinding.shortDescription.setText(step.getShortDescription());
    }

    public void replaceData(int recipeId, List<Step> steps) {
        setList(recipeId, steps);
        notifyDataSetChanged();
    }

    private void setList(int recipeId, List<Step> steps) {
        mRecipeId = recipeId;
        mSteps = checkNotNull(steps);
    }

    @Override
    public int getItemCount() {
        return mSteps.size();
    }

    private Step getItem(int position) {
        return mSteps.get(position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        final RecipeStepItemBinding mRecipeViewBinding;
        private final RecipeStepsListActivity.RecipeStepsItemListener mItemListener;

        public ViewHolder(RecipeStepItemBinding binding, RecipeStepsListActivity.RecipeStepsItemListener listener) {
            super(binding.getRoot());
            mItemListener = listener;
            itemView.setOnClickListener(this);
            mRecipeViewBinding = binding;
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            Step step = getItem(position);
            mItemListener.onRecipeStepClick(mRecipeId, step);
        }
    }
}
