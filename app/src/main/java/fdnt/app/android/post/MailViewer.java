package fdnt.app.android.post;

import android.os.Bundle;
import android.os.Environment;
import android.view.MenuItem;
import android.webkit.WebView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMultipart;

import fdnt.app.android.R;

public class MailViewer extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mail_viewer);
        Toolbar toolbar = findViewById(R.id.toolbar_viewer);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        /*if (PreferenceManager
                .getDefaultSharedPreferences(GlobalUtil.ta_aktywnosc)
                .getBoolean("dark_mode", true)) {
            setTheme(R.style.AppTheme_NoActionBarDark);
        }*/

        TextView view_sender = findViewById(R.id.view_sender);
        TextView view_subject = findViewById(R.id.view_subject);
        TextView view_date = findViewById(R.id.view_date);
        final TextView view_circle = findViewById(R.id.name_circle);
        TextView view_to = findViewById(R.id.view_to);

        Bundle extras = getIntent().getExtras();

        final int nr = Integer.parseInt(extras.getString("nr"));

        view_sender.setText(AsyncMailLoad.ITEMS.get(nr).sender);
        view_circle.setText(AsyncMailLoad.ITEMS.get(nr).sender.substring(0,1).toUpperCase());
        view_subject.setText(AsyncMailLoad.ITEMS.get(nr).subject);
        view_date.setText(AsyncMailLoad.ITEMS.get(nr).date);
        view_to.setText("do mnie");

        final WebView view_content = findViewById(R.id.view_content);
        view_content.getSettings().setJavaScriptEnabled(true);
        final String html = AsyncMailLoad.ITEMS.get(nr).content;
        view_content.loadData(html, "text/html", "UTF-8");
     /*   new Thread(new Runnable() {
            public void run() {
                try {
                    final String newHtml = loadImages(html, AsyncMailLoad.ITEMS.get(nr).message, view_content);

                    view_content.post(new Runnable() {
                        @Override
                        public void run() {
                            System.out.println(newHtml);
                            view_content.loadData(newHtml, "text/html", "UTF-8");
                        }
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (MessagingException e) {
                    e.printStackTrace();
                }
            }
        }).start();*/
    }

    private String loadImages(String html, Message message, WebView view) throws IOException, MessagingException {
        Queue<BodyPart> images = new LinkedBlockingQueue<>();
        String rv = html;
        getImages((MimeMultipart) message.getContent(), images);
        for (BodyPart bodyPart: images) {
            InputStream is = bodyPart.getInputStream();
            File f = new File(Environment.getDownloadCacheDirectory(), bodyPart.getFileName());
            f.createNewFile();
            FileOutputStream fos = new FileOutputStream(f);
            byte[] buf = new byte[4096];
            int bytesRead;
            while((bytesRead = is.read(buf))!=-1) {
                fos.write(buf, 0, bytesRead);
            }
            fos.close();
           // html += "<img src=\"" + f.getAbsolutePath() + "\">";
           //rv = html.replaceAll("cid:[^\"]+", f.getCanonicalPath());
            rv = f.getPath();
        }

   /*     Queue<String> cids = new LinkedBlockingQueue<String>();
        Pattern p = Pattern.compile();
        Matcher m = p.matcher(html);
        while (m.find()) {
            cids.add(m.group());
        }*/


        return rv;
    }

    private void getImages(MimeMultipart mimeMultipart, Queue<BodyPart> images) throws MessagingException, IOException {
        int count = mimeMultipart.getCount();
        for (int i = 0; i < count; i++) {
            BodyPart bodyPart = mimeMultipart.getBodyPart(i);

            if (bodyPart.isMimeType("image/*")) {
                images.add(bodyPart);
            } else if (bodyPart.getContent() instanceof MimeMultipart) {
                getImages((MimeMultipart) bodyPart.getContent(), images);
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()== android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}