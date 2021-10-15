package com.example.parkinson.features.questionnaire;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.parkinson.data.DataRepository;
import com.example.parkinson.data.UserRepository;
import com.example.parkinson.model.general_models.Medicine;
import com.example.parkinson.model.question_models.Questionnaire;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class QuestionnaireListViewModel extends ViewModel {
    private final UserRepository userRepository;

    public MutableLiveData<List<Questionnaire>> myMedicationData = new MutableLiveData<>();

    MutableLiveData<Boolean> isLoading = new MutableLiveData<>();

    // @Inject tells Dagger how to create instances of MainViewModel
    @Inject
    public QuestionnaireListViewModel(UserRepository userRepository, DataRepository dataRepository) {
        this.userRepository = userRepository;
    }

    public void initQuestionnaireData() {
        isLoading.postValue(true);
        userRepository.getQuestionnaireList(setQuestionnaireListener());

    }



    private ChildEventListener setQuestionnaireListener() {
        return new ChildEventListener() {

            List<Questionnaire> list = new ArrayList<Questionnaire>();

            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                if (snapshot.exists()) {

                        Questionnaire questionnaire = snapshot.getValue(Questionnaire.class);

                        list.add(questionnaire);


                        myMedicationData.setValue(list);

                        isLoading.postValue(false);

                }
                isLoading.postValue(false);

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Questionnaire questionnaire = new Questionnaire();
                if (snapshot.exists()) {
                        questionnaire = snapshot.getValue(Questionnaire.class);
                        //medicationHashMap.put(med.getId(), med);

                }
                List<Questionnaire> list = new ArrayList<Questionnaire>();
                list.add(questionnaire);
                myMedicationData.setValue(list);
//                updateFilteredCategory();
                isLoading.postValue(false);
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
//                if (snapshot.exists()) {
//                    Medicine med = snapshot.getValue(Medicine.class);
//                    medicationHashMap.remove(med.getId());
//                }
//                List<Medicine> list = new ArrayList<Medicine>(medicationHashMap.values());
//                myMedicationData.setValue(list);
////                updateFilteredCategory();
//                isLoading.postValue(false);
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                isLoading.postValue(false);
            }
        };
    }


}
