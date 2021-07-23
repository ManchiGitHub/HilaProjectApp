package com.example.parkinson.features.medic_case;

import android.graphics.Bitmap;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.parkinson.R;
import com.example.parkinson.features.medicine.binder.MedicineBinderEmptyList;
import com.example.parkinson.features.medicine.binder.MedicineBinderHeader;
import com.example.parkinson.features.medicine.binder.MedicineBinderMedicine;
import com.example.parkinson.features.medicine.models.CategoryEmpty;
import com.example.parkinson.model.general_models.Medicine;

import java.util.List;

import mva2.adapter.ItemSection;
import mva2.adapter.ListSection;
import mva2.adapter.MultiViewAdapter;

public class MyMedicCaseMainAdapter extends RecyclerView.Adapter<MyMedicCaseMainAdapter.MyMedidCaseViewHolder> {

    private List<String> files;
    MyMedicCaseMainAdapterListener listener;

    @NonNull
    @Override
    public MyMedidCaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.files_cardview,parent,false);
        MyMedidCaseViewHolder myMedidCaseViewHolder = new MyMedidCaseViewHolder(view);
        return myMedidCaseViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyMedidCaseViewHolder holder, int position) {
        String uriString = files.get(position).toString();
        Glide.with(holder.itemView).asBitmap().load(uriString).into(holder.imageView);


    }

    @Override
    public int getItemCount() {
        return files.size();
    }

    public interface MyMedicCaseMainAdapterListener {

        void onFileClicked(int position, View view);

    }

//    MyMedicCaseMainAdapter(MyMedicCaseMainAdapterListener listener ) {
//        this.listener = listener;
//        //init();
//    }

    public void setListener(MyMedicCaseMainAdapterListener listener)
    {
        this.listener = listener;
    }

    public MyMedicCaseMainAdapter(List<String> files) {
        this.files = files;
    }

    public class MyMedidCaseViewHolder extends RecyclerView.ViewHolder
    {
        ImageView imageView;

        public MyMedidCaseViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.file_pic);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(listener!=null)
                    {
                        listener.onFileClicked(getAdapterPosition(),view);
                    }
                }
            });


        }
    }


    //ItemSection<CategoryEmpty> emptyList = new ItemSection<>();

//    private void init() {
//        this.registerItemBinders(
//                new MedicineBinderHeader(),
//                new MedicineBinderMedicine(listener),
//                new MedicineBinderEmptyList());
//        emptyList.setItem(new CategoryEmpty());
//
//        addSection(emptyList);
//    }

//    void updateMedicineList(List<Medicine> list) {
//        this.removeAllSections();
//        if(list.isEmpty()){
//            addSection(emptyList);
//        } else {
//            ListSection<Medicine> medicationListSection = new ListSection<>();
//            medicationListSection.set(list);
//            this.addSection(medicationListSection);
//        }
//
//    }
}
