package fdnt.app.android.post;

import android.os.StrictMode;

import java.util.Properties;

import javax.mail.AuthenticationFailedException;
import javax.mail.Folder;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.Transport;

import fdnt.app.android.Dane;

public class  MailLogging {

    private static void openSmtpSession(final String email, final String pass) throws MessagingException {
        Properties props = new Properties();

        props.put("mail.smtp.host", "smtp.dzielo.pl");
        props.put("mail.smtp.socketFactory.port", "465");
        props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.port", "465");

        Dane.smtpSession = Session.getInstance(props,
                new javax.mail.Authenticator() {
                    //Authenticating the password
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(email, pass);
                    }
                });

        //Testing connection
        Transport transport = Dane.smtpSession.getTransport("smtp");
        transport.connect("smtp.dzielo.pl", 465, email, pass);
        transport.close();
    }

    private static void openPop3Session(final String email, final String pass) throws MessagingException {
        Properties props = new Properties();

        props.put("mail.pop3.host", "mail.dzielo.pl");
        props.put("mail.pop3.port", "110");
        props.put("mail.pop3.starttls.enable", "true");
        Dane.pop3Session = Session.getInstance(props,
                new javax.mail.Authenticator() {
                    //Authenticating the password
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(email, pass);
                    }
                });

        //Testing connection
        Store store = Dane.pop3Session.getStore("pop3");
        store.connect();
        store.close();
    }

    protected static boolean openSessions(String email, String pass) throws MessagingException {
        enableStrictMode();
        openSmtpSession(email, pass);
        openPop3Session(email, pass);
        return testConnection(email, pass);
    }

    private static void enableStrictMode() {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
    }

    private static boolean testConnection(String email, String pass) {
        enableStrictMode();

        try {
            Transport transport = Dane.smtpSession.getTransport("smtp");
            transport.connect("smtp.dzielo.pl", 465, email, pass);
            transport.close();

            Store store = Dane.pop3Session.getStore("pop3");
            store.connect();
            store.close();

            return true;

        } catch (AuthenticationFailedException e) {
            System.out.println("AuthenticationFailedException - for authentication failures");
            e.printStackTrace();
            System.out.println(e.getMessage());
        } catch (MessagingException e) {
            System.out.println("for other failures");
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    protected static void getMesseges() throws MessagingException {
        Store store = Dane.pop3Session.getStore("pop3");
        store.connect();

        Folder emailFolder = store.getFolder("INBOX");
        emailFolder.open(Folder.READ_ONLY);


        Dane.messages = emailFolder.getMessages();

        emailFolder.close(false);
        store.close();
    }
}