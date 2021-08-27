package com.example.parkinson.features.metrics.binder;

import android.view.View;
import android.view.ViewGroup;

import com.example.parkinson.R;
import com.example.parkinson.features.metrics.models.CategoryEmpty;

import mva2.adapter.ItemBinder;
import mva2.adapter.ItemViewHolder;

public class MetricBinderEmptyList extends ItemBinder<CategoryEmpty, MetricBinderEmptyList.ViewHolder> {

    @Override
    public ViewHolder createViewHolder(ViewGroup parent) {
        return new ViewHolder(inflate(parent, R.layout.item_empty_medicine_list));
    }

    @Override
    public boolean canBindData(Object item) {
        return item instanceof CategoryEmpty;
    }

    @Override
    public void bindViewHolder(ViewHolder holder, CategoryEmpty item) {

    }

    static class ViewHolder extends ItemViewHolder<CategoryEmpty> {

        public ViewHolder(View itemView) {
            super(itemView);

        }
    }
}
