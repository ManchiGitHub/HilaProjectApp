package com.example.parkinson.features.splash;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.ParkinsonApplication;
import com.example.parkinson.R;
import com.example.parkinson.data.enums.EDataSourceData;
import com.example.parkinson.features.main.MainActivity;
import com.example.parkinson.features.on_boarding.OnBoardingActivity;
import com.example.parkinson.features.on_boarding.OnBoardingViewModel;

import javax.inject.Inject;
import javax.sql.DataSource;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class SplashActivity extends AppCompatActivity {

    SplashViewModel splashViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
//        ((ParkinsonApplication) getApplicationContext()).appComponent.inject(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        splashViewModel = new ViewModelProvider(this).get(SplashViewModel.class);

        splashViewModel.init();
        initObservers();
    }

    private void initObservers() {
        splashViewModel.navigationEvent.observe(this, new Observer<SplashViewModel.NavigationEvent>() {
            @Override
            public void onChanged(SplashViewModel.NavigationEvent navigationEvent) {
                switch (navigationEvent) {
                    case OPEN_MAIN_ACTIVITY:
                        openMainActivity();
                        break;
                    case OPEN_ON_BOARDING_ACTIVITY:
                        openOnBoardingActivity();
                        break;
                }
            }
        });
    }

    private void openMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    private void openOnBoardingActivity() {
        Intent intent = new Intent(this, OnBoardingActivity.class);
        startActivity(intent);
        finish();
    }

}