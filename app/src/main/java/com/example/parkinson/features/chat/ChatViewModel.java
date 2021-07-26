package com.example.parkinson.features.chat;

import android.util.Log;
import android.widget.Toast;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.parkinson.data.ChatRepository;
import com.google.firebase.database.DataSnapshot;

import java.util.ArrayList;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class ChatViewModel extends ViewModel {

    private final ChatRepository chatRepository;

    private final MutableLiveData<ArrayList<ChatRoom>> mChatRooms = new MutableLiveData<>();
    private final MutableLiveData<ArrayList<ChatMessage>> mMessages = new MutableLiveData<>();

    @Inject
    public ChatViewModel(ChatRepository repository) {

        this.chatRepository = repository;

        chatRepository.getChatRooms();
        chatRepository.setListener(new ChatRepository.ChatRepositoryListener() {
            @Override
            public void onRoomsReceived(ArrayList<ChatRoom> chatRooms) {
                mChatRooms.setValue(chatRooms);
            }

            @Override
            public void onRoomOpen(DataSnapshot result) {

                ArrayList<ChatMessage> messages = new ArrayList<>();

                for (DataSnapshot snap : result.getChildren()) {



                    // exclude the 'contact' field in the database, and only get the messages
                    if (!(snap.getValue() instanceof String)) {

                        //Log.d("llop", snap.getValue() + "");
                        ChatMessage message = snap.getValue(ChatMessage.class);
                        messages.add(message);
                    }
                }

                mMessages.setValue(messages);
            }
        });
    }

    public LiveData<ArrayList<ChatRoom>> getChatRooms() {
        return this.mChatRooms;
    }

    public LiveData<ArrayList<ChatMessage>> getChatMessages() {
        return this.mMessages;
    }

    public void getMessagesFromDB(String roomKey) {
        chatRepository.loadMessages(roomKey);
    }
}
