package fdnt.app.android;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Logowanie extends AppCompatActivity {


    //To jest obiekt do uwierzytelniania
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logowanie);

        /*if (PreferenceManager
                .getDefaultSharedPreferences(Dane.ta_aktywnosc)
                .getBoolean("dark_mode", true)) {
            Log.d("style", "logg");
            setTheme(R.style.AppTheme_NoActionBarDark);
        }*/

        Toolbar toolbar = findViewById(R.id.toolbar_logging);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null)
            reset();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()== android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    // uruchamia się gdy zostanie przyciśnięty przysisk
    public void onLogInPressed(View view) {
        EditText editTextEmail = findViewById(R.id.email);
        String email = editTextEmail.getText().toString();
        EditText editTextPassword = findViewById(R.id.password);
        String password = editTextPassword.getText().toString();
        EditText editTextMailPassword = findViewById(R.id.mail_password);
        String mailPassword = editTextMailPassword.getText().toString();

        if (!email.equals("")) {
            LoggingTask logging = new LoggingTask(email, password, mailPassword, this);
            logging.execute();
        }
    }

    private void reset() {
        Intent intent = new Intent(this, MainFrame.class);
        startActivity(intent);
        Dane.this_activity.finish();
        finish();
    }

    public void onNewUserPressed(View view) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(Logowanie.this);
        alertDialog.setMessage("Najpierw upewnij się, że Twój email jest już w bazie. " +
                "Aby ustawić hasło do aplikacji wpisz poniżej swój adres mailowy w domenie dzielo.pl " +
                "i kliknij WYŚLIJ. \nNa podany adres dostaniesz maila z linkiem do ustawienia hasła. " +
                "W przypadku niepowodzenia lub pytań skontaktuj się z administratorami aplikacji.");

        final EditText input = new EditText(Logowanie.this);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        input.setLayoutParams(lp);
        alertDialog.setView(input);

        alertDialog.setNegativeButton("Anuluj",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

        alertDialog.setPositiveButton("Wyślij",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        String email = input.getText().toString();
                        FirebaseAuth.getInstance().sendPasswordResetEmail(email);
                        dialog.cancel();
                    }
                });

        alertDialog.show();
    }
}