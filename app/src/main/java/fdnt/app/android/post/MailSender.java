package fdnt.app.android.post;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.util.Properties;

import javax.mail.Flags;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import fdnt.app.android.utils.GlobalUtil;
import fdnt.app.android.R;

public class MailSender extends AppCompatActivity {

    private EditText addressView;
    private EditText subjectView;
    private EditText contentView;
    private String path;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mail_sender2);

        Toolbar toolbar = findViewById(R.id.toolbar_sender);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        /*if (PreferenceManager
                .getDefaultSharedPreferences(GlobalUtil.ta_aktywnosc)
                .getBoolean("dark_mode", true)) {
            setTheme(R.style.AppTheme_NoActionBarDark);
        }*/

        addressView = findViewById(R.id.send_to);
        subjectView = findViewById(R.id.send_subject);
        contentView = findViewById(R.id.send_content);
        TextView myAdress = findViewById(R.id.my_email);

        myAdress.setText(GlobalUtil.userEmail());

        Bundle extras = getIntent().getExtras();
        if (extras != null && extras.getString("to") != null) {
            addressView.setText(extras.getString("to"));
        }
    }

    public void send(View view) {


        String address = addressView.getText().toString();
        String subject = subjectView.getText().toString();
        String content = contentView.getText().toString();

        if (address.isEmpty()) {
            Toast.makeText(this,"Podaj adres!",Toast.LENGTH_LONG).show();
        }
        else if (subject.isEmpty()) {
            Toast.makeText(this,"Podaj temat!",Toast.LENGTH_LONG).show();
        }
        else {
            Send sm = new Send(this, address, subject, content);
            sm.execute();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()== android.R.id.home) {
            saveDraft();
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        saveDraft();
        super.onBackPressed();
    }

    public void getPath(View view) {
        Intent chooseFile = new Intent(Intent.ACTION_GET_CONTENT);
        chooseFile.setType("*/*");
        chooseFile = Intent.createChooser(chooseFile, "Wybierz plik");
        startActivityForResult(chooseFile, 0);
    }

    private void saveDraft() {
        String address = addressView.getText().toString();
        String subject = subjectView.getText().toString();
        String content = contentView.getText().toString();

        try {
            final MimeMessage mm = new MimeMessage(GlobalUtil.smtpSession);
            mm.setFrom(new InternetAddress(GlobalUtil.userEmail()));
            mm.addRecipient(Message.RecipientType.TO, new InternetAddress(address));
            mm.setSubject(subject);
            mm.setText(content);

            SharedPreferences data = getSharedPreferences("post", Context.MODE_PRIVATE);
            final String pass = data.getString("pass", "");
            final String email = GlobalUtil.userEmail();

            new Thread(new Runnable() {
                public void run() {
                    try {
                        Session session = Session.getDefaultInstance(new Properties());
                        Store store = session.getStore("imaps");
                        store.connect("mail.dzielo.pl", 993, email, pass);

                        //create the folder object and open it
                        Folder emailFolder = store.getFolder("DRAFTS");
                        emailFolder.open(Folder.READ_WRITE);

                        mm.setFlag(Flags.Flag.DRAFT, true);
                        MimeMessage draftMessages[] = {mm};
                        emailFolder.appendMessages(draftMessages);
                    }
                    catch (MessagingException e) {
                    }
                }
            }).start();
        } catch (MessagingException e) {
            Toast.makeText(this,"Błąd podczas zapisywania!",Toast.LENGTH_LONG).show();
        }
    }
}