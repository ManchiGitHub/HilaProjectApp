package com.example.parkinson.model.question_models;

import com.google.firebase.database.Exclude;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Questionnaire {
    private List<Question> questionList = new ArrayList<>();
    private String questionnaireName;
    private long date_sent;

    private Long date_answered;
    private String isAnswered;

    public Questionnaire() {
    }

    public Questionnaire(List<Question> questionList, String name, long date_sent, Long date_answered) {
        this.questionList = questionList;
        this.questionnaireName = name;
        this.date_sent = date_sent;
        this.date_answered = date_answered;
        //this.date_answered = date_answered;
        this.isAnswered = isAnswered;
    }

    public Questionnaire(List<Question> questionList, String name) {
        this.questionList = questionList;
        this.questionnaireName = name;
    }

    @Exclude
    public boolean exists() {

        return (questionList != null && !questionList.isEmpty());
    }

    public String getIsAnswered() {
        return isAnswered;
    }

    public void setIsAnswered(String isAnswered) {
        this.isAnswered = isAnswered;
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

    public long getDate_sent() {
        return date_sent;
    }

    public void setDate_sent(long date_sent) {
        this.date_sent = date_sent;
    }

    public Long getDate_answered() {
        return date_answered;
    }

    public void setDate_answered(long date_answered) {
        this.date_answered = date_answered;
    }
}
