package com.example.parkinson.data;


import androidx.annotation.NonNull;

import com.example.parkinson.data.enums.EDataSourceData;
import com.example.parkinson.data.enums.EDataSourceUser;
import com.example.parkinson.model.general_models.Medicine;
import com.example.parkinson.model.general_models.MedicineReport;
import com.example.parkinson.model.general_models.Report;
import com.example.parkinson.model.question_models.Questionnaire;
import com.example.parkinson.model.user_models.Patient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;


import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
final public class UserRepository {

//    private final FirebaseAuth auth;
//    private final DatabaseReference db;
    private final DatabaseReference userTable;
    private FirebaseUser firebaseUser;

    private Patient currentPatientDetails;

    // TODO: 23/7/2021 it's not a table...
    @Inject
    public UserRepository(DatabaseReference db, FirebaseAuth auth) {
//        this.authenticator = db;
        firebaseUser = auth.getCurrentUser();
        userTable = db.getDatabase().getReference("Patients");
//        this.auth = auth;
//        this.db = db;
    }

    /**
     * Get full patient details
     */
    public Patient getPatientDetails() {
        return currentPatientDetails;
    }

    public void postPatientDetails(Patient patient) {

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        userTable.child( firebaseUser.getUid()).child(EDataSourceUser.USER_DETAILS.name).setValue(patient);
    }

    public void updateUserToken(String token) {
        currentPatientDetails.setToken(token);
        userTable.child(firebaseUser.getUid()).child(EDataSourceUser.USER_DETAILS.name).setValue(currentPatientDetails);
    }

    /**
     * Get last answered questionnaire.
     * If null - new patient
     */
    public void getQuestionnaire(ValueEventListener listener) {
        userTable.child(firebaseUser.getUid()).child(EDataSourceUser.QUESTIONNAIRE.name).addListenerForSingleValueEvent(listener);
    }

    /**
     * Get answered questionnaire and update server
     */
    public void postQuestionnaire(Questionnaire questionnaire) {
        userTable.child(firebaseUser.getUid()).child(EDataSourceUser.QUESTIONNAIRE.name).setValue(questionnaire);
        userTable.child(firebaseUser.getUid()).child(EDataSourceUser.USER_DETAILS.name).child("hasUnansweredQuestionnaire").setValue(false);
    }

    public void getMedicationList(ChildEventListener listener) {
        userTable.child(firebaseUser.getUid()).child(EDataSourceData.INDICES_LIST.name).addChildEventListener(listener);
    }

    public void postMedication(Medicine medicine) {
        userTable.child(firebaseUser.getUid()).child(EDataSourceUser.USER_DETAILS.name).child("needToUpdateMedicine").setValue(false);
        userTable.child(firebaseUser.getUid()).child(EDataSourceUser.INDICES_LIST.name).child(medicine.getId()).setValue(medicine);
    }

    public void deleteMedication(Medicine medicine) {
        userTable.child(firebaseUser.getUid()).child(EDataSourceUser.INDICES_LIST.name).child(medicine.getId()).setValue(null);
    }

    public void postReport(Report report) {
        userTable.child(firebaseUser.getUid()).child(EDataSourceUser.REPORTS.name).push().setValue(report);
    }

    public void getReportsList(ChildEventListener listener) {
        userTable.child(firebaseUser.getUid()).child(EDataSourceUser.REPORTS.name).addChildEventListener(listener);
    }


    /**
     * Login to firebase with username and password
     **/
    public void login(String username, String password, OnCompleteListener listener) {
        if (!username.isEmpty() && !password.isEmpty()) {
            FirebaseAuth.getInstance().signInWithEmailAndPassword(username, password).addOnCompleteListener(listener);
//            authenticator.getAuthentication().signInWithEmailAndPassword(username, password).addOnCompleteListener(listener);
        }
    }

    /**
     * Logout of firebase
     **/
    public void logout() {
        FirebaseAuth.getInstance().signOut();
    }

    /**
     * Get current stored user
     **/
    public FirebaseUser getCurrentUser() {
        return firebaseUser;
    }

    /**
     * Fetch new user instance from firebase
     **/
    public void updateCurrentUser() {
        this.firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
    }

    public void getMedicationListNotif(ValueEventListener listener){
        userTable.child(firebaseUser.getUid()).child(EDataSourceData.INDICES_LIST.name).addValueEventListener(listener);
    }

    public void pushMedicineReport(List<MedicineReport> reportList, ValueEventListener listener) {
        userTable.child(firebaseUser.getUid()).child("Medicine Reports").addValueEventListener(listener);
    }

    InitUserListener initUserListener;
    public interface InitUserListener{
        void finishedLoadingUser();
    }

    public void initUserDetails(InitUserListener initUserListener){
        this.initUserListener = initUserListener;
        fetchPatientDetails();
    }

    public void fetchPatientDetails() {
        userTable.child(firebaseUser.getUid()).child(EDataSourceUser.USER_DETAILS.name).addListenerForSingleValueEvent(setPatientDataListener());
    }

    /**
     * Posting PatientDetails data to observer
     **/
    private ValueEventListener setPatientDataListener() {
        return new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    currentPatientDetails = dataSnapshot.getValue(Patient.class);
                    initUserListener.finishedLoadingUser();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
    }
}
