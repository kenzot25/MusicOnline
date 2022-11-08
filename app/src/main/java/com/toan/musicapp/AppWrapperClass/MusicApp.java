package com.toan.musicapp.AppWrapperClass;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;

public class MusicApp extends Application {
    public static final String CONTROLLER_CHANNEL = "Music_Controller";

    @Override
    public void onCreate() {
        super.onCreate();
        createNotificationChannel();
    }
    private void createNotificationChannel(){
        NotificationChannel channel = new NotificationChannel(
                CONTROLLER_CHANNEL,
                "Bảng Điều Khiển",
                NotificationManager.IMPORTANCE_DEFAULT
        );
        channel.setDescription("Bảng Điều Khiển");
        NotificationManager manager = getSystemService(NotificationManager.class);
        manager.createNotificationChannel(channel);
    }
}
