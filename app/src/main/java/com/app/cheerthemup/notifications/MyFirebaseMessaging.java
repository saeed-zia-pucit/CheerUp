package com.app.cheerthemup.notifications;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.app.cheerthemup.views.activities.MessageActivity1;
import com.app.cheerthemup.utils.Constants;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class MyFirebaseMessaging extends FirebaseMessagingService {


    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        String sented = remoteMessage.getData().get("sented");
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser != null && sented.equals(firebaseUser.getUid())) {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                sendOreoNotification(remoteMessage);
            } else {
                sendNotification(remoteMessage);

            }

        }
    }

    private void sendOreoNotification(RemoteMessage remoteMessage) {


        String user = remoteMessage.getData().get("user");
        String icon = remoteMessage.getData().get("icon");
        String title = remoteMessage.getData().get("title");
        String body = remoteMessage.getData().get("body");
        String sented = remoteMessage.getData().get("sented");

//        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
//// Adds the back stack
//        stackBuilder.addParentStack(ResultActivity.class);
//// Adds the Intent to the top of the stack
//        stackBuilder.addNextIntent(resultIntent);
//// Gets a PendingIntent containing the entire back stack
//        PendingIntent resultPendingIntent =
//                stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);


        RemoteMessage.Notification notification = remoteMessage.getNotification();
        int j = Integer.parseInt(user.replaceAll("[\\D]", ""));

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor edit = prefs.edit();
        String uid = prefs.getString(Constants.UID, "");


        String recieverIdentity = prefs.getString(Constants.RECIEVERIDENTITYHIDDEN, "");
        Intent intent = new Intent(this, MessageActivity1.class);
        Bundle bundle = new Bundle();
//        edit.putString(Constants.fromNotification, "true");
//        edit.apply();
//        bundle.putString(Constants.USERID, user);
//        bundle.putString(Constants.RECIEVERID, sented);
//        bundle.putString(Constants.USERNAME, body);
//        bundle.putString(Constants.RECIEVERIDENTITYHIDDEN, recieverIdentity);
//        intent.putExtras(bundle);
//        intent.setAction(Long.toString(System.currentTimeMillis()));
//        intent.addCategory(Intent.CATEGORY_LAUNCHER);
//        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra(Constants.USERNAME, body);
        intent.putExtra(Constants.UID, user);
        intent.putExtra(Constants.fromNotification, "true");
        intent.setAction(Long.toString(System.currentTimeMillis()));

        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);


//        Intent intent = new Intent(getActivity(), MessageActivity.class);
//        String uid = users.get(pos).getUid();
//        intent.putExtra(Constants.USERNAME, userName);
//        intent.putExtra(Constants.UID, uid);
//        startActivity(intent);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, j, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        Uri defaultSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);


        OreoNotifications oreoNotifications = new OreoNotifications(this);
        Notification.Builder builder = oreoNotifications.getOreoNotification(title, body, pendingIntent, defaultSound, icon);

        int i = 0;

        if (j > 0) {
            i = j;
        }


        oreoNotifications.getManager().notify(i, builder.build());
    }

    private void sendNotification(RemoteMessage remoteMessage) {

        String user = remoteMessage.getData().get("user");
        String icon = remoteMessage.getData().get("icon");
        String title = remoteMessage.getData().get("title");
        String body = remoteMessage.getData().get("body");
        String sented = remoteMessage.getData().get("sented");
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor edit = prefs.edit();

        RemoteMessage.Notification notification = remoteMessage.getNotification();
        int j = Integer.parseInt(user.replaceAll("[\\D]", ""));

        Intent intent = new Intent(this, MessageActivity1.class);
        Bundle bundle = new Bundle();
//        edit.putString(Constants.fromNotification, "true");
//        edit.apply();
//        bundle.putString(Constants.USERID, user);
//        bundle.putString(Constants.RECIEVERID, sented);
//        bundle.putString(Constants.USERNAME, body);
//        bundle.putString(Constants.RECIEVERIDENTITYHIDDEN, recieverIdentity);
//        intent.putExtras(bundle);
//        intent.setAction(Long.toString(System.currentTimeMillis()));
//        intent.addCategory(Intent.CATEGORY_LAUNCHER);
//        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra(Constants.USERNAME, body);
        intent.putExtra(Constants.UID, user);
        intent.putExtra(Constants.fromNotification, "true");
        intent.setAction(Long.toString(System.currentTimeMillis()));

        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, j, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        Uri defaultSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                .setSmallIcon(Integer.parseInt(icon))
                .setContentTitle(title)
                .setContentText(body)
                .setAutoCancel(true)
                .setSound(defaultSound)
                .setContentIntent(pendingIntent);

        NotificationManager noti = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        int i = 0;

        if (j > 0) {
            i = j;
        }


        noti.notify(i, builder.build());

    }
}

