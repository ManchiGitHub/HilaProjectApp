package com.example.parkinson.features.metrics;

import android.content.Context;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.parkinson.R;
import com.example.parkinson.features.main.MainActivity;
import com.example.parkinson.features.metrics.MyMetricsMainAdapter.MyMedicinesMainAdapterListener;
import com.example.parkinson.model.general_models.Medicine;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class MyMetricsFragment extends Fragment {


    private MetricViewModel metricViewModel;

    RecyclerView recyclerView;
    MyMetricsMainAdapter adapter;

    public MyMetricsFragment(){
        super(R.layout.fragment_my_medicines);
    }

    @Override
    public void onAttach(@NonNull Context context) {
//        ((MainActivity) getActivity()).mainComponent.inject(this);
        super.onAttach(context);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        metricViewModel = new ViewModelProvider(requireActivity()).get(MetricViewModel.class);

        metricViewModel.initMedicineData();
        initViews(view);
        initUi(view);
        initObservers();

    }

    private void initViews(View view) {
        recyclerView = view.findViewById(R.id.myMedicinesFragRecycler);
    }

    private void initUi(View view) {
        adapter = new MyMetricsMainAdapter(getMainAdapterListener(), getContext());
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        getView().findViewById(R.id.myMedicinesFragExitBtn).setOnClickListener(v->{
            getActivity().onBackPressed();
        });

        CardView addMedicine = view.findViewById(R.id.myMedicinesFragAddBtn);
        addMedicine.setOnClickListener(v -> {
            NavDirections action = MyMetricsFragmentDirections.actionMedicineFragmentToMedicineCategoryFragment();
            Navigation.findNavController(view).navigate(action);
        });
    }

    private void initObservers() {
        metricViewModel.isLoading.observe(getViewLifecycleOwner(), isLoading-> {
            MainActivity mainActivity = (MainActivity) getActivity();
            mainActivity.updateLoadingScreen(isLoading);
        });
        metricViewModel.myMedicationData.observe(getViewLifecycleOwner(), medicationCategories -> {
            adapter.updateMedicineList(medicationCategories);
        });
    }


    private MyMedicinesMainAdapterListener getMainAdapterListener(){
        return new MyMedicinesMainAdapterListener() {
            @Override
            public void onMedicineClick(Medicine medicine) {
                NavDirections action = MyMetricsFragmentDirections.actionMyMedicinesFragmentToSingleMedicineFrag(medicine);
                Navigation.findNavController(getView()).navigate(action);
            }
        };
    }

}
