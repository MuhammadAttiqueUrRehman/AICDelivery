package com.aic.aicdelivery;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import androidx.core.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.messaging.RemoteMessage;


public class FirebaseMessagingService extends com.google.firebase.messaging.FirebaseMessagingService {
    private static final String TAG = "FireBase";
    private NotificationChannel mChannel;
    private NotificationManager notifManager;

    public FirebaseMessagingService() {
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        Log.i("Debugging", "Received Remote Message");
        Log.i("Debugging", "Build.VERSION.SDK_INT " + Build.VERSION.SDK_INT + "  Build.VERSION_CODES.O " + Build.VERSION_CODES.O);
        Log.i("Debugging", "Remote Message " + remoteMessage.getNotification().toString());
        if (remoteMessage.getNotification().getBody().length() > 0) {
            displayCustomNotificationForOrders(remoteMessage.getNotification().getTitle(), remoteMessage.getNotification().getBody());
        }
    }

    private void displayCustomNotificationForOrders(String title, String description) {
        Log.d("msg", "onMessageReceived: " + description);
        Bitmap bitmaps = BitmapFactory.decodeResource(getResources(), R.drawable.nlogo);
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT | PendingIntent.FLAG_IMMUTABLE);
        String channelId = "KHIDMANOW";
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, channelId)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(title)
//                .setLargeIcon(bitmaps)
                .setPriority(NotificationManager.IMPORTANCE_MAX)
                /*.setStyle(new NotificationCompat.BigPictureStyle()
                        .bigPicture(bitmaps)
                        .bigLargeIcon(null))*/
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText(description))
                .setContentText(description).setAutoCancel(true).setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .setDefaults(NotificationCompat.DEFAULT_VIBRATE)
                .setPriority(NotificationCompat.PRIORITY_MAX)
                .setCategory(NotificationCompat.CATEGORY_MESSAGE);
        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            builder.setVisibility(NotificationCompat.VISIBILITY_PUBLIC);
            NotificationChannel channel = new NotificationChannel(channelId, "khidmanow channel", NotificationManager.IMPORTANCE_HIGH);
            manager.createNotificationChannel(channel);
        }
        manager.notify(0, builder.build());
    }

    private int getNotificationIcon() {
        boolean useWhiteIcon = (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP);
        return useWhiteIcon ? R.mipmap.ic_launcher : R.mipmap.ic_launcher;
    }
}


/*public class FirebaseMessagingService extends com.google.firebase.messaging.FirebaseMessagingService {
    private static final String TAG ="FireBase" ;
    private NotificationChannel mChannel;
    private NotificationManager notifManager;
    public FirebaseMessagingService() {
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        Log.i(TAG, "1111111111111111111111111111111onMessageRecevied111111111111111111111111111111111111");
        Log.i(TAG, "1111111111111111111111111111111onMessageRecevied111111111111111111111111111111111111" + remoteMessage.getData().toString());
            if ( remoteMessage.getData().size() > 0) {
                Log.i(TAG, "2222222222222222222222222222onMessageRecevied222222222222222222222222222222222222");
                JSONObject jsonObject = new JSONObject(remoteMessage.getData());
                try {
                    displayCustomNotificationForOrders(jsonObject.getString("title"), jsonObject.getString("body"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
    }

    private void displayCustomNotificationForOrders(String title, String description) {
        if (notifManager == null) {
            notifManager = (NotificationManager) getSystemService
                    (Context.NOTIFICATION_SERVICE);
        }
        Log.i(TAG, "3333333333333333333333333333333onMessageRecevied333333333333333333333333333333333333333333333");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Log.i(TAG, "3333333333333333333333333333333onMessageRecevied33333333333333333333333333333");
            NotificationCompat.Builder builder;
            Intent intent = new Intent(this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            PendingIntent pendingIntent;
            int importance = NotificationManager.IMPORTANCE_HIGH;
            if (mChannel == null) {
                mChannel = new NotificationChannel
                        ("0", title, importance);
                mChannel.setDescription(description);
                mChannel.enableVibration(true);
                notifManager.createNotificationChannel(mChannel);
            }
            builder = new NotificationCompat.Builder(this, "0");
            Log.i(TAG, "4444444444444444444444444444444onMessageRecevied4444444444444444444444444444444444");
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                    Intent.FLAG_ACTIVITY_SINGLE_TOP);
            pendingIntent = PendingIntent.getActivity(this, 1251, intent, PendingIntent.FLAG_ONE_SHOT);
            builder.setContentTitle(title)
                    .setSmallIcon(getNotificationIcon()) // required
                    .setContentText(description)  // required
                    .setDefaults(Notification.DEFAULT_ALL)
                    .setAutoCancel(true)
                    .setLargeIcon(BitmapFactory.decodeResource
                            (getResources(), R.mipmap.ic_launcher))
                    .setBadgeIconType(R.mipmap.ic_launcher)
                    .setContentIntent(pendingIntent)
                    .setSound(RingtoneManager.getDefaultUri
                            (RingtoneManager.TYPE_NOTIFICATION));
            Notification notification = builder.build();
            notifManager.notify(0, notification);
            Log.i(TAG, "5555555555555555555555555555555555onMessageRecevied5555555555555555555555555555555555");
        } else {
            Intent intent = new Intent(this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            PendingIntent pendingIntent = null;
            pendingIntent = PendingIntent.getActivity(this, 1251, intent, PendingIntent.FLAG_ONE_SHOT);
            Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                    .setContentTitle(title)
                    .setContentText(description)
                    .setAutoCancel(true)
                    .setColor(ContextCompat.getColor(getBaseContext(), R.color.colorPrimary))
                    .setSound(defaultSoundUri)
                    .setSmallIcon(getNotificationIcon())
                    .setContentIntent(pendingIntent)
                    .setStyle(new NotificationCompat.BigTextStyle().setBigContentTitle(title).bigText(description));

            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.notify(1251, notificationBuilder.build());
        }
    }

    private int getNotificationIcon() {
        boolean useWhiteIcon = (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP);
        return useWhiteIcon ? R.mipmap.ic_launcher : R.mipmap.ic_launcher;
    }

}
*/
