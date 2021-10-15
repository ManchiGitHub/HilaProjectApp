package com.example.parkinson.data;


import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import javax.inject.Inject;
import javax.inject.Singleton;
import static com.example.parkinson.data.enums.EDataSourceData.INDICES_LIST;
import static com.example.parkinson.data.enums.EDataSourceData.QUESTIONNAIRE_FOLLOW_UP;
import static com.example.parkinson.data.enums.EDataSourceData.QUESTIONNAIRE_NEW_PATIENT;

@Singleton
public class DataRepository {

    private final DatabaseReference dataTable;

    @Inject
    public DataRepository(/*FirebaseDatabase db*/ DatabaseReference ref) {
        dataTable = ref.child("Data");
    }

    /** Get follow up questionnaire - answered after every meeting with the doctor */
    public void getFollowUpQuestionnaire(ValueEventListener listener){
        dataTable.child(QUESTIONNAIRE_FOLLOW_UP.name).addListenerForSingleValueEvent(listener);
    }


}
