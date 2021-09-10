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
import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class QuestionnaireViewModel extends ViewModel {
    private final UserRepository userRepository;
    private final DataRepository dataRepository;
    MutableLiveData<Questionnaire> questionnaireDataEvent = new MutableLiveData<>();
    MutableLiveData<Boolean> isLoading = new MutableLiveData<>();
    String key;
    private Questionnaire _questionnaire;

    // @Inject tells Dagger how to create instances of MainViewModel
    @Inject
    public QuestionnaireViewModel(UserRepository userRepository, DataRepository dataRepository) {
        this.userRepository = userRepository;
        this.dataRepository = dataRepository;
    }

    /**
     * init fragment data
     * when isNewQuestionnaire is true - get question list from data
     * else need to update last questionnaire - get questionnaire from user
     */
    public void init(Boolean isNewQuestionnaire, String index) {
        isLoading.postValue(true);
        if (isNewQuestionnaire) {
            dataRepository.getFollowUpQuestionnaire(setQuestionnaireListener(index));
        }
        else {
            userRepository.getQuestionnaire(setQuestionnaireListener(index));
        }
    }

    /**
     * Get single question from questionnaire
     *
     * @param position is current page number
     */
    public Question getDataByPosition(int position) {
        if (_questionnaire.getQuestionList().size() > position) {
            return _questionnaire.getQuestionList().get(position);
        }
        else {
            return null;
        }
    }

    /**
     * Getting answers chosen by user and updating the questionnaire
     **/
    public void updateMultipleChoiceAnswer(int position, List<String> chosenAnswers) {
        ((MultipleChoiceQuestion) _questionnaire.getQuestionList().get(position)).setAnswers(chosenAnswers);
    }

    /**
     * Getting answer string from user and updating the questionnaire
     **/
    public void updateOpenAnswer(int position, String answer) {
        ((OpenQuestion) _questionnaire.getQuestionList().get(position)).setAnswer(answer);
    }

    private ValueEventListener setQuestionnaireListener(String index) {
        return new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()) {


                    Questionnaire q = dataSnapshot.child(index).getValue(Questionnaire.class);

                    if (q != null) {
                        if (q.exists()) {

                            List<Question> questionList = new ArrayList<>();
                            List<Question> qList = q.getQuestionList();

                            for (int i = 0; i < qList.size(); i++) {

                                EQuestionType type = qList.get(i).getType();

                                if (type == EQuestionType.OpenQuestion) {

                                    questionList.add(
                                            getOpenQuestionByIndex(dataSnapshot, index, i)
                                    );
                                }
                                else if (type == EQuestionType.MultipleChoiceQuestion) {

                                    questionList.add(
                                            getMultipleChoiceQuestionByIndex(dataSnapshot, index, i)
                                    );
                                }
                            }

                            _questionnaire = new Questionnaire(
                                    questionList,
                                    q.getQuestionnaireName(),
                                    q.getDate_sent(),
                                    System.currentTimeMillis()
                            );

                            questionnaireDataEvent.setValue(_questionnaire);
                            isLoading.postValue(false);
                        }
                    }
                    isLoading.postValue(false);
//
//                        Question question = questionnaire.getValue(Question.class);
//                        name = dataSnapshot.child(index).child("questionnaireName").getValue(String.class);
//
//
////                        date_sent = Long.parseLong(dataSnapshot.child(index).child("date_answered").getValue(String.class));
//                        DataSnapshot dsp = dataSnapshot.getValue(DataSnapshot.class);
//
//
//                        date_received = dataSnapshot.child(index).child("date_sent").getValue(long.class);
//                        if (question.getType().equals(EQuestionType.MultipleChoiceQuestion)) {
//                            question = questionnaire.getValue(MultipleChoiceQuestion.class);
//
//                        }
//                        else {
//
//                            if (question.getType().equals(EQuestionType.OpenQuestion)) {
//                                question = questionnaire.getValue(OpenQuestion.class);
//                            }
//                        }
//                        questionList.add(question);
//                    }
//
//
////                        for (DataSnapshot questionnaire : dataSnapshot.child(ds.getKey()).getChildren())
////                        {
////                        Questionnaire questionnaire1 = questionnaire.getValue(Questionnaire.class);
////                        name = questionnaire1.getQuestionnaireName();
////
////
////                        }
//
//                    questionnaire = new Questionnaire(questionList, name, date_sent, date_received, System.currentTimeMillis() + "");
//                    questionnaireDataEvent.setValue(questionnaire);
//                    isLoading.postValue(false);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                isLoading.postValue(false);
            }
        };
    }

    private MultipleChoiceQuestion getMultipleChoiceQuestionByIndex(DataSnapshot dataSnapshot, String questionnairePos, int i) {

        return dataSnapshot.
                child(questionnairePos).
                child("questionList").
                child(i + "").getValue(MultipleChoiceQuestion.class);
    }

    private OpenQuestion getOpenQuestionByIndex(DataSnapshot dataSnapshot, String questionnairePos, int i) {

        return dataSnapshot.
                child(questionnairePos).
                child("questionList").
                child(i + "").getValue(OpenQuestion.class);
    }


    public void onFinishClick(String index) {
        userRepository.postQuestionnaire(_questionnaire, index);
    }
}
