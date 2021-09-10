package com.example.parkinson.features.metrics.add_new_medicine.single_metric;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.parkinson.data.DataRepository;
import com.example.parkinson.data.UserRepository;
import com.example.parkinson.model.general_models.Medicine;
import com.example.parkinson.model.general_models.Time;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class SingleMetricViewModel extends ViewModel {
    private final UserRepository userRepository;
    private final DataRepository dataRepository;

    MutableLiveData<Boolean> isLoading = new MutableLiveData<>();
    MutableLiveData<Medicine> medicineData = new MutableLiveData<>();

    // @Inject tells Dagger how to create instances of MainViewModel
    @Inject
    public SingleMetricViewModel(UserRepository userRepository, DataRepository dataRepository) {
        this.userRepository = userRepository;
        this.dataRepository = dataRepository;
    }

    public void initData(Medicine medicine) {
        List<Time> hoursArr = new ArrayList<>();
//        if(medicine.getHoursArr() != null){
//            for(Time time : medicine.getHoursArr()){
//                Time newTime = new Time(time.getMinutes(),time.getHour());
//                hoursArr.add(newTime);
//            }
//        }

        final Medicine newMedicine = new Medicine(
                medicine.getId(), medicine.getName(), medicine.getValueList());
        medicineData.postValue(newMedicine);
    }

//    public void setDosage(Double dosage) {
//        medicineData.getValue().setDosage(dosage);
//    }

    public void saveMedicine(Medicine med) {
        userRepository.postMedication(med);
    }

    public void deleteMedicine() {
        userRepository.deleteMedication(medicineData.getValue());
    }

}
