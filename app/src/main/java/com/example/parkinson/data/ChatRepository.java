package com.example.parkinson.data;

import android.util.Log;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.parkinson.features.chat.ChatMessage;
import com.example.parkinson.features.chat.ChatRoom;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.Objects;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class ChatRepository {

    public interface ChatRepositoryListener {
        void onRoomsReceived(ArrayList<ChatRoom> chatRooms);

        void onRoomOpen(DataSnapshot result);

        void onNewMessage(ChatMessage message);
    }

    private ChatRepositoryListener listener;

    @Inject
    public ChatRepository() {

    }

    /**
     * Call this function to get all the messages in a room.
     * <br> The key consists of the patient's id and the doctor's id:
     * <b>{patientID}_{doctorID}</b>
     * @param roomKey The room id.
     */
    public void loadMessages(String roomKey){

        FirebaseDatabase.getInstance().getReference().child("Chats").child(roomKey).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (task.isSuccessful()) {
                    listener.onRoomOpen(task.getResult());
                }
                else{
                    Log.d("TAG", "onComplete: failed " + task.getException().toString());
                }
            }
        });


        FirebaseDatabase.getInstance().getReference().child("Chats").child(roomKey).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                if (!(snapshot.getValue() instanceof String)){
                    ChatMessage message = snapshot.getValue(ChatMessage.class);
//                    Log.d("alih", "onChildAdded: " + snapshot.getValue());
                    listener.onNewMessage(message);
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

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
                    if (Objects.requireNonNull(snap.getKey()).contains(userID)) {

                        // get the room
                        ChatRoom chatRoom = snap.getValue(ChatRoom.class);

                        // add the room key
                        Objects.requireNonNull(chatRoom).setRoomKey(snap.getKey());

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
