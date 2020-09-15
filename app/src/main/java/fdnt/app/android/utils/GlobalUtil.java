package fdnt.app.android.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.util.Patterns;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import javax.mail.Session;
import javax.mail.internet.InternetAddress;

import fdnt.app.android.BuildConfig;
import fdnt.app.android.UstawieniaAX;
import fdnt.app.android.post.MailSender;

public class GlobalUtil {

    public static boolean ifLogged() {
        return FirebaseAuth.getInstance().getCurrentUser() != null;
    }

    public static boolean ifLoggedToPost() {
        SharedPreferences data = this_activity.getSharedPreferences("post", Context.MODE_PRIVATE);
        return !data.getString("pass", "").equals("");
    }


    public static Activity this_activity;
    public static UstawieniaAX aktywnosc_ustawienia;

    //Poczta
    public static Session smtpSession;

    //Aktualnie zalogowany użytkownik
    public static String userEmail() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user != null)
            return user.getEmail();
        else
            return "";
    }

    public static String userName() {

        return userEmail()
                .replace(".", "")
                .replace("@dzielopl", "");
    }

    public static void createUriIntent(Context context, String uri) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
        context.startActivity(intent);
    }

    public static boolean isValidEmailAddr(String emailAddr) {
        if (emailAddr.isEmpty()) {
            return false;
        } else {
            return Patterns.EMAIL_ADDRESS.matcher(emailAddr).matches();
        }
    }
    
    /**
     * Method checks does given email and password are correct
     *
     * @param email Email, which we wanna check
     * @param password Password, which we wanna check
     * @return Returns rather email and password are correct
     * */
    public static boolean isProperEmailAndPasswordProvided(String email, String password) {
        if(email == null || password == null || password.length () == 0) return false;
        try {
            new InternetAddress (email).validate ();
        } catch (Exception ex) {
            return false;
        }
        return true;
    }
    
    /**
     * Methods responsible for sending and email.
     *
     * @param mail    Receiver's email address.
     * @param context Required for some magic :(
     */
    public static void sendMail(String mail, Context context) {
        if (GlobalUtil.ifLoggedToPost()) {
            Intent intent = new Intent(GlobalUtil.this_activity, MailSender.class);
            intent.putExtra("to", mail);
            GlobalUtil.this_activity.startActivity(intent);
        } else {
            String mailURL = "mailto:" + mail;
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(mailURL));
            context.startActivity(intent);
        }
    }

    //Wersja aplikacji
    public String nazwaWersjiAplikacji() {
        return BuildConfig.VERSION_NAME;
    }

    public int numerWersjiAplikacji() {
        return BuildConfig.VERSION_CODE;
    }

}




