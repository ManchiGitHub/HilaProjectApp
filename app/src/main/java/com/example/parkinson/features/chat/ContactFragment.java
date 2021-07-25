package com.example.parkinson.features.chat;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.parkinson.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ContactFragment extends Fragment {
    public interface OnChatClickListener {
        void onChatClicked(String userID);
    }

    private OnChatClickListener listener;

    //List
    private RecyclerView recyclerView;
//    private ChatUsersAdapter userAdapter;
    private ChatRoomsAdapter chatRoomsAdapter;


    //firebase
    private DatabaseReference databaseReference;


    //MainActivity must implement first
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            listener = (OnChatClickListener) context;
        } catch (ClassCastException ex) {
            throw new ClassCastException("The Activity must implement OnChatClickListener interface");
        }
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        ((AppCompatActivity) getActivity()).getSupportActionBar().show();

        View rootView = inflater.inflate(R.layout.fragment_contacts, container, false);
//        progressBar = rootView.findViewById(R.id.chat_fragment_progress_bar);
//        noChatsTv = rootView.findViewById(R.id.no_chats_yet);

        //init recyclerview
        recyclerView = rootView.findViewById(R.id.contacts_recycler);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        //init friends list
//        allChatsList = new ArrayList<>();
        //load all users with whom i chatted
        loadAllMyChats();

        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull  View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    //pulling all the users id's that i chatted with and then create user list with recyclerview of those users in the chats page
    private void loadAllMyChats() {

        String userID = FirebaseAuth.getInstance().getCurrentUser().getUid();

        FirebaseDatabase.getInstance().getReference("Chats").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Log.d("alih", "onDataChange: " + snapshot);
            }

            @Override
            public void onCancelled(@NonNull  DatabaseError error) {

            }
        });

//        progressBar.setVisibility(View.VISIBLE);
//        databaseReference = FirebaseDatabase.getInstance().getReference("chatList").child(fUser.getUid());
//        databaseReference.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//
//                allChatsList.clear();
//                for (DataSnapshot ds : snapshot.getChildren()) {
//                    ChatList chatList = ds.getValue(ChatList.class);
//                    allChatsList.add(chatList);
//                }
//                // updateUserWithTimeStamp();
//                createChatList(); //create users list with recyclerview
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });

    }

//    private void updateUserWithTimeStamp() {
//        users = new ArrayList<>();
//        databaseReference = FirebaseDatabase.getInstance().getReference("users").child(fUser.getUid());
//
//        String timeStamp = String.valueOf(System.currentTimeMillis());
//
//        databaseReference.child("timeStamp").setValue(timeStamp).addOnSuccessListener(new OnSuccessListener<Void>() {
//            @Override
//            public void onSuccess(Void aVoid) {
//
//
//            }
//        });
//
//    }

    private void createChatList() {
//        users = new ArrayList<>();
        databaseReference = FirebaseDatabase.getInstance().getReference("users");
//        databaseReference.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//
////                users.clear();
//                for (DataSnapshot ds : snapshot.getChildren()) {
//                    User user = ds.getValue(User.class);
//                    for (ChatList chatList : allChatsList) {
//                        if (user.getId().equals(chatList.getId())) {
//                            users.add(user);
//                        }
//                    }
//                }
//
//                Collections.sort(users);
//                userAdapter = new ChatUsersAdapter(users, getActivity());
//                userAdapter.setMyChatUserListener(ChatsFragment.this);
//                recyclerView.setAdapter(userAdapter);
//                progressBar.setVisibility(View.GONE);
//
//                if (users.isEmpty()) {
//                    noChatsTv.setVisibility(View.VISIBLE);
//                } else {
//                    noChatsTv.setVisibility(View.GONE);
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//                progressBar.setVisibility(View.GONE);
//            }
//        });
    }

//    @Override
//    public void onChatUserClicked(int pos, View v) {
//        listener.onChatClicked(users.get(pos).getId());
//    }
}
