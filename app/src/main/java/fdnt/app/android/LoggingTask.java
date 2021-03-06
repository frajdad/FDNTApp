package fdnt.app.android;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.concurrent.Semaphore;

import fdnt.app.android.utils.GlobalUtil;

//Class is extending AsyncTask because this class is going to perform a networking operation
public class LoggingTask extends AsyncTask<Void,Void,Void> {

    private Activity context;

    private ProgressDialog progressDialog;
    private short ok;

    private String email;
    private String password;
    private String mailPass;

    public LoggingTask(String email, String pass, String mailPass, Activity context) {
        this.email = email;
        this.password = pass;
        this.mailPass = mailPass;
        this.context = context;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        Log.d("login", email);

        progressDialog = ProgressDialog.show(context,"Logowanie","Proszę czekać...",false,false);
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        progressDialog.dismiss();
        switch (ok) {
            case 2:
                Toast.makeText(context, "Zły login lub hasło aplikacji", Toast.LENGTH_LONG).show();
                break;
        }
    }

    @Override
    protected Void doInBackground(Void... params) {
        // Zmienna określa kod błędu
        ok = 0;
        // Semafor do synchronizacji logowania do poczty i Firebase
        final Semaphore semaphore = new Semaphore(0);
        
        if (ok == 0) {
            try {
                FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(context, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (!task.isSuccessful()) {
                                    ok = 2;
                                }
                                else {
                                    reset();
                                }
                                semaphore.release();
                            }
                        });
            }
            catch (Exception e) {
                ok = 2;
            }

            try {
                semaphore.acquire();
            } catch (InterruptedException e) {
                ok = 2;
            }
        }

        return null;
    }

    private void reset() {
        Intent intent = new Intent(context, MainFrame.class);
        intent.putExtra("post_log", "log");
        context.startActivity(intent);
        GlobalUtil.this_activity.finish();
        context.finish();
    }
}
