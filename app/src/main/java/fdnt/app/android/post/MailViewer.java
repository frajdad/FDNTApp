package fdnt.app.android.post;

import android.os.Bundle;
import android.os.Message;
import android.view.MenuItem;
import android.webkit.WebView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
        WebView view_content = findViewById(R.id.view_content);
        TextView view_date = findViewById(R.id.view_date);
        TextView view_circle = findViewById(R.id.name_circle);
        TextView view_to = findViewById(R.id.view_to);

        Bundle extras = getIntent().getExtras();

        int nr = Integer.parseInt(extras.getString("nr"));

        view_sender.setText(AsyncMailLoad.ITEMS.get(nr).sender);
        view_circle.setText(AsyncMailLoad.ITEMS.get(nr).sender.substring(0,1).toUpperCase());
        view_subject.setText(AsyncMailLoad.ITEMS.get(nr).subject);
        view_date.setText(AsyncMailLoad.ITEMS.get(nr).date);
        view_to.setText("do mnie");

        String html = AsyncMailLoad.ITEMS.get(nr).content;
        System.out.println(html);
        view_content.getSettings().setJavaScriptEnabled(true);
        view_content.loadData(html, "text/html", "UTF-8");
    }

    private String loadImages(String html, Message message) {
        Queue<String> cids = new LinkedBlockingQueue<String>();
        Pattern p = Pattern.compile("cid:[^\"]+");
        Matcher m = p.matcher(html);
        while (m.find()) {
            cids.add(m.group());
        }
        return "";
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()== android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}