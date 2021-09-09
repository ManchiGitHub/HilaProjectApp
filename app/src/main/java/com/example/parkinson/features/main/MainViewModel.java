package com.example.parkinson.features.main;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.navigation.NavDirections;

import com.example.parkinson.data.DataRepository;
import com.example.parkinson.data.UserRepository;
import com.example.parkinson.model.general_models.Report;
import com.example.parkinson.model.question_models.Questionnaire;
import com.example.parkinson.model.user_models.Patient;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;


@HiltViewModel
public class MainViewModel extends ViewModel {
    private final UserRepository userRepository;
    private final DataRepository dataRepository;

    public MutableLiveData<Patient> patientEvent;
    public MutableLiveData<ArrayList<Questionnaire>> questionnaireEvent;
    MutableLiveData<Boolean> isLoading;

    List<Report> reports = new ArrayList<>();
    MutableLiveData<List<String>> messagesData = new MutableLiveData<>();
    MutableLiveData<List<Report>> reportsData = new MutableLiveData<>();

    /**
     * For navigation between fragments in main activity using NavigationComponent
     **/
    MutableLiveData<NavDirections> openFragmentEvent;

    /**
     * For navigation between activities
     **/
    MutableLiveData<OpenActivityEvent> openActivityEvent;

    public enum OpenActivityEvent {
        OPEN_ON_BOARDING_ACTIVITY
    }

    /**
     * Inject tells Dagger how to create instances of MainViewModel
     **/
    @Inject
    public MainViewModel(UserRepository userRepository, DataRepository dataRepository) {
        this.userRepository = userRepository;
        this.dataRepository = dataRepository;
        patientEvent = new MutableLiveData<>();
        questionnaireEvent = new MutableLiveData<>();
        openFragmentEvent = new MutableLiveData<>();
        openActivityEvent = new MutableLiveData<>();
        isLoading = new MutableLiveData<>();
    }

    /**
     * Getting PatientDetails from firebase
     **/
    public void initData() {
        isLoading.setValue(false);
        handleUserDetails(userRepository.getPatientDetails());
        //userRepository.getReportsList(setReportsListener());
        userRepository.getQuestionnaireListValue(setQuestionnaireListenerValue());
    }

    private void handleUserDetails(Patient patientDetails) {
        if (patientDetails!=null){
            patientEvent.postValue(patientDetails);
            List<String> messages = new ArrayList<>();
            if (patientDetails.getHasUnansweredQuestionnaire()) {
                messages.add("קיים שאלון חדש המחכה למענה");
            }
            if (patientDetails.getNeedToUpdateMedicine()) {
                messages.add("יש למלא רשימת תרופות");
            }
            if (!patientDetails.getNeedToUpdateMedicine() && !patientDetails.getHasUnansweredQuestionnaire()) {
                messages.add("אין הודעות חדשות");
            }
            messagesData.postValue(messages);
        }
    }


//    private ChildEventListener setReportsListener() {
//        return new ChildEventListener() {
//
//            @Override
//            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
//                if (snapshot.exists()) {
//                    Report report = snapshot.getValue(Report.class);
//                    reports.add(report);
//                    reportsData.postValue(reports);
//                }
//                isLoading.setValue(false);
//            }
//
//            @Override
//            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
//                isLoading.setValue(false);
//            }
//
//            @Override
//            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
//
//            }
//
//            @Override
//            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
//
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        };
//    }

    /**
     * Logging out of current user and back to on boarding activity
     **/
    public void logOut() {
        userRepository.logout();
        openActivityEvent.postValue(OpenActivityEvent.OPEN_ON_BOARDING_ACTIVITY);

    }

    private ValueEventListener setQuestionnaireListenerValue()
    {
        return new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                ArrayList<Questionnaire> list = new ArrayList<Questionnaire>();

                if (snapshot.exists()) {



                    //Log.d("GGGG",snapshot.getKey());


                    // Log.d("GGGG",ds.getKey());

                    for (DataSnapshot ds : snapshot.getChildren())
                    {
                        Questionnaire questionnaire = ds.getValue(Questionnaire.class);

                        //questionnaireEvent.postValue(questionnaire);

                        list.add(questionnaire);
                    }

                    questionnaireEvent.postValue(list);



                    //Log.d("GGGG",questionnaire.getQuestionnaireName());

                    //medicationHashMap.put(med.getId(), med);

                    //myMedicationData.setValue(list);
//                    updateFilteredCategory();
                    //isLoading.postValue(false);

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
    }

    private ChildEventListener setQuestionnaireListener() {
        return new ChildEventListener() {

            List<Questionnaire> list = new ArrayList<Questionnaire>();

            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                if (snapshot.exists()) {



                    //Log.d("GGGG",snapshot.getKey());


                    // Log.d("GGGG",ds.getKey());


                    Questionnaire questionnaire = snapshot.getValue(Questionnaire.class);

                    //questionnaireEvent.postValue(questionnaire);
                    //Log.d("GGGG",questionnaire.getQuestionnaireName());

                    //medicationHashMap.put(med.getId(), med);
                    list.add(questionnaire);
                    //myMedicationData.setValue(list);
//                    updateFilteredCategory();
                    //isLoading.postValue(false);

                }
                //isLoading.postValue(false);

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
                //myMedicationData.setValue(list);
//                updateFilteredCategory();
                //isLoading.postValue(false);
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
                //isLoading.postValue(false);
            }
        };
    }
}
