package fdnt.app.android;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.iid.FirebaseInstanceId;

import java.io.IOException;

public class UstawieniaAX extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Dane.aktywnosc_ustawienia = this;

        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.settings, new SettingsFragment())
                .commit();
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

    }


    public static class SettingsFragment extends PreferenceFragmentCompat {
        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey);

            Preference resetPref = (Preference) findPreference("reset");

            if(FirebaseAuth.getInstance().getCurrentUser() == null)
                resetPref.setVisible(false);
            else
                resetPref.setVisible(true);

            resetPref.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                public boolean onPreferenceClick(Preference preference) {

                    Dane.aktywnosc_ustawienia.resetPassword();
                    return true;
                }
            });

            final Preference pushPref = findPreference("powiadomienia_zezwolenie");
            pushPref.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                public boolean onPreferenceClick(Preference preference) {

                    if (PreferenceManager
                            .getDefaultSharedPreferences(Dane.aktywnosc_ustawienia)
                            .getBoolean("powiadomienia_zezwolenie", true)){


                        FirebaseInstanceId.getInstance().getToken();
                    }
                    else {

                        new Thread() {
                            public void run() {
                                try {
                                    FirebaseInstanceId.getInstance().deleteInstanceId();
                                }
                                catch (IOException e) {
                                    //...
                                }
                            }
                        }.run();


                    }
                    return true;
                }
            });


        }



    }

    public void resetPassword() {

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseAuth.getInstance().sendPasswordResetEmail(user.getEmail());

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Wiadomość z linkiem do zmiany hasła została wysłana na Twoją skrzynkę mailową.");
        builder.setCancelable(true);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
            }
        });
        builder.show();
    }
}