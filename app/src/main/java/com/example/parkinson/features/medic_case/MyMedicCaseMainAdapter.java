package com.example.parkinson.features.medic_case;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.example.parkinson.R;

import java.util.List;

public class MyMedicCaseMainAdapter extends RecyclerView.Adapter<MyMedicCaseMainAdapter.MyMedidCaseViewHolder> {

    private List<MedicFile> files;
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



        String uriString = files.get(position).getFilePath().toString();
        //Glide.with(holder.itemView).asBitmap().load(uriString).into(holder.imageView);
        Glide.with(holder.itemView).asBitmap().load(uriString).circleCrop().into(holder.imageView);




        holder.timeStamp.setText(files.get(position).getTimeStamp());
        holder.title.setText(files.get(position).getTitle());




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

    public MyMedicCaseMainAdapter(List<MedicFile> files) {
        this.files = files;
    }

    public void updateList(List<MedicFile> list)
    {
        this.files = list;
    }

    public class MyMedidCaseViewHolder extends RecyclerView.ViewHolder
    {
        ImageView imageView;
        TextView timeStamp;
        TextView title;

        public MyMedidCaseViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.file_pic);
            timeStamp = itemView.findViewById(R.id.file_added_time_stamp);
            title = itemView.findViewById(R.id.file_added_title);

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
