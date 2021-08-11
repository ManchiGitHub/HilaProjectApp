package com.example;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;
import androidx.lifecycle.ProcessLifecycleOwner;

import com.google.firebase.FirebaseApp;

import dagger.hilt.android.HiltAndroidApp;

@HiltAndroidApp
public class ParkinsonApplication extends Application implements LifecycleObserver {

    public static boolean IS_APP_IN_BACKGROUND = true;
    public static boolean IS_APP_DEAD;

    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseApp.initializeApp(this);
        ProcessLifecycleOwner.get().getLifecycle().addObserver(this);
    }


    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    public void onAppBackgrounded() {
        //App in background
        IS_APP_IN_BACKGROUND = true;
        Log.d("alih", "onAppBackgrounded: ");
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    public void onAppForegrounded() {
        // App in foreground
        Log.d("alih", "onAppForegrounded: ");
        IS_APP_IN_BACKGROUND = false;
        IS_APP_DEAD = false;
    }
}


