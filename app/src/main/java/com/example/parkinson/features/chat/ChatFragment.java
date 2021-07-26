package com.example.parkinson.features.chat;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.parkinson.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class ChatFragment extends Fragment {

    public interface OnChatClickListener {
        void onChatClicked(String userID);
    }

    private OnChatClickListener listener;
    private RecyclerView recyclerView;
    private ChatRoomsAdapter chatRoomsAdapter;
    private String mRoomKey;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        return inflater.inflate(R.layout.in_chat_layout, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mRoomKey = requireArguments().getString("room_key");
        String contactName = requireArguments().getString("contact_name");

        // make this a global variable if needed
        ChatViewModel chatViewModel = new ViewModelProvider(this).get(ChatViewModel.class);

        // call method to start getting all of the messages according to the provided room key
        chatViewModel.getMessagesFromDB(mRoomKey);

        /*
        * 2 approaches to get all messages from the database:
        * after calling 'getMessagesFromDB, you can either get all of them in an array,
        * or get all of them, but one by one .
        */

        // get all existing messages in here.
        chatViewModel.getChatMessages().observe(getViewLifecycleOwner(), new Observer<ArrayList<ChatMessage>>() {
            @Override
            public void onChanged(ArrayList<ChatMessage> chatMessages) {

                // get all messages from here
            }
        });

        // new messages (of both sides) will be here
        chatViewModel.getChatMessage().observe(getViewLifecycleOwner(), new Observer<ChatMessage>() {
            @Override
            public void onChanged(ChatMessage message) {

                // get a new message from here
            }
        });

        initializeRecyclerView(view);

        // temporary click listener
        view.findViewById(R.id.chat_send_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessage();
            }
        });

    }

    private void sendMessage(){

        String userID = FirebaseAuth.getInstance().getCurrentUser().getUid();

        // create a 'ChatMessage' object
        ChatMessage message = new ChatMessage();

        // get the new message
        message.setMessage("some new message");

        // can be empty for now
        message.setSenderName(" ");

        message.setSenderId(userID);
        message.setDoctor(false);
        message.setTimestamp(System.currentTimeMillis());

        String timeStampKey = String.valueOf(message.getTimestamp());

        FirebaseDatabase.
                getInstance().
                getReference().
                child("Chats").
                child(mRoomKey).
                child(timeStampKey).setValue(message);
    }

    private void initializeRecyclerView(View view) {
        recyclerView = view.findViewById(R.id.in_chat_recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

}