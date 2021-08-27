package com.example.parkinson.features.metrics;

import com.example.parkinson.features.metrics.binder.MetricBinderEmptyList;
import com.example.parkinson.features.metrics.binder.MetricBinderHeader;
import com.example.parkinson.features.metrics.binder.MetricBinderMetric;
import com.example.parkinson.features.metrics.models.CategoryEmpty;
import com.example.parkinson.model.general_models.Medicine;

import java.util.List;

import mva2.adapter.ItemSection;
import mva2.adapter.ListSection;
import mva2.adapter.MultiViewAdapter;

public class MyMetricsMainAdapter extends MultiViewAdapter {

    MyMedicinesMainAdapterListener listener;

    public interface MyMedicinesMainAdapterListener extends MetricBinderMetric.MedicineBinderMedicineListener {
    }

    MyMetricsMainAdapter(MyMedicinesMainAdapterListener listener) {
        this.listener = listener;
        init();
    }

    ItemSection<CategoryEmpty> emptyList = new ItemSection<>();

    private void init() {
        this.registerItemBinders(
                new MetricBinderHeader(),
                new MetricBinderMetric(listener),
                new MetricBinderEmptyList());
        emptyList.setItem(new CategoryEmpty());

        addSection(emptyList);
    }

    void updateMedicineList(List<Medicine> list) {
        this.removeAllSections();
        if(list.isEmpty()){
            addSection(emptyList);
        } else {
            ListSection<Medicine> medicationListSection = new ListSection<>();
            medicationListSection.set(list);
            this.addSection(medicationListSection);
        }

    }
}
