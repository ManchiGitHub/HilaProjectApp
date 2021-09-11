package com.example.parkinson.features.main;

import android.content.Context;
import android.os.Bundle;
import android.service.autofill.Dataset;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;
import com.example.parkinson.R;
import com.example.parkinson.data.UserRepository;
import com.example.parkinson.data.enums.EDataSourceUser;
import com.example.parkinson.features.main.adapters.MessagesListAdapter;
import com.example.parkinson.model.question_models.Question;
import com.example.parkinson.model.question_models.Questionnaire;
import com.example.parkinson.model.user_models.Patient;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

public class MainFragment extends Fragment {

    private MainViewModel mainViewModel;

    CardView medicineBtn;
    CardView questionnaireBtn;
    CardView medicCaseBtn;
    CardView contactsBtn;

    ImageView medicineBadge;
    ImageView questionnaireBadge;

    UserRepository userRepository;


    RecyclerView messagesList;
//    RecyclerView reportsList;

    public MainFragment() {
        super(R.layout.fragment_main);
    }

    @Override
    public void onAttach(@NonNull Context context) {
//        ((MainActivity) getActivity()).mainComponent.inject(this);
        super.onAttach(context);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mainViewModel = new ViewModelProvider(requireActivity()).get(MainViewModel.class);

        mainViewModel.initData();
        initViews(view);
        initUi(view);
        initObservers();
        //handleData();

    }

    private void initViews(View view) {
        medicineBtn = view.findViewById(R.id.mainFragMedicineBtn);
        questionnaireBtn = view.findViewById(R.id.mainFragQuestionnaireBtn);
        medicCaseBtn = view.findViewById(R.id.mainFragMedicCaseBtn);
        medicineBadge = view.findViewById(R.id.mainFragMedicineBadge);
        questionnaireBadge = view.findViewById(R.id.mainFragQuestionnaireBadge);

        //messagesList= view.findViewById(R.id.recyclerMessages);
//        reportsList= view.findViewById(R.id.recyclerReports);

        contactsBtn = view.findViewById(R.id.mainFragMessageBtn);
    }

    private void initUi(View view) {
        medicineBtn.setOnClickListener(v -> {
            openMedicineFragment(view);
        });
        questionnaireBtn.setOnClickListener(v -> {
            openQuestionnaireFragment(view);
        });
        medicCaseBtn.setOnClickListener(v -> {
            openMedicCaseFragment(view);
        });

//        contactsBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                openContactsFragment(v);
//            }
//        });

        contactsBtn.setOnClickListener(v -> {
            openContactsFragment(view);
        });

    }

    private void initObservers() {
        mainViewModel.patientEvent.observe(getViewLifecycleOwner(), patient -> {

            //handlePatientData(patient);
        });
        mainViewModel.questionnaireEvent.observe(getViewLifecycleOwner(),questionnaire ->
        {

            for(Questionnaire qs : questionnaire)
            {


                if(qs.getDate_answered() == null) {
                    questionnaireBadge.setVisibility(View.VISIBLE);
                }

//                if (qs.isAnswered() == false)
//                {
//                    System.out.println(qs.getQuestionnaireName() + "LL");
//                    questionnaireBadge.setVisibility(View.VISIBLE);
//                }
            }
        });
//        mainViewModel.reportsData.observe(getViewLifecycleOwner(),data->{
//            ReportsListAdapter adapter = new ReportsListAdapter(data);
//            reportsList.setAdapter(adapter);
//        });
//        mainViewModel.messagesData.observe(getViewLifecycleOwner(),data-> {
//            MessagesListAdapter adapter = new MessagesListAdapter(data);
//            messagesList.setAdapter(adapter);
//
//        });

    }

//    private void handlePatientData(Patient patient) {
//        if (patient.getHasUnansweredQuestionnaire()) {
//            questionnaireBadge.setVisibility(View.VISIBLE);
//        } else {
//            questionnaireBadge.setVisibility(View.INVISIBLE);
//        }
//
//        if (patient.getNeedToUpdateMedicine()) {
//            medicineBadge.setVisibility(View.VISIBLE);
//        } else {
//            medicineBadge.setVisibility(View.INVISIBLE);
//        }
//    }

    private void handleData()
    {



//        ds.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//
//                for (DataSnapshot ds : snapshot.getChildren())
//                    System.out.println(ds.getKey());
//
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });



//        FirebaseDatabase.getInstance().getReference().child(FirebaseAuth.getInstance().getUid()).child(EDataSourceUser.QUESTIONNAIRE.name).addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//
//                //System.out.println(snapshot.getChildren());
//
//                //Questionnaire questionnaire = snapshot.child("1").getValue(Questionnaire.class);
//
//                //System.out.println(snapshot.child("0").getKey());
//                //System.out.println(questionnaire.getQuestionnaireName());
//
//                //System.out.println(snapshot.child("1").getValue() + "!!!!!!!!!!!!!!!!!!");
//
//                for(DataSnapshot ds : snapshot.getChildren())
//                {
//
//
//                    //System.out.println(ds.getValue() + "!!!!!!!!!!!!!!!!!");
//
//
//                    //Questionnaire questionnaire = ds.getValue(Questionnaire.class);
//
//
//
////                    if(String.valueOf(questionnaire.getDate_answered()).isEmpty())
////                    {
////
////                        questionnaireBadge.setVisibility(View.VISIBLE);
////                    }
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });

    }

    /** Navigates to QuestionnaireFragment with NavigationController with isNewQuestionnaire Args **/
    private void openQuestionnaireFragment(View view){
//        NavDirections action = MainFragmentDirections.actionMainFragmentToQuestionnaireFragment(mainViewModel.patientEvent.getValue().getHasUnansweredQuestionnaire());
//        Navigation.findNavController(view).navigate(action);

        NavDirections action = MainFragmentDirections.actionMainFragmentToQuestionnaireListFragment();
        Navigation.findNavController(view).navigate(action);
    }

    private void openMedicineFragment(View view) {
        NavDirections action = MainFragmentDirections.actionMainFragmentToMyMedicinesFragment();
        Navigation.findNavController(view).navigate(action);
    }

    private void openMedicCaseFragment(View view) {
        NavDirections action = MainFragmentDirections.actionMainFragmentToMyMedicCaseFragment();
        Navigation.findNavController(view).navigate(action);
    }

    private void openContactsFragment(View view) {
        NavDirections action = MainFragmentDirections.actionMainFragmentToContactFragment();
        Navigation.findNavController(view).navigate(action);
    }

//    private void openReportActivity() {
//        Toast.makeText(getActivity(), "bla bla", Toast.LENGTH_SHORT).show();
//        Intent intent = new Intent(getActivity(), NotificationActivity.class);
//        startActivity(intent);
//    }




}
