package fdnt.app.android.ui.main.recview;
// wspólne metody dla obu adapterów

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import fdnt.app.android.Dane;
import fdnt.app.android.post.MailSender;

public class Static {
    public static void sendMail(String mail, Context context) {
        if(Dane.ifLoggedToPost()) {
            Intent intent = new Intent(Dane.this_activity, MailSender.class);
            intent.putExtra("to", mail);
            Dane.this_activity.startActivity(intent);
        }
        else {
            String mailURL = "mailto:" + mail;
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(mailURL));
            context.startActivity(intent);
        }
    }
}
