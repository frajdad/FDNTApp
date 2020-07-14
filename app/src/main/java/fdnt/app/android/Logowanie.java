package fdnt.app.android;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Logowanie extends AppCompatActivity {


    //To jest obiekt do uwierzytelniania
    private FirebaseAuth mAuth;
    //To jest obiekt, który pokazjuje kręcące się kóło na czas logowania
    private ProgressDialog mProgress;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logowanie);

        mAuth = FirebaseAuth.getInstance();

        mProgress = new ProgressDialog(this);
        mProgress.setTitle("Logowanie");
        mProgress.setMessage("Proszę czekać...");
        mProgress.setCancelable(false);
        mProgress.setIndeterminate(true);
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null)
            reset();
    }

    // uruchamia się gdy zostanie przyciśnięty przysisk
    public void onLogInPressed(View view) {
        mProgress.show();
        try {
            EditText editTextEmail = (EditText) findViewById(R.id.email);
            String email = editTextEmail.getText().toString();
            EditText editTextPassword = (EditText) findViewById(R.id.password);
            String password = editTextPassword.getText().toString();
            EditText editTextMailPassword = (EditText) findViewById(R.id.mail_password);
            String mailPassword = editTextMailPassword.getText().toString();

            logIn(email, password, mailPassword);
        }
        catch (Exception e) {
            TextView zle_dane = findViewById(R.id.zle_dane);
            zle_dane.setVisibility(View.VISIBLE);
            mProgress.dismiss();
        }
    }

    private void logIn(String email, String password, final String mailPass) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            SharedPreferences data = getSharedPreferences("post", MODE_PRIVATE);
                            SharedPreferences.Editor dataEdit = data.edit();
                            dataEdit.putString("pass", mailPass);
                            dataEdit.apply();
                            reset();
                        } else {
                            mProgress.dismiss();
                            TextView zle_dane = findViewById(R.id.zle_dane);
                            zle_dane.setVisibility(View.VISIBLE);
                        }

                    }
                });
    }

    private void reset() {
        Intent intent = new Intent(this, MainFrame.class);
        startActivity(intent);
        Dane.ta_aktywnosc.finish();
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