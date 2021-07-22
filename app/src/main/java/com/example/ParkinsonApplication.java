package com.example;

import android.app.Application;

import com.example.parkinson.di.ApplicationComponent;
import com.example.parkinson.di.DaggerApplicationComponent;
import com.google.firebase.FirebaseApp;

// TODO: 23/7/2021 consider implementing dagger-hilt if moving forward is too troublesome
public class ParkinsonApplication extends Application {

    public ApplicationComponent appComponent = DaggerApplicationComponent.create();

    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseApp.initializeApp(this);
    }


}
