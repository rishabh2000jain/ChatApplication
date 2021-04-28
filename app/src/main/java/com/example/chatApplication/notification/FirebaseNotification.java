package com.example.chatApplication.notification;


import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.chatApplication.BuildConfig;
import com.example.chatApplication.Constants;
import com.example.chatApplication.R;
import com.example.chatApplication.chatScreen.ChatActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;

public class FirebaseNotification extends FirebaseMessagingService  {
    public static boolean isForeGrounded;
    @Override
    public void onNewToken(@NonNull String s) {
        super.onNewToken(s);
    }

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        Map<String, String> data = remoteMessage.getData();
        String title = data.get("title");
        String body = data.get("message");

        //if the notification is sent by same user then don't show notification

        if (!FirebaseAuth.getInstance().getCurrentUser().getEmail().equals(title) && !isForeGrounded && BuildConfig.FLAVOR.equals("paid")) {
            showNotification(body, title);
        }
    }

    public void showNotification(String body, String title) {
        //for android version grater then Oreo
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel(Constants.CHANNEL_ID, Constants.CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT);
            notificationChannel.setDescription(body);
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(notificationChannel);
        }
        Intent intent = new Intent(this, ChatActivity.class);
        // if present it will close running activity above it in stack else create new
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        // FLAG_CANCEL_CURRENT used because if the activity is running then it will cancel current intent
        PendingIntent pendingIntent =  PendingIntent.getActivity(this,0,intent,PendingIntent.FLAG_CANCEL_CURRENT);
        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(this);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, Constants.CHANNEL_NAME);
        builder.setContentText(body);
        builder.setContentTitle(title);
        builder.setAutoCancel(true);
        builder.setSmallIcon(R.drawable.app_logo);
        builder.setContentIntent(pendingIntent);
        notificationManagerCompat.notify(123, builder.build());
    }



}
