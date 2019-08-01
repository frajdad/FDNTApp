package com.example.helloworld;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class Powiadomienia extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {


    }

    @Override
    public void onNewToken(String token) {
    }
}
