package com.mbl.zaikavendor.helper;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.util.Log;

import com.mbl.zaikavendor.R;
import com.mbl.zaikavendor.Welcome;
import com.mbl.zaikavendor.app.EndPoints;
import com.mbl.zaikavendor.constant.Config;
import com.mbl.zaikavendor.constant.NotificationUtils;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Map;

import java.net.URL;
import java.util.Random;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    private static final String TAG = MyFirebaseMessagingService.class.getSimpleName();
    private NotificationUtils notificationUtils;

    SharedPreferences pref ;
    String ringtoneid="0";


    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Log.e(TAG, "From: " + remoteMessage.getFrom());

        pref= getSharedPreferences(EndPoints.sharedpref, 0);
        ringtoneid = pref.getString(EndPoints.ringtonetype,"0");
        int totalnotification=1;
        SharedPreferences.Editor editor = pref.edit();
        if(pref.getInt(EndPoints.notificationcount, -1)>0){
            totalnotification=pref.getInt(EndPoints.notificationcount, -1)+1;
            editor.putInt(EndPoints.notificationcount, totalnotification); // Storing name
        } else{
            editor.putInt(EndPoints.notificationcount, totalnotification); // Storing name
        }
        editor.commit();

        if (remoteMessage == null)
            return;

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            Log.e(TAG, "Notification Body: " + remoteMessage.getNotification().getBody());
            handleNotification(remoteMessage.getNotification().getBody());
        }

        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            Log.e(TAG, "Data Payload: " + remoteMessage.getData().toString());

            try {
                JSONObject json = new JSONObject(remoteMessage.getData().toString());
                handleDataMessage(json);



            } catch (Exception e) {
                Log.e(TAG, "Exception: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    private void handleNotification(String message) {
        if (!NotificationUtils.isAppIsInBackground(getApplicationContext())) {
            // app is in foreground, broadcast the push message
            Intent pushNotification = new Intent(Config.PUSH_NOTIFICATION);
            pushNotification.putExtra("message", message);
            LocalBroadcastManager.getInstance(this).sendBroadcast(pushNotification);

            // play notification sound
            NotificationUtils notificationUtils = new NotificationUtils(getApplicationContext());
            notificationUtils.playNotificationSound();
        }else{
            // If the app is in background, firebase itself handles the notification
        }
    }

    private void handleDataMessage(JSONObject json) {
        Log.e(TAG, "push json: " + json.toString());

        try {
            JSONObject data = json.getJSONObject("data");

            String title = data.getString("title");
            String message = data.getString("message");
            boolean isBackground = data.getBoolean("is_background");
            String imageUrl = data.getString("image");
            String timestamp = data.getString("timestamp");
            JSONObject payload = data.getJSONObject("payload");
            sendNotification(title,message);

            SharedPreferences.Editor editor = pref.edit();

            if(title.contains("restaurant")){
                int already=pref.getInt(EndPoints.ordertype1,0);
                editor.putInt(EndPoints.ordertype1, (already+1)); // Storing name
            } else if(title.contains("party")){
                int already=pref.getInt(EndPoints.ordertype2,0);
                editor.putInt(EndPoints.ordertype2, (already+1)); // Storing name
            } else if(title.contains("tiffin")){
                int already=pref.getInt(EndPoints.ordertype3,0);
                editor.putInt(EndPoints.ordertype3, (already+1)); // Storing name
                Log.e(TAG, "tiffin: " + (already+1));
            } else if(title.contains("Medicated")){
                int already=pref.getInt(EndPoints.ordertype4,0);
                editor.putInt(EndPoints.ordertype4, (already+1)); // Storing name
            }else{
                Log.e(TAG, "tiffin: Not found"  );
            }
            editor.commit();

            Log.e(TAG, "title: " + title);
            Log.e(TAG, "message: " + message);
            Log.e(TAG, "isBackground: " + isBackground);
            Log.e(TAG, "payload: " + payload.toString());
            Log.e(TAG, "imageUrl: " + imageUrl);
            Log.e(TAG, "timestamp: " + timestamp);


         /*   if (!NotificationUtils.isAppIsInBackground(getApplicationContext())) {
                // app is in foreground, broadcast the push message
                Intent pushNotification = new Intent(Config.PUSH_NOTIFICATION);
                pushNotification.putExtra("message", message);
                LocalBroadcastManager.getInstance(this).sendBroadcast(pushNotification);

                // play notification sound
                NotificationUtils notificationUtils = new NotificationUtils(getApplicationContext());
                notificationUtils.playNotificationSound();
            } else {
                // app is in background, show the notification in notification tray
                Intent resultIntent = new Intent(getApplicationContext(), Welcome.class);
                resultIntent.putExtra("message", message);

                // check for image attachment
                if (TextUtils.isEmpty(imageUrl)) {
                    showNotificationMessage(getApplicationContext(), title, message, timestamp, resultIntent);
                } else {
                    // image is present, show notification with image
                    showNotificationMessageWithBigImage(getApplicationContext(), title, message, timestamp, resultIntent, imageUrl);
                }
            } */
        } catch (JSONException e) {
            Log.e(TAG, "Json Exception: " + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "Exception: " + e.getMessage());


        }
    }

    /**
     * Showing notification with text only
     */
    private void showNotificationMessage(Context context, String title, String message, String timeStamp, Intent intent) {
        notificationUtils = new NotificationUtils(context);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        notificationUtils.showNotificationMessage(title, message, timeStamp, intent);
    }

    /**
     * Showing notification with text and image
     */
    private void showNotificationMessageWithBigImage(Context context, String title, String message, String timeStamp, Intent intent, String imageUrl) {
        notificationUtils = new NotificationUtils(context);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        notificationUtils.showNotificationMessage(title, message, timeStamp, intent, imageUrl);
    }

    private void sendNotification(String remoteMessage,String msg) {
        //  RemoteMessage.Notification notification = remoteMessage.getNotification();
        Intent intent = new Intent(this, Welcome.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent,
                PendingIntent.FLAG_ONE_SHOT);

        int notificationId = new Random().nextInt(); // just use a counter in some util class...

        String CHANNEL_ID = "zaikahansappdriver";// The id of the channel.
        String channelName = "Channel Name Driver";
        int importance = NotificationManager.IMPORTANCE_HIGH;

        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        Uri uri=Uri.parse("android.resource://"+getPackageName()+"/raw/perfect_sms");
        if(ringtoneid.equals("1")){
            uri=Uri.parse("android.resource://"+getPackageName()+"/raw/perfect_sms");
        }
        Ringtone ring = RingtoneManager.getRingtone(getApplicationContext(), uri);
        ring.play();
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this,CHANNEL_ID)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(),R.drawable.appicon))
                .setSmallIcon(R.drawable.appicon)
                .setContentTitle(remoteMessage)
                .setContentText(msg)
                .setAutoCancel(true)
                .setSound(null)
                //.setSound(uri)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel mChannel = new NotificationChannel(
                    CHANNEL_ID, channelName, importance);
            notificationManager.createNotificationChannel(mChannel);
        }
        notificationManager.notify(0, notificationBuilder.build());

        Intent i = new Intent();
        i.setClass(this, Welcome.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i);
    }

}
