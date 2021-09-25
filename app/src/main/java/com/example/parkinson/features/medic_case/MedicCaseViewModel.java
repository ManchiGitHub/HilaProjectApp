package com.example.parkinson.features.medic_case;

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
public class MedicCaseViewModel extends ViewModel {
    private final UserRepository userRepository;
    private final DataRepository dataRepository;


    public MutableLiveData<List<MedicFile>> myMedicationData = new MutableLiveData<>();
//    public MutableLiveData<List<MedicineCategory>> categoryListData = new MutableLiveData<>();
//    public MutableLiveData<MedicineCategory> filteredCategory = new MutableLiveData<>();

    MutableLiveData<Boolean> isLoading = new MutableLiveData<>();
    HashMap<String, Medicine> medicationHashMap = new HashMap<>();

    // @Inject tells Dagger how to create instances of MainViewModel
    @Inject
    public MedicCaseViewModel(UserRepository userRepository, DataRepository dataRepository) {
        this.userRepository = userRepository;
        this.dataRepository = dataRepository;
    }

    public void initMedicineData() {
        isLoading.postValue(true);
        userRepository.getFilesList(setMyMedicationListener());
        isLoading.postValue(false);
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
    //


    private ValueEventListener setFilesListener()
    {
        List<MedicFile> list = new ArrayList<MedicFile>();

        return new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
//                    Medicine med = snapshot.getValue(Medicine.class);
//                    medicationHashMap.put(med.getId(), med);
//                    List<Medicine> list = new ArrayList<Medicine>(medicationHashMap.values());
//                    myMedicationData.setValue(list);
////                    updateFilteredCategory();
//                    isLoading.postValue(false);

                    //MedicFile medicFile = (MedicFile)snapshot.getValue(MedicFile.class);
                    MedicFile medicFile = new MedicFile();
                    medicFile.setFilePath((String)snapshot.child("filePath").getValue());
                    medicFile.setNotes((String)snapshot.child("notes").getValue());
                    medicFile.setTimeStamp((String)snapshot.child("timeStamp").getValue());
                    medicFile.setTitle((String)snapshot.child("title").getValue());
                    list.add(medicFile);
                    myMedicationData.setValue(list);
                    isLoading.postValue(false);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
    }
    private ChildEventListener setMyMedicationListener() {
        return new ChildEventListener() {

            List<MedicFile> list = new ArrayList<MedicFile>();

            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                if (snapshot.exists()) {
//                    Medicine med = snapshot.getValue(Medicine.class);
//                    medicationHashMap.put(med.getId(), med);
//                    List<Medicine> list = new ArrayList<Medicine>(medicationHashMap.values());
//                    myMedicationData.setValue(list);
////                    updateFilteredCategory();
//                    isLoading.postValue(false);

                    MedicFile medicFile = snapshot.getValue(MedicFile.class);
                    list.add(medicFile);
                    myMedicationData.setValue(list);
                    isLoading.postValue(false);
                }

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                MedicFile medicFile = new MedicFile();
                if (snapshot.exists()) {
                    medicFile = snapshot.getValue(MedicFile.class);
                    //medicationHashMap.put(med.getId(), med);

                }
                List<MedicFile> list = new ArrayList<MedicFile>();
                list.add(medicFile);
                myMedicationData.setValue(list);
//                updateFilteredCategory();
                isLoading.postValue(false);


                if (snapshot.exists()) {
//                    Medicine med = snapshot.getValue(Medicine.class);
//                    medicationHashMap.put(med.getId(), med);

//                    MedicFile medicFile = snapshot.getValue(MedicFile.class);
//                    list.add(medicFile);
//                    myMedicationData.setValue(list);
//                    isLoading.postValue(false);
                }
//                List<Medicine> list = new ArrayList<Medicine>(medicationHashMap.values());
//                myMedicationData.setValue(list);
////                updateFilteredCategory();
//                isLoading.postValue(false);
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
//                    Medicine med = snapshot.getValue(Medicine.class);
//                    medicationHashMap.remove(med.getId());

//                    MedicFile medicFile = snapshot.getValue(MedicFile.class);
//                    myMedicationData.setValue(list);
//                    isLoading.postValue(false);
                }
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
