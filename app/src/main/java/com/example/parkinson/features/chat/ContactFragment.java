package com.example.parkinson.features.chat;

import android.os.Bundle;
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
import com.google.firebase.database.DatabaseReference;
import java.util.ArrayList;
import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class ContactFragment extends Fragment {

    public interface OnChatClickListener {
        void onChatClicked(String userID);
    }

    private RecyclerView recyclerView;
    private ChatRoomsAdapter chatRoomsAdapter;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_contacts, container, false);


    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initializeRecyclerView(view);



        // initialize chat viewmodel and observe the chat list
        ChatViewModel chatViewModel = new ViewModelProvider(requireActivity()).get(ChatViewModel.class);
        chatViewModel.getChatRooms().observe(getViewLifecycleOwner(), new Observer<ArrayList<ChatRoom>>() {
            @Override
            public void onChanged(ArrayList<ChatRoom> chatRooms) {
                chatRoomsAdapter = new ChatRoomsAdapter(chatRooms);
                recyclerView.setAdapter(chatRoomsAdapter);
                chatRoomsAdapter.notifyDataSetChanged();
            }
        });

        getView().findViewById(R.id.contactFragExitBtn).setOnClickListener(v -> {
            getActivity().onBackPressed();
        });

//        loadChatRooms();

    }

    private void initializeRecyclerView(View view) {
        recyclerView = view.findViewById(R.id.contacts_recycler);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));


    }

//
//    //pulling all the users id's that i chatted with and then create user list with recyclerview of those users in the chats page
////    private void loadChatRooms() {
////
////        FirebaseDatabase.getInstance().getReference("Chats").addValueEventListener(new ValueEventListener() {
////            @Override
////            public void onDataChange(@NonNull DataSnapshot snapshot) {
////                ArrayList<ChatRoom> chatRooms = new ArrayList<>();
////
////                for (DataSnapshot snap : snapshot.getChildren()){
////
////                    if (snap.getKey().contains(FirebaseAuth.getInstance().getCurrentUser().getUid())){
////                        String[] strings = snap.getKey().split("_");
////                        String key = strings[1];
////
////                        Map<String, ChatRoom> map = (Map<String, ChatRoom>) snap.getValue();
////                        Log.d("alih", "onDataChange: " + map);
////
////                    }
////
////                }
////
////                Log.d("alih", "onDataChange: " + chatRooms);
////            }
////
////            @Override
////            public void onCancelled(@NonNull DatabaseError error) {
////
////            }
////        });
////
////
//////        FirebaseDatabase.getInstance().getReference("Chats").addValueEventListener(new ValueEventListener() {
//////            @Override
//////            public void onDataChange(@NonNull DataSnapshot snapshot) {
//////                Log.d("alih", "onDataChange: " + snapshot);
//////            }
//////
//////            @Override
//////            public void onCancelled(@NonNull  DatabaseError error) {
//////
//////            }
//////        });
////
//////        progressBar.setVisibility(View.VISIBLE);
//////        databaseReference = FirebaseDatabase.getInstance().getReference("chatList").child(fUser.getUid());
//////        databaseReference.addValueEventListener(new ValueEventListener() {
//////            @Override
//////            public void onDataChange(@NonNull DataSnapshot snapshot) {
//////
//////                allChatsList.clear();
//////                for (DataSnapshot ds : snapshot.getChildren()) {
//////                    ChatList chatList = ds.getValue(ChatList.class);
//////                    allChatsList.add(chatList);
//////                }
//////                // updateUserWithTimeStamp();
//////                createChatList(); //create users list with recyclerview
//////            }
//////
//////            @Override
//////            public void onCancelled(@NonNull DatabaseError error) {
//////
//////            }
//////        });
////
////    }
//
////    private void updateUserWithTimeStamp() {
////        users = new ArrayList<>();
////        databaseReference = FirebaseDatabase.getInstance().getReference("users").child(fUser.getUid());
////
////        String timeStamp = String.valueOf(System.currentTimeMillis());
////
////        databaseReference.child("timeStamp").setValue(timeStamp).addOnSuccessListener(new OnSuccessListener<Void>() {
////            @Override
////            public void onSuccess(Void aVoid) {
////
////
////            }
////        });
////
////    }
//
//    private void createChatList() {
////        users = new ArrayList<>();
//        databaseReference = FirebaseDatabase.getInstance().getReference("users");
////        databaseReference.addValueEventListener(new ValueEventListener() {
////            @Override
////            public void onDataChange(@NonNull DataSnapshot snapshot) {
////
//////                users.clear();
////                for (DataSnapshot ds : snapshot.getChildren()) {
////                    User user = ds.getValue(User.class);
////                    for (ChatList chatList : allChatsList) {
////                        if (user.getId().equals(chatList.getId())) {
////                            users.add(user);
////                        }
////                    }
////                }
////
////                Collections.sort(users);
////                userAdapter = new ChatUsersAdapter(users, getActivity());
////                userAdapter.setMyChatUserListener(ChatsFragment.this);
////                recyclerView.setAdapter(userAdapter);
////                progressBar.setVisibility(View.GONE);
////
////                if (users.isEmpty()) {
////                    noChatsTv.setVisibility(View.VISIBLE);
////                } else {
////                    noChatsTv.setVisibility(View.GONE);
////                }
////            }
////
////            @Override
////            public void onCancelled(@NonNull DatabaseError error) {
////                progressBar.setVisibility(View.GONE);
////            }
////        });
//    }
//
////    @Override
////    public void onChatUserClicked(int pos, View v) {
////        listener.onChatClicked(users.get(pos).getId());
////    }
}
