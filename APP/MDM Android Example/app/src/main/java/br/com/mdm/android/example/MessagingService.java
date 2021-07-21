package br.com.mdm.android.example;

import androidx.annotation.NonNull;

import br.com.hands.mdm.libs.android.notification.MDMNotification;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class MessagingService extends FirebaseMessagingService {
    public MessagingService() {
    }

    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        try {
            super.onMessageReceived(remoteMessage);
            boolean isMDMNotification = MDMNotification.isMDMNotification(remoteMessage.getData());
            if (isMDMNotification) {
                MDMNotification.processNotification(remoteMessage.getData(), this.getApplicationContext());
            }
        } catch (Throwable ignore) { }
    }

    public void onNewToken(@NonNull String newToken) {
        try {
            super.onNewToken(newToken);
            MDMNotification.registerToken(newToken, this.getApplicationContext());
        } catch (Throwable ignore) {
        }
    }
}
