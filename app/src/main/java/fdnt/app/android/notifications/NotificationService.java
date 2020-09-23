package fdnt.app.android.notifications;

import android.app.IntentService;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.Set;

import javax.mail.Message;
import javax.mail.MessagingException;

import fdnt.app.android.MainFrame;
import fdnt.app.android.R;
import fdnt.app.android.post.AsyncMailLoad;
import fdnt.app.android.utils.GlobalUtil;

public class NotificationService extends IntentService {
    public NotificationService() {
        super("NotificationService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        checkPost();
        checkNews();
    }

    private void checkPost() {
        if (GlobalUtil.ifLoggedToPost()) {
            SharedPreferences data = getSharedPreferences("last_mails", Context.MODE_PRIVATE);
            long oldTime = data.getLong("time", 0);

            Message last = AsyncMailLoad.getEmails("INBOX", 1, this);
            if (last != null) {
                try {
                    long newTime = last.getSentDate().getTime();
                    if (newTime > oldTime) {
                        SharedPreferences.Editor editor = data.edit();
                        editor.putLong("time", newTime);
                        editor.apply();

                        if (oldTime !=0) {
                            notification(last.getSubject(), "Kliknij aby otworzyć e-mail", "post");
                        }
                    }
                } catch (MessagingException e) {
                }
            }
        }
    }

    private void checkNews() {
        if (GlobalUtil.ifLogged()) {
            SharedPreferences drawerNames = getSharedPreferences(GlobalUtil.userName() + "name", MODE_PRIVATE); //id->name
            SharedPreferences drawerActions = getSharedPreferences(GlobalUtil.userName() + "act", MODE_PRIVATE); //name->site

            SharedPreferences data = getSharedPreferences("last_news", Context.MODE_PRIVATE);

            Set<String> items = drawerNames.getAll().keySet();
            if (items.size() == 0) return;

            for (String item : items) {
                String name = drawerNames.getString(item, "!");
                String site = drawerActions.getString(name, "");

                long oldTime = data.getLong(name, 0);
                long newTime = getLastModified(site);

                if (newTime > oldTime) {
                    SharedPreferences.Editor editor = data.edit();
                    editor.putLong(name, newTime);
                    editor.apply();

                    if (oldTime != 0) {
                        notification("Sprawdź zakładkę " + name, "Zawartość " + name + " została zmodyfikowana", name);
                    }
                }
            }
        }
    }

    public static long getLastModified(String site) {
       Connection con = Jsoup.connect(site);
        try {
            Document doc = con.get();
            String timeString =
                doc.getElementsByClass("LqzjUe").attr("data-last-updated-at-time");
            return Long.parseLong(timeString);
        } catch (IOException e) {
            return 0;
        }
    }

    private void notification(String title, String content, String tab) {
        Intent intent = new Intent(this, MainFrame.class);
        intent.putExtra("fragment", tab);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        createNotificationChannel();
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_launcher)
                .setContentTitle(title)
                .setContentText(content)
                .setContentIntent(pendingIntent)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true)
                .setVibrate(new long[] {200, 100, 300, 700});

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        notificationManager.notify((int) (Math.random()*1000000), builder.build());
    }

    private final String CHANNEL_ID = "FDNT_NEWS_CHANNEL";
    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "FDNT Powiadomienie";
            String description = "Nowe aktualności lub wiadomości";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
}