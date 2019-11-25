package fdnt.app.android;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class WyswietlaniePowiadomien extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wyswietlanie_powiadomien);

        String tekst = getIntent().getExtras().getParcelable("tresc").toString();
        wyświetl(tekst);
    }

    public void wyświetl(String tekst) {

        TextView treść = findViewById(R.id.tekst_powiadomienia);
        treść.setText(tekst);
    }
}
