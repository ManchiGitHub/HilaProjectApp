package com.example.parkinson.features.metrics.add_new_medicine.single_metric;

import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.parkinson.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class MetricsAdapter extends RecyclerView.Adapter<MetricsAdapter.MetricsViewHolder> {

    private ArrayList<SingleMetric> metricsList;

    public MetricsAdapter(ArrayList<SingleMetric> list) {
        this.metricsList = list;
    }

    @NonNull
    @Override
    public MetricsAdapter.MetricsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.metrics_cell, parent, false);

        return new MetricsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MetricsAdapter.MetricsViewHolder holder, int position) {

        //holder.contactNameTv.setText(mChatRooms.get(position).getContactNAme());

        Calendar calendar = Calendar.getInstance(Locale.getDefault());
        calendar.setTimeInMillis(metricsList.get(position).getTimeStamp());
        String date = DateFormat.format("dd/MM/yyyy HH:mm", calendar).toString();

        holder.metricValue.setText(metricsList.get(position).getValue());
        holder.timeStamp.setText(date);
    }

    @Override
    public int getItemCount() {
        if(metricsList!=null) {
            return metricsList.size();
        }
        else
            return 0;
    }


    public class MetricsViewHolder extends RecyclerView.ViewHolder {

        private TextView metricValue;
        private TextView timeStamp;

        public MetricsViewHolder(@NonNull View itemView) {
            super(itemView);

            metricValue = itemView.findViewById(R.id.metrics_value);
            timeStamp = itemView.findViewById(R.id.metrics_timestamp);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

////                    NavDirections action = ContactFragmentDirections.actionContactFragmentToChatFragment();
//                    Bundle bundle = new Bundle();
//                    bundle.putString("room_key", mChatRooms.get(getAdapterPosition()).getRoomKey());
//                    bundle.putString("contact_name", mChatRooms.get(getAdapterPosition()).getContactNAme());
//                    Navigation.findNavController(v).navigate(R.id.action_contactFragment_to_chatFragment, bundle);

                }
            });
        }
    }
}
