package fdnt.app.android.post;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.widget.Toast;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import fdnt.app.android.Dane;


//Class is extending AsyncTask because this class is going to perform a networking operation
public class Send extends AsyncTask<Void,Void,Void> {

    //Declaring Variables
    private Activity context;

    //Information to send email
    private String email;
    private String subject;
    private String message;

    //Progressdialog to show while sending email
    private ProgressDialog progressDialog;

    private boolean ok = true;

    //Class Constructor
    public Send(Activity context, String email, String subject, String message){
        //Initializing variables
        this.context = context;
        this.email = email;
        this.subject = subject;
        this.message = message;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        //Showing progress dialog while sending email
        progressDialog = ProgressDialog.show(context,"Wysyłanie wiadomości","Proszę czekać...",false,false);
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        //Dismissing the progress dialog
        progressDialog.dismiss();
        //Showing a success message
        if (ok) {
            Toast.makeText(context,"Wiadomość wysłano!",Toast.LENGTH_LONG).show();
            context.finish();
        }
        else {
            Toast.makeText(context,"Błąd podczas wysyłania!",Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected Void doInBackground(Void... params) {
        try {
            //Creating MimeMessage object
            MimeMessage mm = new MimeMessage(Dane.smtpSession);
            //Setting sender address
            mm.setFrom(new InternetAddress(Dane.userEmail()));
            //Adding receiver
            mm.addRecipient(Message.RecipientType.TO, new InternetAddress(email));
            //Adding subject
            mm.setSubject(subject);
            //Adding message
            mm.setText(message);

            //Sending email
            Transport.send(mm);

        } catch (MessagingException e) {
            ok = false;
        }
        return null;
    }
}
