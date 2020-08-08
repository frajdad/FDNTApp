package fdnt.app.android.post;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import fdnt.app.android.Dane;
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
                .getDefaultSharedPreferences(Dane.ta_aktywnosc)
                .getBoolean("dark_mode", true)) {
            setTheme(R.style.AppTheme_NoActionBarDark);
        }*/

        addressView = findViewById(R.id.send_to);
        subjectView = findViewById(R.id.send_subject);
        contentView = findViewById(R.id.send_content);
        TextView myAdress = findViewById(R.id.my_email);

        myAdress.setText(Dane.userEmail());

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
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    public void getPath(View view) {
        Intent chooseFile = new Intent(Intent.ACTION_GET_CONTENT);
        chooseFile.setType("*/*");
        chooseFile = Intent.createChooser(chooseFile, "Choose a file");
        startActivityForResult(chooseFile, 0);
    }
}