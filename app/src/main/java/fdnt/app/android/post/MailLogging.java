package fdnt.app.android.post;

import android.os.StrictMode;

import java.util.Properties;

import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;

import fdnt.app.android.GlobalUtil;

public class  MailLogging {

    private static void openSmtpSession(final String email, final String pass) throws MessagingException {
        Properties props = new Properties();

        props.put("mail.smtp.host", "smtp.dzielo.pl");
        props.put("mail.smtp.socketFactory.port", "465");
        props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.port", "465");

        GlobalUtil.smtpSession = Session.getInstance(props,
                new javax.mail.Authenticator() {
                    //Authenticating the password
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(email, pass);
                    }
                });

        //Testing connection
        Transport transport = GlobalUtil.smtpSession.getTransport("smtp");
        transport.connect("smtp.dzielo.pl", 465, email, pass);
        transport.close();
    }

    public static void openSessions(String email, String pass) throws MessagingException {
        enableStrictMode();
        openSmtpSession(email, pass);
    }

    private static void enableStrictMode() {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
    }

    public static void testConnection(String email, String pass) throws MessagingException {
        enableStrictMode();

        Transport transport = GlobalUtil.smtpSession.getTransport("smtp");
        transport.connect("smtp.dzielo.pl", 465, email, pass);
        transport.close();
    }
}