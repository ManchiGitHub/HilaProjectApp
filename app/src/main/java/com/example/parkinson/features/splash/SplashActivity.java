package com.example.parkinson.features.splash;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

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
    private String roomKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
//        ((ParkinsonApplication) getApplicationContext()).appComponent.inject(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        if (getIntent().getExtras() != null) {
            for (String key : getIntent().getExtras().keySet()) {
                Object value = getIntent().getExtras().get(key);
                Log.d("alih", "onCreate: splash: key: " + key + " | value: " + value);
            }
        }

        Log.d("alih", "onCreate: splash activity");
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
        Bundle bundle = new Bundle();
        bundle.putString("room_key", roomKey);
        intent.putExtras(bundle);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

    private void openOnBoardingActivity() {
        Intent intent = new Intent(this, OnBoardingActivity.class);
        startActivity(intent);
        finish();
    }

}