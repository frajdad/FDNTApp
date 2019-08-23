package com.example.helloworld;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Logowanie extends AppCompatActivity {


    //To jest obiekt do uwierzytelniania
    private FirebaseAuth mAuth;
    //To jest obiekt, który pokazjuje kręcące się kóło na czas logowania
    private ProgressDialog mProgress;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logowanie);

        mAuth = FirebaseAuth.getInstance();

        mProgress = new ProgressDialog(this);
        mProgress.setTitle("Logowanie");
        mProgress.setMessage("Proszę czekać...");
        mProgress.setCancelable(false);
        mProgress.setIndeterminate(true);
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.

        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null)
            resetuj(currentUser);
    }

    // uruchamia się gdy zostanie przyciśnięty przysisk
    public void zaloguj(View view) {

        mProgress.show();

        EditText editTextEmail = (EditText) findViewById(R.id.email);
        String email = editTextEmail.getText().toString();
        EditText editTextPassword = (EditText) findViewById(R.id.password);
        String password = editTextPassword.getText().toString();


        loguj(email, password);

    }

    private void loguj(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            FirebaseUser user = mAuth.getCurrentUser();
                            resetuj(user);
                        } else {

                            mProgress.dismiss();
                            TextView zle_dane = findViewById(R.id.zle_dane);
                            zle_dane.setVisibility(View.VISIBLE);
                        }

                    }
                });
    }

    private void resetuj(FirebaseUser user) {

        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        Dane.ta_aktywnosc.finish();
        finish();
    }


}