package fdnt.app.android;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import fdnt.app.android.utils.GlobalUtil;


public class Logowanie extends AppCompatActivity {
    
    //To jest obiekt do uwierzytelniania
    private FirebaseAuth mAuth;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logowanie);

        /*if (PreferenceManager
                .getDefaultSharedPreferences(GlobalUtil.ta_aktywnosc)
                .getBoolean("dark_mode", true)) {
            Log.d("style", "logg");
            setTheme(R.style.AppTheme_NoActionBarDark);
        }*/

        Toolbar toolbar = findViewById(R.id.toolbar_logging);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        /*TextView contact_to_admin = findViewById(R.id.contact_to_admin);

        contact_to_admin.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });*/
        TextWatcher textWatcher = new TextWatcher () {
            @Override
            public void beforeTextChanged (CharSequence s, int start, int count, int after) {
            
            }
        
            @Override
            public void onTextChanged (CharSequence s, int start, int before, int count) {
                String email = ((EditText) findViewById(R.id.email)).getText().toString();
                String password = ((EditText) findViewById(R.id.password)).getText().toString();
                if(GlobalUtil.isProperEmailAndPasswordProvided (email, password))
                    findViewById (R.id.email_sign_in_button).setBackgroundColor (ContextCompat.getColor(getApplicationContext (),
                            R.color.activeButton));
                else findViewById (R.id.email_sign_in_button).setBackgroundColor (ContextCompat.getColor(getApplicationContext (),
                        R.color.inactiveButton));
            
            }
        
            @Override
            public void afterTextChanged (Editable s) {
            
            }
        };
        ((AutoCompleteTextView) findViewById (R.id.email)).addTextChangedListener (textWatcher);
        ((EditText) findViewById (R.id.password)).addTextChangedListener (textWatcher);
    
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
        if(!GlobalUtil.isProperEmailAndPasswordProvided (email, password)) return;
        if (GlobalUtil.isValidEmailAddr(email)) {
            LoggingTask logging = new LoggingTask(email, password, "", this);
            logging.execute();
        } else {
            Toast.makeText(this, "Zły login aplikacji", Toast.LENGTH_LONG).show();
        }
    }

    private void reset() {
        Intent intent = new Intent(this, MainFrame.class);
        intent.putExtra("post_log", "log");
        startActivity(intent);
        GlobalUtil.this_activity.finish();
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


        alertDialog.setNegativeButton("Anuluj", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        alertDialog.setPositiveButton("Wyślij", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String email = input.getText().toString();
                FirebaseAuth.getInstance().sendPasswordResetEmail(email);
                dialog.cancel();
            }
        });

        alertDialog.show();
    }
    
}