package com.example.parkinson.fcm;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import com.example.ParkinsonApplication;
import com.example.parkinson.R;
import com.example.parkinson.features.main.MainActivity;
import com.example.parkinson.features.splash.SplashActivity;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;

import javax.inject.Inject;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "FirebaseMessagingServce";

    private MessagingManager messagingManager;
//    String roomKey;
//    String contactName;

    public MyFirebaseMessagingService() {

    }

    @Inject
    public MyFirebaseMessagingService(MessagingManager messagingManager) {
        this.messagingManager = messagingManager;
    }

    @Override
    public void onNewToken(String token) {
        super.onNewToken(token);
//        messagingManager.updateToken(token);
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        // Message contains a data payload.
        if (remoteMessage.getNotification() == null) {
            sendMessageNotification(remoteMessage);
        }
        else {
            // Message contains a notification payload.
            String notificationTitle = remoteMessage.getNotification().getTitle();
            String notificationBody = remoteMessage.getNotification().getBody();
            sendAnnouncementNotification(notificationTitle, notificationBody);
        }
    }

    /**
     * This function is triggered when an announcement type notification is received.
     * This kind of notification is always displayed to the user, regardless of the app's current state.
     * An empty intent is passed in the pending intent.
     * @param notificationTitle The title given by the server.
     * @param notificationBody The notification body given by the server.
     */
    private void sendAnnouncementNotification(String notificationTitle, String notificationBody) {

        final String NOTIFICATION_CHANNEL_ID = "announcement";
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        // Android versions Oreo and above require a notification channel
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            NotificationChannel channel = makeChannel(NOTIFICATION_CHANNEL_ID);
            manager.createNotificationChannel(channel);
        }

        // Set a message ringtone
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        // empty intent
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent pendingIntent =
                PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder =
                createNotificationBuilder(NOTIFICATION_CHANNEL_ID, notificationTitle, notificationBody, pendingIntent, defaultSoundUri);

        manager.notify(0, builder.build());
    }

    /**
     * This function is triggered when a message type notification is received.
     * This notification will contain a data payload sent by the server, with the following keys:<br><br>
     * &nbsp {@code room_key - The relevant chat room.}<br> &nbsp {@code message - The message.}<br>
     * &nbsp {@code title - The notification title.}<br> &nbsp {@code contact_name - The notification title.}<br>
     * &nbsp {@code notif_type - The notification type.}<br><br> <b>Message notifications have data payloads only</b>.
     * The extracted values from the payload can be obtained in the launched activity via the above keys.<br>
     * Example of retrieving the values:<br><br>
     * <pre>
     * if (getIntent().getExtras() != null) {
     *  for (String key : getIntent().getExtras().keySet()) {
     *      // get all values here
     *  }
     * }</pre>
     *
     * @param remoteMessage The FCM message
     */
    private void sendMessageNotification(RemoteMessage remoteMessage) {

        // Variables for the current push notification
        final boolean isAppInBackground = ParkinsonApplication.IS_APP_IN_BACKGROUND;
        final String NOTIFICATION_CHANNEL_ID = "private_message";

        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        // Get the data payload.
        Map<String, String> data = remoteMessage.getData();
        String roomKey = data.get("room_key");
        String message = data.get("message");
        String title = data.get("title");

        // Prepare data to be sent to the app.
        // This data can be retrieved in the launched activity's intent extras.
        Bundle bundle = new Bundle();
        bundle.putString("room_key", roomKey);

        // Android versions Oreo and above require a notification channel
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            NotificationChannel channel = makeChannel(NOTIFICATION_CHANNEL_ID);
            manager.createNotificationChannel(channel);
        }

        // Set a message ringtone
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        // If the app is dead or simply in the background, this intent will be used
        // to bring up the application via a click event on the notification window.
        // This intent is initialized according to the current state of the application.
        // If the app is not alive, the Splash activity is set to launch.
        // Otherwise, the Main activity is set to launch.
        Intent intent;

        intent = new Intent(getApplicationContext(), SplashActivity.class);
//        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        if (isAppDead) {
//        }
//        else {
//            intent = new Intent(getApplicationContext(), MainActivity.class);
//            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        }

        intent.putExtras(bundle);

        PendingIntent pendingIntent =
                PendingIntent.getActivity(this, 1, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder =
                createNotificationBuilder(NOTIFICATION_CHANNEL_ID, title, message, pendingIntent, defaultSoundUri);

        // Message notifications should be shown only if the app isn't in the foreground.
        if (isAppInBackground) {
            manager.notify(1, builder.build());
        }
    }

    private NotificationCompat.Builder createNotificationBuilder(String channelID, String title, String message, PendingIntent pendingIntent, Uri soundUri) {

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, channelID);
        builder.setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_ALL)
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.drawable.icon_medical)
                .setContentTitle(title)
                .setContentIntent(pendingIntent)
                .setSound(soundUri)
                .setContentText(message);

        return builder;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private NotificationChannel makeChannel(String channelID) {

        NotificationChannel channel = new NotificationChannel(channelID, "data notification", NotificationManager.IMPORTANCE_HIGH);
        channel.setDescription("channel for data notifications");
        channel.enableLights(true);
        channel.setLightColor(Color.RED);
        channel.enableVibration(true);
        channel.setVibrationPattern(new long[]{0, 1000, 500, 1000});

        return channel;
    }
}