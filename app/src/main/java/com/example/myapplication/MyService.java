package com.example.myapplication;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;

public class MyService extends Service {

    private static final String TAG = "MyService";
    private String notificationId = "ChannelId";
    private String notificationName = "ChannelName";
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.i(TAG,"onBind called!");
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i(TAG,"onCreate called");
    }

    private Notification getNotification() {
        Notification.Builder builder = new Notification.Builder(this);
        builder.setSmallIcon(R.drawable.ic_save)
                .setContentTitle("Bluetooth scan Test");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            builder.setChannelId(notificationId);
        }
        return builder.build();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i(TAG,"onStartCommand called!");
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel channel = new NotificationChannel(
                    notificationId,
                    notificationName,
                    NotificationManager.IMPORTANCE_HIGH);
            notificationManager.createNotificationChannel(channel);
            startForeground(1, getNotification());// 通知栏标识符 前台进程对象唯一ID
        }
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        Log.i(TAG,"onDestroy called");
        super.onDestroy();
    }
}
