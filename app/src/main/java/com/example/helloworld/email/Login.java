package com.example.helloworld.email;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.helloworld.R;

import java.util.Properties;

import javax.mail.AuthenticationFailedException;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.Transport;

import androidx.appcompat.app.AppCompatActivity;

public class Login extends AppCompatActivity implements View.OnClickListener {

    //Declaring EditText
    private EditText editEmailAddress;
    private EditText editEmailPassword;

    private String email;
    private String pass;

    //Send button
    private Button buttonEmailLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        testConnection();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.email_login);

        //Initializing the views
        editEmailAddress = findViewById(R.id.editEmailAddress);
        editEmailPassword = findViewById(R.id.editEmailPassword);
        buttonEmailLogin = findViewById(R.id.buttonEmailLogin);

        //Adding click listener
        buttonEmailLogin.setOnClickListener(this);
    }

    private void openSmtpSession() throws MessagingException {
        Properties props = new Properties();

        props.put("mail.smtp.host", "smtp.dzielo.pl");
        props.put("mail.smtp.socketFactory.port", "465");
        props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.port", "465");

        Data.smtpSession = Session.getInstance(props,
                new javax.mail.Authenticator() {
                    //Authenticating the password
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(email, pass);
                    }
                });

        //Testing connection
        Transport transport = Data.smtpSession.getTransport("smtp");
        transport.connect("smtp.dzielo.pl", 465, email, pass);
        transport.close();
    }

    private void openPop3Session() throws MessagingException {
        Properties props = new Properties();

        props.put("mail.pop3.host", "mail.dzielo.pl");
        props.put("mail.pop3.port", "110");
        props.put("mail.pop3.starttls.enable", "true");
        Data.pop3Session = Session.getInstance(props,
                new javax.mail.Authenticator() {
                    //Authenticating the password
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(email, pass);
                    }
                });

        //Testing connection
        Store store = Data.pop3Session.getStore("pop3");
        store.connect();
        store.close();
    }

    private void openSessions() throws MessagingException{
        email = editEmailAddress.getText().toString().trim();
        pass = editEmailPassword.getText().toString().trim();
        Data.emailAddress = email;

        enableStrictMode();
        openSmtpSession();
        openPop3Session();
        testConnection();
    }

    @Override
    public void onClick(View v) {
        try {
            openSessions();
        } catch(AuthenticationFailedException e) {
            System.out.println("AuthenticationFailedException - for authentication failures");
            e.printStackTrace();
            System.out.println(e.getMessage());
        } catch(MessagingException e) {
            System.out.println("for other failures");
            e.printStackTrace();
        }
        buttonEmailLogin.setOnClickListener(this);
    }

    private void enableStrictMode()
    {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
    }

    private void testConnection()
    {
        enableStrictMode();

        try {
            Transport transport = Data.smtpSession.getTransport("smtp");
            transport.connect("smtp.dzielo.pl", 465, email, pass);
            transport.close();

            Store store = Data.pop3Session.getStore("pop3");
            store.connect();
            store.close();

            loginSuccess();
        } catch(AuthenticationFailedException e) {
            System.out.println("AuthenticationFailedException - for authentication failures");
            e.printStackTrace();
            System.out.println(e.getMessage());
        } catch(MessagingException e) {
            System.out.println("for other failures");
            e.printStackTrace();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    private void loginSuccess()
    {
        Intent intent = new Intent(this, Main.class);
        startActivity(intent);
        finish();
    }
}
