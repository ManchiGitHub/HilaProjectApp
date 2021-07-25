package com.example.parkinson.features.chat;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.example.parkinson.data.ChatRepository;
import java.util.ArrayList;
import javax.inject.Inject;
import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class ChatViewModel extends ViewModel {

    private final MutableLiveData<ArrayList<ChatRoom>> mChatRooms = new MutableLiveData<>();

    @Inject
    public ChatViewModel(ChatRepository repository) {
        repository.getChatRooms();
        repository.setListener(new ChatRepository.ChatRepositoryListener() {
            @Override
            public void onRoomsReceived(ArrayList<ChatRoom> chatRooms) {
                mChatRooms.setValue(chatRooms);
            }
        });
    }

    public LiveData<ArrayList<ChatRoom>> getChatRooms(){
        return this.mChatRooms;
    }
}
