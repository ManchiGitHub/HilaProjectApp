package com.example;

import android.app.Application;
import com.google.firebase.FirebaseApp;
import dagger.hilt.android.HiltAndroidApp;

@HiltAndroidApp
public class ParkinsonApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseApp.initializeApp(this);
    }


}
