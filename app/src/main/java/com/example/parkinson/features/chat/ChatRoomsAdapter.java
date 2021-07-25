package com.example.parkinson.features.chat;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.parkinson.R;

import java.util.List;


public class ChatRoomsAdapter extends RecyclerView.Adapter<ChatRoomsAdapter.ChatRoomsViewHolder> {

    @NonNull
    @Override
    public ChatRoomsAdapter.ChatRoomsViewHolder onCreateViewHolder(@NonNull  ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.files_cardview,parent,false);
        ChatRoomsViewHolder chatRoomsViewHolder = new ChatRoomsViewHolder(view);

        return chatRoomsViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ChatRoomsAdapter.ChatRoomsViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }


    public class ChatRoomsViewHolder extends RecyclerView.ViewHolder{

        public ChatRoomsViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
