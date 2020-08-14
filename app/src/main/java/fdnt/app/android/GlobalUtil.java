package fdnt.app.android;

//chcę tu trzymać jakieś globalne dane


import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import javax.mail.Message;
import javax.mail.Session;

public class GlobalUtil {

    protected static boolean ifLogged() {
        return FirebaseAuth.getInstance().getCurrentUser() != null;
    }

    public static boolean ifLoggedToPost() {
        SharedPreferences data = this_activity.getSharedPreferences("post", Context.MODE_PRIVATE);
        return !data.getString("pass", "").equals("");
    }


    public static Activity this_activity;
    protected static UstawieniaAX aktywnosc_ustawienia;

    //Poczta
    public static Session smtpSession;
    public static Session pop3Session;
    public static Message[] messages;

    //Aktualnie zalogowany użytkownik
    public static String userEmail() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user != null)
            return user.getEmail();
        else
            return "";
    }

    protected static String userName() {

         return userEmail()
                 .replace(".", "")
                 .replace("@dzielopl", "");
    }

    //Wersja aplikacji
    public String nazwaWersjiAplikacji() {
        return BuildConfig.VERSION_NAME;
    }
    public int numerWersjiAplikacji() {
        return BuildConfig.VERSION_CODE;
    }
    
}




