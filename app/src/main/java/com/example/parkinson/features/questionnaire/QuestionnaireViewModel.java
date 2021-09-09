package com.example.parkinson.features.questionnaire;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.example.parkinson.data.DataRepository;
import com.example.parkinson.data.UserRepository;
import com.example.parkinson.model.enums.EQuestionType;
import com.example.parkinson.model.question_models.MultipleChoiceQuestion;
import com.example.parkinson.model.question_models.OpenQuestion;
import com.example.parkinson.model.question_models.Question;
import com.example.parkinson.model.question_models.Questionnaire;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import javax.inject.Inject;
import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class QuestionnaireViewModel extends ViewModel {
    private final UserRepository userRepository;
    private final DataRepository dataRepository;


    private Questionnaire questionnaire;
    MutableLiveData<Questionnaire> questionnaireDataEvent = new MutableLiveData<>();
    MutableLiveData<Boolean> isLoading  = new MutableLiveData<>();

    String key;

    // @Inject tells Dagger how to create instances of MainViewModel
    @Inject
    public QuestionnaireViewModel(UserRepository userRepository, DataRepository dataRepository) {
        this.userRepository = userRepository;
        this.dataRepository = dataRepository;
    }

    /** init fragment data
     * when isNewQuestionnaire is true - get question list from data
     * else need to update last questionnaire - get questionnaire from user
      */
    public void init(Boolean isNewQuestionnaire, String index) {
        isLoading.postValue(true);
        if(isNewQuestionnaire){
            dataRepository.getFollowUpQuestionnaire(setQuestionnaireListener(index));
        } else {
            userRepository.getQuestionnaire(setQuestionnaireListener(index));
        }
    }

    /** Get single question from questionnaire
     * @param position is current page number
     */
    public Question getDataByPosition(int position) {
        if (questionnaire.getQuestionList().size() > position) {
            return questionnaire.getQuestionList().get(position);
        } else {
            return null;
        }
    }

    /** Getting answers chosen by user and updating the questionnaire**/
    public void updateMultipleChoiceAnswer(int position, List<String> chosenAnswers) {
        ((MultipleChoiceQuestion) questionnaire.getQuestionList().get(position)).setAnswers(chosenAnswers);
    }

    /** Getting answer string from user and updating the questionnaire**/
    public void updateOpenAnswer(int position, String answer) {
        ((OpenQuestion) questionnaire.getQuestionList().get(position)).setAnswer(answer);
    }

    private ValueEventListener setQuestionnaireListener(String index) {
        return new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    Log.d("GGGG",dataSnapshot.getKey());
                    List<Question> questionList = new ArrayList<>();
                    String name = "";
                    long date_sent = 0;

                        for (DataSnapshot questionnaire : dataSnapshot.child(index).child("questionList").getChildren()) {


                            Question question = questionnaire.getValue(Question.class);
                            name = dataSnapshot.child(index).child("questionnaireName").getValue(String.class);
                            date_sent = dataSnapshot.child(index).child("date_sent").getValue(long.class);
                            if (question.getType().equals(EQuestionType.MultipleChoiceQuestion)) {
                                question = questionnaire.getValue(MultipleChoiceQuestion.class);

                            } else {

                                if (question.getType().equals(EQuestionType.OpenQuestion)) {
                                    question = questionnaire.getValue(OpenQuestion.class);
                                }
                            }
                            questionList.add(question);
                        }


//                        for (DataSnapshot questionnaire : dataSnapshot.child(ds.getKey()).getChildren())
//                        {
//                        Questionnaire questionnaire1 = questionnaire.getValue(Questionnaire.class);
//                        name = questionnaire1.getQuestionnaireName();
//
//
//                        }

                    questionnaire = new Questionnaire(questionList,name,date_sent,System.currentTimeMillis() + "");
                    questionnaireDataEvent.setValue(questionnaire);
                    isLoading.postValue(false);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                isLoading.postValue(false);
            }
        };
    }

    public void onFinishClick(String index) {
        userRepository.postQuestionnaire(questionnaire,index);
    }
}
