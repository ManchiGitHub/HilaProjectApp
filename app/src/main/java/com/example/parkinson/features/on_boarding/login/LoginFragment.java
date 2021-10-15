package com.example.parkinson.features.on_boarding.login;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.ParkinsonApplication;
import com.example.parkinson.R;
import com.example.parkinson.features.main.MainActivity;
import com.example.parkinson.features.main.MainViewModel;
import com.example.parkinson.features.medic_case.MedicFile;
import com.example.parkinson.features.on_boarding.OnBoardingActivity;
import com.example.parkinson.features.on_boarding.OnBoardingViewModel;
import com.example.parkinson.model.enums.EClinics;
import com.example.parkinson.model.user_models.Patient;
import com.google.android.material.textfield.TextInputEditText;

import java.sql.Date;
import java.util.Objects;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;

import static com.example.parkinson.features.main.MainActivity.files;

@AndroidEntryPoint
public class LoginFragment extends Fragment {

    private LoginViewModel loginViewModel;

    private OnBoardingViewModel onBoardingViewModel;

    private TextInputEditText userName;
    private TextInputEditText password;
    private TextView login;

    public LoginFragment() {
        super(R.layout.fragment_login2);
    }

    @Override
    public void onAttach(@NonNull Context context) {
//        ((OnBoardingActivity) getActivity()).onBoardingComponent.inject(this);
        super.onAttach(context);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        loginViewModel = new ViewModelProvider(this).get(LoginViewModel.class);
        onBoardingViewModel = new ViewModelProvider(requireActivity()).get(OnBoardingViewModel.class);



        initUi(view);
        initObservers();

        SharedPreferences pref = getActivity().getSharedPreferences("prefs", Context.MODE_PRIVATE);
        boolean isAnswered = pref.getBoolean("isAnswered", false);
        if(!isAnswered)
        {
            createCustomDialog();
        }
    }

    private void initUi(View view) {
        userName = view.findViewById(R.id.loginFragUserName);
        password = view.findViewById(R.id.loginFragPassword);
        login = view.findViewById(R.id.loginFragLoginBtn);


        login.setOnClickListener(v -> loginViewModel.onLoginClick());

        userName.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }
            @Override
            public void afterTextChanged(Editable s) {
                String email = s.toString();
                loginViewModel.setEmail(email);
            }
        });

        password.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }
            @Override
            public void afterTextChanged(Editable s) {
                String email = s.toString();
                loginViewModel.setPassword(email);
            }
        });
    }

    private void initObservers() {
//        loginViewModel.nextButtonState.observe(getViewLifecycleOwner(), new Observer<LoginViewModel.NextButtonState>() {
//            @Override
//            public void onChanged(LoginViewModel.NextButtonState nextButtonState) {
//                switch (nextButtonState) {
//                    case ENABLE:
//
//                    case DISABLE:
//                }
//            }
//        });
        loginViewModel.loginEvent.observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean successful) {
                if (successful) {
                    onBoardingViewModel.openMainActivity();
                }
            }
        });
        loginViewModel.errorEvent.observe(getViewLifecycleOwner(), new Observer<LoginViewModel.ErrorEvent>() {
            @Override
            public void onChanged(LoginViewModel.ErrorEvent errorEvent) {
                switch (errorEvent){
                    case LOGIN_FAIL:
                        Toast.makeText(requireContext(),"Login fail",Toast.LENGTH_SHORT).show();
                        break;
                    case UN_VALID_EMAIL:
                        Toast.makeText(requireContext(),"Un valid Email",Toast.LENGTH_SHORT).show();
                        break;
                    case UN_VALID_PASSWORD:
                        Toast.makeText(requireContext(),"Un valid password",Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        });
    }

    private void createCustomDialog() {

        final AlertDialog alertDialog;
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_start_layout, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setCancelable(false);
        alertDialog = builder.setView(dialogView).show();
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        SharedPreferences pref = getActivity().getSharedPreferences("prefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();




        //ImageView iconIv = dialogView.findViewById(R.id.profile_dialog_icon);
        final Button okBtn = dialogView.findViewById(R.id.profile_dialog_btn);
        final Button cancelBtn = dialogView.findViewById(R.id.profile_dialog_btn1);




        okBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                editor.putBoolean("isAnswered", true);
                editor.apply();

                alertDialog.dismiss();


            }
        });

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editor.putBoolean("isAnswered", false);
                editor.apply();
                alertDialog.dismiss();
                getActivity().finish();
            }
        });
    }

}
