package fdnt.app.android.post;

import android.os.Bundle;
import android.view.MenuItem;
import android.webkit.WebView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import fdnt.app.android.R;

public class MailViewer extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mail_viewer);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        TextView view_sender = (TextView) findViewById(R.id.view_sender);
        TextView view_subject = (TextView) findViewById(R.id.view_subject);
        WebView view_content = (WebView) findViewById(R.id.view_content);
        TextView view_date = (TextView) findViewById(R.id.view_date);

        Bundle extras = getIntent().getExtras();

        view_sender.setText(extras.getString("sender"));
        view_subject.setText(extras.getString("subject"));
        view_date.setText(extras.getString("date"));

        view_content.getSettings().setJavaScriptEnabled(true);
        view_content.loadDataWithBaseURL("", extras.getString("content"), "text/html", "UTF-8", "");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()== android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}