package com.example.parkinson.features.on_boarding;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import android.content.Intent;
import android.os.Bundle;
import com.example.parkinson.R;
import com.example.parkinson.features.main.MainActivity;
import com.google.firebase.FirebaseApp;
import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class OnBoardingActivity extends AppCompatActivity {

//    public OnBoardingComponent onBoardingComponent;


    private OnBoardingViewModel onBoardingViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
//        onBoardingComponent = ((ParkinsonApplication) getApplicationContext())
//                .appComponent.onBoardingComponent().create();
//        onBoardingComponent.inject(this);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_on_boarding);

        FirebaseApp.initializeApp(this);

        onBoardingViewModel = new ViewModelProvider(this).get(OnBoardingViewModel.class);

        initObservers();
    }

    private void initObservers() {
        onBoardingViewModel.navigationEvent.observe(this, new Observer<OnBoardingViewModel.NavigationEvent>() {
            @Override
            public void onChanged(OnBoardingViewModel.NavigationEvent navigationEvent) {
                switch (navigationEvent) {
                    case OPEN_ON_MAIN_ACTIVITY:
                        openMainActivity();
                        break;
                }
            }
        });

    }


    public void openMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

}