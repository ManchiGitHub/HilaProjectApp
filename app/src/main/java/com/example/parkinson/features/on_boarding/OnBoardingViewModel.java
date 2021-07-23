package com.example.parkinson.features.on_boarding;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.example.parkinson.data.UserRepository;
import javax.inject.Inject;
import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class OnBoardingViewModel extends ViewModel {
    private final UserRepository userRepository;

    MutableLiveData<NavigationEvent> navigationEvent;

    enum NavigationEvent{
        OPEN_ON_MAIN_ACTIVITY
    }
    // @Inject tells Dagger how to create instances of MainViewModel
    @Inject
    public OnBoardingViewModel(UserRepository userRepository) {
        this.userRepository = userRepository;
        navigationEvent = new MutableLiveData<>();
    }

    public void openMainActivity() {
        navigationEvent.postValue(NavigationEvent.OPEN_ON_MAIN_ACTIVITY);
    }
}
