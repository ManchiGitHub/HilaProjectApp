package com.example.parkinson.features.questionnaire;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.parkinson.R;
import com.example.parkinson.features.main.MainActivity;
import com.example.parkinson.features.main.MainViewModel;
import com.example.parkinson.model.question_models.Questionnaire;

import java.util.ArrayList;
import java.util.List;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class QuestionnaireListFragment extends Fragment {


    private QuestionnaireListViewModel questionnaireListViewModel;

    private RecyclerView recyclerView;
    private QuestionnaireListAdapter adapter;
    private List<Questionnaire> temp = new ArrayList<Questionnaire>();
    MainViewModel mainViewModel;


    //Dialogs
    private AlertDialog alertDialog;

    public QuestionnaireListFragment() {
        super(R.layout.fragment_questionnaire_list);
    }

    @Override
    public void onAttach(@NonNull Context context) {
//        ((MainActivity) getActivity()).mainComponent.inject(this);
        super.onAttach(context);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mainViewModel = new ViewModelProvider(requireActivity()).get(MainViewModel.class);

        questionnaireListViewModel = new ViewModelProvider(this).get(QuestionnaireListViewModel.class);

        questionnaireListViewModel.initQuestionnaireData();
        initViews(view);
        //initUi(view);
        initObservers();
       // initLaunchers();

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        //Collections.reverseOrder();
        adapter = new QuestionnaireListAdapter(temp);
        recyclerView.setAdapter(adapter);

        adapter.setListener(new QuestionnaireListAdapter.QuestionnaireListAdapterListener() {
            @Override
            public void onQuestionnaireClick(int position, View view) {

                Toast.makeText(view.getContext(), "HH", Toast.LENGTH_SHORT).show();
                Bundle bundle = new Bundle();
//                bundle.putString("room_key", mChatRooms.get(getAdapterPosition()).getRoomKey());
//                bundle.putString("contact_name", mChatRooms.get(getAdapterPosition()).getContactNAme());
               // Navigation.findNavController(view).navigate(R.id.action_contactFragment_to_chatFragment, bundle);

                NavDirections action = QuestionnaireListFragmentDirections.actionQuestionnaireListFragmentToQuestionnaireFragment(mainViewModel.patientEvent.getValue().getHasUnansweredQuestionnaire(),position+"");
               Navigation.findNavController(view).navigate(action);



            }
        });


        getView().findViewById(R.id.questionnaireListFragExitBtn).setOnClickListener(v -> {
            getActivity().onBackPressed();
        });

    }

    private void initViews(View view) {
        recyclerView = view.findViewById(R.id.questionnaireListFragRecycler);
//        addBtn = view.findViewById(R.id.medicCaseFragAddBtn);
      //  fabBtn = view.findViewById(R.id.medic_case_fab_btn);

    }

    private void initObservers() {
        questionnaireListViewModel.isLoading.observe(getViewLifecycleOwner(), isLoading -> {
            MainActivity mainActivity = (MainActivity) getActivity();
            mainActivity.updateLoadingScreen(isLoading);
        });
        questionnaireListViewModel.myMedicationData.observe(getViewLifecycleOwner(), questionnaireList -> {

            adapter = new QuestionnaireListAdapter(questionnaireList);

            recyclerView.setAdapter(adapter);
            adapter.setListener(new QuestionnaireListAdapter.QuestionnaireListAdapterListener() {
                @Override
                public void onQuestionnaireClick(int position, View view) {
                    Bundle bundle = new Bundle();
                    bundle.putString("key", position + "");
                    NavDirections action = QuestionnaireListFragmentDirections.actionQuestionnaireListFragmentToQuestionnaireFragment(mainViewModel.patientEvent.getValue().getHasUnansweredQuestionnaire(),position+"");
                    Navigation.findNavController(view).navigate(action);
                }
            });
        });
    }





    ImageView imageView;
    ImageButton backBtn;



    @Override
    public void onPause() {
        super.onPause();
    }


}
