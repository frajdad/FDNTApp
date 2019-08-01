package com.example.helloworld;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Logowanie extends AppCompatActivity {


    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logowanie);

        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.

        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null)
            updateUI(currentUser);
    }

    // uruchamia się gdy zostanie przyciśnięty przysisk
    public void zaloguj(View view) {

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
                            // Sign in success, update UI with the signed-in user's information

                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            TextView zle_dane = findViewById(R.id.zle_dane);
                            zle_dane.setVisibility(View.VISIBLE);
                        }

                        // ...
                    }
                });
    }

    private void updateUI(FirebaseUser user) {

        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        Dane.ta_aktywnosc.finish();
        finish();
    }

}