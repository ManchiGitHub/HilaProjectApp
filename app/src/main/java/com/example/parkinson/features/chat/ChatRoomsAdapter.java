package com.example.parkinson.features.chat;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.parkinson.R;

import java.util.ArrayList;

public class ChatRoomsAdapter extends RecyclerView.Adapter<ChatRoomsAdapter.ChatRoomsViewHolder> {

    private ArrayList<ChatRoom> mChatRooms;

    public ChatRoomsAdapter(ArrayList<ChatRoom> list) {
        this.mChatRooms = list;
    }

    @NonNull
    @Override
    public ChatRoomsAdapter.ChatRoomsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.contact_cell, parent, false);

        return new ChatRoomsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatRoomsAdapter.ChatRoomsViewHolder holder, int position) {

        holder.contactNameTv.setText(mChatRooms.get(position).getContactNAme());
    }

    @Override
    public int getItemCount() {
        return mChatRooms.size();
    }


    public class ChatRoomsViewHolder extends RecyclerView.ViewHolder {

        private TextView contactNameTv;
        private TextView timeStamp;

        public ChatRoomsViewHolder(@NonNull View itemView) {
            super(itemView);

            contactNameTv = itemView.findViewById(R.id.contact_name_tv);
            timeStamp = itemView.findViewById(R.id.chat_cell_time_tv);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
        }
    }
}
