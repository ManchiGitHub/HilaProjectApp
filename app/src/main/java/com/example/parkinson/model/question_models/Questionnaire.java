package com.example.parkinson.model.question_models;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Questionnaire {
    private List<Question> questionList = new ArrayList<>();
    private String questionnaireName;

    public Questionnaire() {
    }

    public Questionnaire(List<Question> questionList ,String name) {
        this.questionList = questionList;
        this.questionnaireName = name;
    }

    public List<Question> getQuestionList() {
        return questionList;
    }

    public void setQuestionList(List<Question> questionList) {
        this.questionList = questionList;
    }


    public String getQuestionnaireName() {
        return questionnaireName;
    }

    public void setQuestionnaireName(String questionnaireName) {
        this.questionnaireName = questionnaireName;
    }
}