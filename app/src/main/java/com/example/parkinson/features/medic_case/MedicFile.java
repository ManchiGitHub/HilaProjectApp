package com.example.parkinson.features.medic_case;

import java.io.Serializable;

public class MedicFile implements Serializable {

    private String filePath;
    private String timeStamp;
    private String title;
    private String notes;

    public MedicFile(String filePath, String timeStamp) {
        this.filePath = filePath;
        this.timeStamp = timeStamp;
    }

    public MedicFile() {
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}
