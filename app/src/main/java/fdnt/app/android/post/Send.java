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

import fdnt.app.android.utils.GlobalUtil;


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
            MimeMessage mm = new MimeMessage(GlobalUtil.smtpSession);
            mm.setFrom(new InternetAddress(GlobalUtil.userEmail()));
            mm.addRecipient(Message.RecipientType.TO, new InternetAddress(email));
            mm.setSubject(subject);
            mm.setText(message);

            //Sending email
            Transport.send(mm);


        } catch (MessagingException e) {
            ok = false;
        }
        return null;
    }
}
