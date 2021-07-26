package com.example.parkinson.features.chat;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.parkinson.R;
import java.util.ArrayList;
import java.util.List;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class ChatFragment extends Fragment {


    //Views
    private ImageView profileIv;
    private TextView usernameTv;
    private ImageButton sendBtn;
    private EditText messageEt;

    //chat messages
    private ChatAdapter chatAdapter;
    private List<ChatMessage> chats;
    private RecyclerView recyclerView;




    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.in_chat_layout, container, false);


        //init views
        profileIv = rootView.findViewById(R.id.in_chat_profile_img);
        usernameTv = rootView.findViewById(R.id.in_chat_username);
        messageEt = rootView.findViewById(R.id.in_chat_et);
        sendBtn = rootView.findViewById(R.id.chat_send_btn);


        //init recyclerview
        recyclerView = rootView.findViewById(R.id.in_chat_recycler_view);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager manager = new LinearLayoutManager(getActivity());
        manager.setStackFromEnd(true); //show last first
        recyclerView.setLayoutManager(manager);

        return rootView;

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        String roomKEy = requireArguments().getString("room_key");
        String contactName = requireArguments().getString("contact_name");

        // make this a global variable if needed
        ChatViewModel chatViewModel = new ViewModelProvider(this).get(ChatViewModel.class);

        // call method to get all of the messages according to the provided room key
        chatViewModel.getMessagesFromDB(roomKEy);
        chatViewModel.getChatMessages().observe(getViewLifecycleOwner(), new Observer<ArrayList<ChatMessage>>() {
            @Override
            public void onChanged(ArrayList<ChatMessage> chatMessages) {


                chatAdapter  = new ChatAdapter(chatMessages); //create new adapter with loaded list
                recyclerView.setAdapter(chatAdapter);

                for (ChatMessage msg : chatMessages){

                    // the messages...
                    StringBuilder builder = new StringBuilder();
                    builder.append("\nmessage: ").
                            append(msg.getMessage()).
                            append("\nsender name: ").
                            append(msg.getSenderName()).
                            append("\nsender id: ").
                            append(msg.getSenderId()).
                            append("\ntime stamp: ").
                            append(msg.getTimestamp()).
                            append("\nis doctor: ").
                            append(msg.getIsDoctor());

                    Log.d("alih", "onChanged: " + builder.toString());
                }
            }
        });




        initializeRecyclerView(view);

    }

    private void initializeRecyclerView(View view) {
        recyclerView = view.findViewById(R.id.in_chat_recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

}