package com.example.parkinson.features.chat;


import com.google.firebase.database.PropertyName;


// Class to hold the object received from firebase
public class ChatRoom {

    private String contactName;

    public ChatRoom() {
    }

    @PropertyName("contact")
    public String getContactNAme() {

        return this.contactName;
    }

    @PropertyName("contact")
    public void setContactNAme(String contactNAme) {
        this.contactName = contactNAme;
    }
}
