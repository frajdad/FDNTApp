package fdnt.app.android;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import fdnt.app.android.post.MailSender;

public class UstawieniaAX extends PreferenceFragmentCompat {
        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey);

            Preference resetPref = (Preference) findPreference("reset");
            Preference sendPref = (Preference) findPreference("send");

            if(FirebaseAuth.getInstance().getCurrentUser() == null)
                resetPref.setVisible(false);
            else
                resetPref.setVisible(true);

            resetPref.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                public boolean onPreferenceClick(Preference preference) {
                    resetPassword();
                    return true;
                }
            });

            sendPref.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    sendComment();
                    return true;
                }
            });

            final Preference pushPref = findPreference("powiadomienia_zezwolenie");
            pushPref.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                public boolean onPreferenceClick(Preference preference) {

                 /*   if (PreferenceManager
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


                    }*/
                    return true;
                }
            });


        }


    public void resetPassword() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseAuth.getInstance().sendPasswordResetEmail(user.getEmail());

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("Wiadomość z linkiem do zmiany hasła została wysłana na Twoją skrzynkę mailową.");
        builder.setCancelable(true);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
            }
        });
        builder.show();
    }

    public void sendComment() {
            Intent intent = new Intent(getActivity(), MailSender.class);
            intent.putExtra("to", "aplikacja@dzielo.pl");
            startActivity(intent);
    }
}