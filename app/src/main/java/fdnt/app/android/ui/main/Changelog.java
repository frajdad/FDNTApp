package fdnt.app.android.ui.main;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import fdnt.app.android.R;

public class Changelog extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_changelog);
        String[] changenotes = getResources().getStringArray(R.array.changenotes);
        ListView listView = (ListView)findViewById(R.id.listview);
        ArrayAdapter<String> changelogListAdapter= new ArrayAdapter<String>(this, R.layout.changelog_row, R.id.textview, changenotes);
        listView.setAdapter(changelogListAdapter);
    }
}