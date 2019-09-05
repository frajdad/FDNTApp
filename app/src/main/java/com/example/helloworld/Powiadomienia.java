package com.example.helloworld;

import android.content.Intent;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;


//Ta zakładka obsługuje powiadomienia push wysyłane z konsoli Firebase
public class Powiadomienia extends FirebaseMessagingService {


    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
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
