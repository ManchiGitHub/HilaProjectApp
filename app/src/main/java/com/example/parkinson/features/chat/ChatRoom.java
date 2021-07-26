package com.example.parkinson.features.chat;

import com.google.firebase.database.IgnoreExtraProperties;
import com.google.firebase.database.PropertyName;


// Class to hold the chat room object received from firebase
@IgnoreExtraProperties
public class ChatRoom {

    private String contactName;

    private String roomKey;

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

    public String getRoomKey() {
        return roomKey;
    }

    public void setRoomKey(String roomKey) {
        this.roomKey = roomKey;
    }

}
