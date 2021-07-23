package com.example.parkinson.di;

import com.example.parkinson.fcm.MyFirebaseMessagingService;
import javax.inject.Singleton;
import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.components.SingletonComponent;

@Module
@InstallIn(SingletonComponent.class)
public class ServiceModule {

//    private final MyFirebaseMessagingService mService;
//
//    ServiceModule(MyFirebaseMessagingService service) {
//        mService = service;
//    }

    @Singleton
    @Provides
    public MyFirebaseMessagingService provideMyService(MyFirebaseMessagingService service) {
        return service;
    }
}
//
//@Component(modules=ServiceModule.class)
//interface MyServiceComponent {
//    void inject(MyFirebaseMessagingService service);
//}