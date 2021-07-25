package com.example.parkinson.data;

import androidx.annotation.NonNull;
import com.example.parkinson.features.chat.ChatRoom;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class ChatRepository {

    public interface ChatRepositoryListener {
        void onRoomsReceived(ArrayList<ChatRoom> chatRooms);
    }

    private ChatRepositoryListener listener;

    @Inject
    public ChatRepository() {

    }

    public void getChatRooms() {

        ArrayList<ChatRoom> chatRooms = new ArrayList<>();
        String userID = FirebaseAuth.getInstance().getCurrentUser().getUid();

        FirebaseDatabase.getInstance().getReference("Chats").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                // iterate through chat rooms
                for (DataSnapshot snap : snapshot.getChildren()) {

                    // get chat rooms of the current user
                    if (snap.getKey().contains(userID)) {

                        ChatRoom chatRoom = snap.getValue(ChatRoom.class);

                        chatRooms.add(chatRoom);
                    }
                }

                // done fetching the data, trigger the callback
                listener.onRoomsReceived(chatRooms);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void setListener(ChatRepositoryListener listener) {
        this.listener = listener;
    }
}
