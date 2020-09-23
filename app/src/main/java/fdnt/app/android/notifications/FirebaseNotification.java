package fdnt.app.android.notifications;

import android.content.Intent;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import fdnt.app.android.MainFrame;


//Ta zakładka obsługuje powiadomienia push wysyłane z konsoli Firebase
public class FirebaseNotification extends FirebaseMessagingService {


    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        Intent intent = new Intent(getApplicationContext(), MainFrame.class);
        intent.putExtra("tresc", remoteMessage.getData().toString());

        startActivity(intent);


    }

    @Override
    public void onDeletedMessages() {

    }

    @Override
    public void onNewToken(String token) {

    }
}
