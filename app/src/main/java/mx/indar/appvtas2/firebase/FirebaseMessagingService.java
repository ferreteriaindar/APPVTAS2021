package mx.indar.appvtas2.firebase;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Vibrator;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.google.firebase.messaging.RemoteMessage;

import mx.indar.appvtas2.NavigationIndar;
import mx.indar.appvtas2.R;

public class FirebaseMessagingService extends  com.google.firebase.messaging.FirebaseMessagingService {

    public FirebaseMessagingService() {
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        String title= remoteMessage.getNotification().getTitle();
        String message= remoteMessage.getNotification().getBody();
        Log.i("notificacion","Mensaje"+title+"*"+message);
        sendNotification(message,title);
    }

    @Override
    public void onDeletedMessages() {

    }


    private  void sendNotification(String messageBody,String title)
    {
        Intent intent = new Intent(this,NavigationIndar.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,0 /*request code*/,intent,PendingIntent.FLAG_ONE_SHOT);
        long[] v = {500,1000};

        grantUriPermission("com.android.systemui", RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION),
                Intent.FLAG_GRANT_READ_URI_PERMISSION);
        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder= new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.ic_ok)
                .setContentTitle(title)
                .setContentText(messageBody)
                .setAutoCancel(true)
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .setVibrate(new long[] {500, 500, 500, 500, 500})
                .setColor(Color.YELLOW)

                .setContentIntent(pendingIntent);



        NotificationManager notificationManager= (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0 /* Id Notification*/,notificationBuilder.build());
        Vibrator vi = (Vibrator) this.getApplicationContext().getSystemService(Context.VIBRATOR_SERVICE);
        // Vibrate for 500 milliseconds
        vi.vibrate(500);




    }
    public class FirebaseIDService extends FirebaseInstanceIdService {
        private static final String TAG = "FirebaseIDService";

        @Override
        public void onTokenRefresh() {
            String refreshedToken = FirebaseInstanceId.getInstance().getToken();
            Log.d(TAG, "Refreshed token: " + refreshedToken);

            // TODO: Implement this method to send any registration to your app's servers.
            sendRegistrationToServer(refreshedToken);
        }

        private void sendRegistrationToServer(String token) {
            // Add custom implementation, as needed.
        }
    }
}
