package com.example.parkinson.features.chat;

import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.parkinson.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ChatViewHolder> {

    public static final int MSG_TYPE_LEFT = 0;
    public static final int MSG_TYPE_RIGHT = 1;

    private List<ChatMessage> chats;

    FirebaseUser fUser;

    public ChatAdapter(List<ChatMessage> chats) {
        this.chats = chats;
    }

    //holds inflated views
    public class ChatViewHolder extends RecyclerView.ViewHolder {

        TextView messageTv;
        ImageView userProfileIv;
        ImageView isSeenIv;
        TextView timeTv;

        public ChatViewHolder(@NonNull View itemView) {
            super(itemView);
            //generics views (can be changes to right or left)
            messageTv = itemView.findViewById(R.id.chat_message);
            userProfileIv = itemView.findViewById(R.id.chat_item_profile);
            isSeenIv = itemView.findViewById(R.id.chat_seen_iv);
            timeTv = itemView.findViewById(R.id.chat_item_time_tv);
        }
    }

    @NonNull
    @Override
    public ChatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //Sender
        if (viewType == MSG_TYPE_RIGHT) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_item_right, parent, false);
            return new ChatViewHolder(view);
        } else { //Receiver
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_item_left, parent, false);
            return new ChatViewHolder(view);
        }

    }

    @Override
    public void onBindViewHolder(@NonNull ChatViewHolder holder, int position) {

        ChatMessage chat = chats.get(position);

        Calendar calendar = Calendar.getInstance(Locale.getDefault());
        calendar.setTimeInMillis(chat.getTimestamp());
        String chatTime = DateFormat.format("dd/MM/yyyy HH:mm", calendar).toString();

        holder.timeTv.setText(chatTime);

        holder.messageTv.setText(chat.getMessage());

//        //is seen indicator
//        if (chat.getIsSeen().equals("true")) {
//            holder.isSeenIv.setImageResource(R.drawable.blue_vv);
//        } else {
//            holder.isSeenIv.setImageResource(R.drawable.grey_vv);
//        }
    }

    @Override
    public int getItemCount() {
        return chats.size();
    }

    //to define between sender and receiver
    @Override
    public int getItemViewType(int position) {
        //fUser = FirebaseAuth.getInstance().getCurrentUser();
        if (!chats.get(position).getIsDoctor()) {
            return MSG_TYPE_LEFT; //I'm sending
        } else {
            return MSG_TYPE_RIGHT; //I'm receiving
        }
    }
}
