package com.example.parkinson.features.questionnaire;

import android.util.Log;

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
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class QuestionnaireListViewModel extends ViewModel {
    private final UserRepository userRepository;
    private final DataRepository dataRepository;


    public MutableLiveData<List<Questionnaire>> myMedicationData = new MutableLiveData<>();
//    public MutableLiveData<List<MedicineCategory>> categoryListData = new MutableLiveData<>();
//    public MutableLiveData<MedicineCategory> filteredCategory = new MutableLiveData<>();

    MutableLiveData<Boolean> isLoading = new MutableLiveData<>();
    HashMap<String, Medicine> medicationHashMap = new HashMap<>();

    // @Inject tells Dagger how to create instances of MainViewModel
    @Inject
    public QuestionnaireListViewModel(UserRepository userRepository, DataRepository dataRepository) {
        this.userRepository = userRepository;
        this.dataRepository = dataRepository;
    }

    public void initQuestionnaireData() {
        isLoading.postValue(true);
        userRepository.getQuestionnaireList(setQuestionnaireListener());
//        dataRepository.getMedicineList(setMedicationCategoryListener());
    }
//
//    public void addNewMedicine(Medicine medicine) {
//        isLoading.postValue(true);
//        userRepository.postMedication(medicine);
//    }
//
//    public void removeMedicine(Medicine medicine) {
//        isLoading.postValue(true);
//        userRepository.deleteMedication(medicine);
//    }

//    private int currentChosenCategoryPosition = -1;
//    public void updateFilteredCategory() {
//        if (currentChosenCategoryPosition > -1){
//            filterCategory(currentChosenCategoryPosition);
//        }
//    }

//    public void filterCategory(int chosenCategoryPosition) {
//        currentChosenCategoryPosition = chosenCategoryPosition;
//        MedicineCategory chosenCategory = categoryListData.getValue().get(chosenCategoryPosition);
//        List<Medicine> filteredList = new ArrayList<>();
//        for (Medicine medicine : chosenCategory.getMedicineList()) {
//            if (medicationHashMap.containsKey(medicine.getId())) {
//                filteredList.add(medicationHashMap.get(medicine.getId()));
//            } else {
//                filteredList.add(medicine);
//            }
//        }
//        filteredCategory.postValue(new MedicineCategory(chosenCategory.getCategoryName(),filteredList));
//    }

//    private ValueEventListener setMedicationCategoryListener() {
//        return new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                //todo add no medication list
//                if (dataSnapshot.exists()) {
//                    List<MedicineCategory> medicationCategoriesList = new ArrayList<>();
//                    for (DataSnapshot category : dataSnapshot.getChildren()) {
//                        MedicineCategory currentCategory = new MedicineCategory();
//                        String name = category.child("categoryName").getValue(String.class);
//                        List<Medicine> medicineList = new ArrayList<>();
//                        for (DataSnapshot medication : category.child("medicationList").getChildren()) {
//                            medicineList.add(medication.getValue(Medicine.class));
//                        }
//                        currentCategory.setCategoryName(name);
//                        currentCategory.setMedicineList(medicineList);
//                        medicationCategoriesList.add(currentCategory);
//                    }
//                    categoryListData.setValue(medicationCategoriesList);
//                    isLoading.postValue(false);
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//                isLoading.postValue(false);
//            }
//        };
//    }


    private ChildEventListener setQuestionnaireListener() {
        return new ChildEventListener() {

            List<Questionnaire> list = new ArrayList<Questionnaire>();

            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                if (snapshot.exists()) {



                    //Log.d("GGGG",snapshot.getKey());


                       // Log.d("GGGG",ds.getKey());

                        Questionnaire questionnaire = snapshot.getValue(Questionnaire.class);
                        //Log.d("GGGG",questionnaire.getQuestionnaireName());

                        //medicationHashMap.put(med.getId(), med);
                        list.add(questionnaire);
                        myMedicationData.setValue(list);
//                    updateFilteredCategory();
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
