package com.example.parkinson.features.notification;


import androidx.annotation.NonNull;

import com.example.parkinson.data.UserRepository;
import com.example.parkinson.model.general_models.Medicine;
import com.example.parkinson.model.general_models.MedicineReport;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.components.SingletonComponent;

@Module
@InstallIn(SingletonComponent.class)
public class NotifManager {

    private UserRepository userRepository;
    private NotificationMangerInterface listener;

    @Singleton
    @Provides
    public NotifManager provideNotifManager(UserRepository userRepository){
        NotifManager manager = new NotifManager();
        manager.userRepository = userRepository;

        return manager;
    }

    public interface NotificationMangerInterface {
        void postNotification(List<Medicine> medicines, int id);

        void closeNotif(int id);
    }

    public void getMedication(int notificationID) {
        userRepository.getMedicationListNotif(getListner(notificationID));
    }

    private ValueEventListener getListner(int notifId) {
        String id = notifId + "";
        return new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    List<Medicine> list = new ArrayList<>();

                    for (DataSnapshot med : snapshot.getChildren()) {
                        Medicine tempMed = med.getValue(Medicine.class);
//                        if (tempMed.getHoursArr()!= null) {
//                            for (Time time : tempMed.getHoursArr()) {
//                                if (time.toString().equals(id))
//                                    list.add(tempMed);
//                            }
//                        }
                    }

                    if (listener != null)
                        listener.postNotification(list, notifId);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
    }

    public void report(int notifId) {
        userRepository.getMedicationListNotif(getListnerForCheck(notifId));
    }

    private ValueEventListener getListnerForCheck(int notifId) {
        String tempId = notifId + "";
        if (notifId == 0) {
            tempId = "00";
        }

        final String id = tempId;
        return new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    List<Medicine> list = new ArrayList<>();

                    for (DataSnapshot med : snapshot.getChildren()) {
                        Medicine tempMed = med.getValue(Medicine.class);
//                        if (tempMed.getHoursArr()!= null) {
//                            for (Time time : tempMed.getHoursArr()) {
//                                if (time.toString().equals(id))
//                                    list.add(tempMed);
//                            }
//                        }
                    }

                    if (listener != null)
                        pushReports(list, notifId);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
    }

    private void pushReports(List<Medicine> list, int notifId) {
        List<MedicineReport> reportList = new ArrayList<>();
        for (Medicine med : list) {
            reportList.add(new MedicineReport(med.getId(), Calendar.getInstance().getTimeInMillis()));
        }
        userRepository.pushMedicineReport(reportList, getFinishReportListner(notifId));
    }

    private ValueEventListener getFinishReportListner(int id) {
        return new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (listener != null) {
                    listener.closeNotif(id);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
    }

    public void setListener(NotificationMangerInterface listener) {
        this.listener = listener;
    }

}
