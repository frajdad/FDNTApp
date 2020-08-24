package fdnt.app.android.post;

import android.os.StrictMode;

import java.util.Properties;

import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Store;
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

    private static void openPop3Session(final String email, final String pass) throws MessagingException {
        Properties props = new Properties();

        props.put("mail.pop3.host", "mail.dzielo.pl");
        props.put("mail.pop3.port", "110");
        props.put("mail.pop3.starttls.enable", "true");
        GlobalUtil.pop3Session = Session.getInstance(props,
                new javax.mail.Authenticator() {
                    //Authenticating the password
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(email, pass);
                    }
                });

        //Testing connection
        Store store = GlobalUtil.pop3Session.getStore("pop3");
        store.connect();
        store.close();
    }

    private static void openImapSession(final String email, final String pass) throws MessagingException {
        Properties props = new Properties();

        props.put("mail.imap.host", "mail.dzielo.pl");
        props.put("mail.imap.port", "993");
        props.put("mail.imap.starttls.enable", "true");

        GlobalUtil.imapSession = Session.getInstance(props,
                new javax.mail.Authenticator() {
                    //Authenticating the password
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(email, pass);
                    }
                });
    }

    public static void openSessions(String email, String pass) throws MessagingException {
        enableStrictMode();
        openSmtpSession(email, pass);
        openPop3Session(email, pass);
    //    openImapSession(email, pass);
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

        Store store = GlobalUtil.pop3Session.getStore("pop3");
        store.connect();
        store.close();
    }
}