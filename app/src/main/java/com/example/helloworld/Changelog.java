package com.example.helloworld;

import android.os.Bundle;
import android.webkit.WebView;

import androidx.appcompat.app.AppCompatActivity;

public class Changelog extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_changelog);

        WebView webView = findViewById(R.id.changelog);
        webView.loadUrl("file:///android_asset/changelog.html");
    }
}
