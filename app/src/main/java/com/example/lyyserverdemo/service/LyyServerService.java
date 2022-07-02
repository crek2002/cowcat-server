package com.example.lyyserverdemo.service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.IBinder;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.example.lyyserverdemo.MainActivity;
import com.example.lyyserverdemo.R;
import com.example.lyyserverdemo.broadcastreceiver.LyyServerBroadcastReceiver;
import com.example.lyyserverdemo.utils.NetUtils;
import com.yanzhenjie.andserver.AndServer;
import com.yanzhenjie.andserver.Server;

import java.net.InetAddress;
import java.util.concurrent.TimeUnit;

/**
 * @author Yingyong Lao
 * 创建时间 2022/5/14 11:13
 * @version 1.0
 */
public class LyyServerService extends Service {
    private Server server;

    @Override
    public void onCreate() {
        super.onCreate();
        server= AndServer.webServer(this)
                .port(8080)//服务器端口
                .timeout(10, TimeUnit.SECONDS)//连接超时，响应超时时间
                .listener(new Server.ServerListener() {
                    @Override
                    public void onStarted() {
                        InetAddress localIPAddress = NetUtils.getLocalIPAddress();
                        if (localIPAddress!=null){
                            LyyServerBroadcastReceiver.onServerStart(LyyServerService.this,localIPAddress.getHostAddress());//服务器启动的时候发送广播
                        }else {
                            LyyServerBroadcastReceiver.onServerError(LyyServerService.this, "获取网络地址失败，请检查网络连接。");//服务器出现异常的时候发送一条广播
                        }
                    }

                    @Override
                    public void onStopped() {
                        LyyServerBroadcastReceiver.onServerStop(LyyServerService.this);//服务器停止的时候发送一条广播
                    }

                    @Override
                    public void onException(Exception e) {
                        e.printStackTrace();
                        LyyServerBroadcastReceiver.onServerError(LyyServerService.this, e.getMessage());//服务器出现异常的时候发送一条广播
                    }
                })
                .build();

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
            NotificationChannel notificationChannel = new NotificationChannel("my_service", "前台Service通知", NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(notificationChannel);
        }
        Intent intent=new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "my_service");
        builder.setContentTitle("Cowcat");
        builder.setContentText("Cowcat服务器已经启动...");
        builder.setSmallIcon(R.drawable.ic_play_arrow);
        builder.setLargeIcon(BitmapFactory.decodeResource(getResources(),R.drawable.icons8_cat_96px_1));
        builder.setContentIntent(pendingIntent);
        Notification notification = builder.build();
        startForeground(1,notification);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        server.startup();
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        server.shutdown();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
