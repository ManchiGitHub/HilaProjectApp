package com.example.parkinson.features.metrics.binder;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.parkinson.R;
import com.example.parkinson.model.general_models.Medicine;

import mva2.adapter.ItemBinder;
import mva2.adapter.ItemViewHolder;

public class MetricBinderMetric extends ItemBinder<Medicine, MetricBinderMetric.ViewHolder> {

    Context context;

    MedicineBinderMedicineListener listener;

    public interface MedicineBinderMedicineListener {
        void onMedicineClick(Medicine medicine);
    }

    public MetricBinderMetric(MedicineBinderMedicineListener listener, Context context) {
        this.listener = listener;
        this.context = context;
    }

    @Override
    public ViewHolder createViewHolder(ViewGroup parent) {
        return new ViewHolder(inflate(parent, R.layout.item_medicine));
    }

    @Override
    public boolean canBindData(Object item) {
        return item instanceof Medicine;
    }

    @Override
    public void bindViewHolder(ViewHolder holder, Medicine item) {
        holder.name.setText(item.getName());
        holder.dosage.setText(this.context.getString(R.string.last_index) + item.getValue());
        holder.itemView.setOnClickListener(v -> {
            listener.onMedicineClick(item);
        });

//        holder.dosage.setText(item.dosageString() + ", ");

//        if (item.getHoursArr().size() == 1) {
//            holder.dosage.setText(holder.dosage.getText() + "פעם אחת ביום");
//        } else {
//            holder.dosage.setText(holder.dosage.getText() + "" + item.getHoursArr().size() + " פעמים ביום");
//        }
    }

    static class ViewHolder extends ItemViewHolder<Medicine> {
        TextView name;
        TextView dosage;

        public ViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.itemMedicineName);
            dosage = itemView.findViewById(R.id.itemMedicineDosage);
        }
    }
}
