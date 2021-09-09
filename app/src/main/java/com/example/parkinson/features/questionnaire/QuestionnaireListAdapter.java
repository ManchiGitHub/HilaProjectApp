package com.example.parkinson.features.questionnaire;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.parkinson.R;
import com.example.parkinson.features.main.MainFragmentDirections;
import com.example.parkinson.features.medic_case.MedicFile;
import com.example.parkinson.model.question_models.Question;
import com.example.parkinson.model.question_models.Questionnaire;

import java.util.List;

public class QuestionnaireListAdapter extends RecyclerView.Adapter<QuestionnaireListAdapter.QuestionnaireListViewHolder> {

    private List<Questionnaire> questionnaires;
    QuestionnaireListAdapterListener listener;

    @NonNull
    @Override
    public QuestionnaireListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.questionnaire_cardview,parent,false);
        QuestionnaireListViewHolder myMedidCaseViewHolder = new QuestionnaireListViewHolder(view);
        return myMedidCaseViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull QuestionnaireListViewHolder holder, int position) {



//        String uriString = files.get(position).getFilePath().toString();
//        Glide.with(holder.itemView).asBitmap().load(uriString).into(holder.imageView);
//
//        holder.timeStamp.setText(files.get(position).getTimeStamp());

        holder.textView.setText(questionnaires.get(position).getQuestionnaireName());

        if(questionnaires.get(position).getDate_answered() == null)
        {
            holder.textView.setTextColor(Color.GREEN);
            //questionnaires.remove(questionnaires.get(position));
            //questionnaires.add(0,questionnaires.get(position));
            //rearrange(questionnaires,questionnaires.get(position));
            //questionnaires.add(questionnaires.size()-1,questionnaires.get(position));
        }




    }



    @Override
    public int getItemCount() {
        return questionnaires.size();
    }

    public interface QuestionnaireListAdapterListener {

        void onQuestionnaireClick(int position, View view);

    }

//    MyMedicCaseMainAdapter(MyMedicCaseMainAdapterListener listener ) {
//        this.listener = listener;
//        //init();
//    }

    public void setListener(QuestionnaireListAdapterListener listener)
    {
        this.listener = listener;
    }

    public QuestionnaireListAdapter(List<Questionnaire> files) {
        this.questionnaires = files;
    }

    public class QuestionnaireListViewHolder extends RecyclerView.ViewHolder
    {
        TextView textView;
        TextView timeStamp;

        public QuestionnaireListViewHolder(@NonNull View itemView) {
            super(itemView);

            textView = itemView.findViewById(R.id.questionnaireName);
            //timeStamp = itemView.findViewById(R.id.file_added_time_stamp);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    //Toast.makeText(view.getContext(), "gg11g", Toast.LENGTH_SHORT).show();

                    Bundle bundle = new Bundle();
                  bundle.putString("key", getAdapterPosition() + "");
//                bundle.putString("room_key", mChatRooms.get(getAdapterPosition()).getRoomKey());
//                bundle.putString("contact_name", mChatRooms.get(getAdapterPosition()).getContactNAme());
                  //Navigation.findNavController(view).navigate(R.id.action_questionnaireListFragment_to_questionnaireFragment, bundle);
                    if(listener!=null)
                    {
                        listener.onQuestionnaireClick(getAdapterPosition(),view);
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
