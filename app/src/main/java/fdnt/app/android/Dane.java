package fdnt.app.android;

//chcę tu trzymać jakieś globalne dane


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import javax.mail.Message;
import javax.mail.Session;

public class Dane {



    protected static Boolean ifLogged() {
        if(FirebaseAuth.getInstance().getCurrentUser() == null)
            return false;
        else
            return true;
    }


    protected static MainActivity ta_aktywnosc;
    protected static UstawieniaAX aktywnosc_ustawienia;

    //Poczta
    protected static Session smtpSession;
    protected static Session pop3Session;
    protected static Message[] messages;

    //Aktualnie zalogowany użytkownik
    protected static String userEmail() {

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




