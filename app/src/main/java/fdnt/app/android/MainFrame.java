package fdnt.app.android;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.preference.PreferenceManager;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Map;
import java.util.Set;

import fdnt.app.android.ui.main.DlaDarczyncy;
import fdnt.app.android.ui.main.Kontakt;
import fdnt.app.android.ui.main.MaterialyPrasowe;
import fdnt.app.android.ui.main.NaszPatron;
import fdnt.app.android.ui.main.OFundacji;
import fdnt.app.android.ui.main.WebTab;

public class MainFrame extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    private FirebaseAnalytics mFirebaseAnalytics;
    private NavigationView navigationView;
    private SharedPreferences preferences;
    private SharedPreferences drawerNames, drawerActions, drawerIcons;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Bundle tabInfo = new Bundle();
        tabInfo.putString("adress", "https://dzielo.pl/");
        WebTab mainTab = WebTab.newInstance();
        mainTab.setArguments(tabInfo);
        setTitle("FDNT");

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, mainTab)
                    .commitNow();
        }

        //Pasek górny
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Pasek poczny z opcjami
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //Tutaj ustawiam widzialnosc poszczegolnych elementow paska bocznego
        Menu nav_Menu = navigationView.getMenu();
        if (Dane.ifLogged()) {
            nav_Menu.findItem(R.id.opcje_dla_zalogowanych).setVisible(true);
        }
        else {
            nav_Menu.findItem(R.id.opcje_dla_zalogowanych).setVisible(false);
        }

        drawerNames = getSharedPreferences(Dane.userName()+"name", MODE_PRIVATE); //id->name
        drawerActions = getSharedPreferences(Dane.userName()+"act", MODE_PRIVATE); //name->site
        drawerIcons = getSharedPreferences(Dane.userName()+"icon", MODE_PRIVATE); //name->icon
        adjustTabs();

        //Pobieramy treści związane z tym, jakie zakładki wyświetlić (w osobnym wątku).
        new Thread(new Runnable() {
            public void run()
            {
                updateTabs();
            }
        }).start();

        Dane.ta_aktywnosc = this;
        preferences = PreferenceManager.getDefaultSharedPreferences(this);

        //wysyłąnie danych do analizy w Firebase
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        Bundle bundle = new Bundle();
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);


        displayNotifications();
    }

    public void restart() {
        finish();
        startActivity(getIntent());
    }

    private void displayNotifications() {
        try {
            String tekst = (String) getIntent().getExtras().get("value");

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage(Html.fromHtml("<i>" + tekst + "</i>"));

            AlertDialog dialog = builder.create();
            dialog.show();
        }
        catch (NullPointerException e) {
        }
    }


    //Co się dzieje jak klikamy "wstecz"
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }
        else {
            super.onBackPressed();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);

        //tu robimy cos, żeby było widać odpowiednie napisy na samej górze paska bocznego
        //nie wiem czy to najlepsze miejsce na to, ale działa
        if (Dane.ifLogged()) {
            TextView email_text = findViewById(R.id.miejsce_na_email);
            email_text.setText(Dane.userEmail());
            TextView name_text = findViewById(R.id.miejsce_logowanie);
            name_text.setText("Wyloguj się");
        } else {
            TextView email_text = findViewById(R.id.miejsce_na_email);
            email_text.setText(" ");
            TextView name_text = findViewById(R.id.miejsce_logowanie);
            name_text.setText("Zaloguj się");
        }

        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    //Wczytujemy z pamięci telefonu i ładujemy zakładki do jakich mamy dostęp
    private void adjustTabs() {
        Menu nav_Menu = navigationView.getMenu();

        Set<String> items = drawerNames.getAll().keySet();
        for(String item: items) {
            int order = Integer.parseInt(item);
            if (nav_Menu.findItem(order) == null) {
                String name = drawerNames.getString(item, "!");
                MenuItem newItem = nav_Menu.add(R.id.opcje_dla_zalogowanych, order, order, name);
                newItem.setIcon(getIcon(drawerIcons.getInt(name, 0)));
                newItem.setCheckable(true); //Chcemy aby zakładka się zaznaczała po pliknięciu
            }
        }
    }

    //Dostosowuje jedną zakładkę
    private void adjustTab(String id, String name) {
        Menu nav_Menu = navigationView.getMenu();
        MenuItem item = nav_Menu.findItem(Integer.parseInt(id));
        if (item != null) {
            item.setIcon(getIcon(drawerIcons.getInt(name, 0)));
        }
    }

    private int getIcon(int nr) {
        switch (nr) {
            case 1:
                return R.mipmap.ogloszenia_ogolne;
            case 2:
                return R.mipmap.materialy;
            case 3:
                return R.mipmap.ogloszenia_wspolnotowe;
            case 4:
                return R.mipmap.formacja;
            default:
                return R.mipmap.materialy_prasowe;
        }
    }

    private void updateTabs() {
        if(FirebaseAuth.getInstance().getCurrentUser() == null)
            return;

        DatabaseReference mRef = FirebaseDatabase.getInstance().getReference();
        DatabaseReference tabsData = mRef.child("users").child(Dane.userName());

        tabsData.addValueEventListener(new ValueEventListener() {
            Map<String, Object> tabs;

            //Najpierw aktualizujemy zakładki użytkownika
            @Override
            public void onDataChange(DataSnapshot data) {
                tabs = (Map<String, Object>) data.getValue();
                SharedPreferences.Editor nameEdit = drawerNames.edit();
                nameEdit.clear();

                for(String name: tabs.keySet()) {
                    nameEdit.putString((String) tabs.get(name), name);
                    updateTabInfo(name, (String) tabs.get(name));
                }
                nameEdit.commit();
                adjustTabs();
            }

            @Override
            public void onCancelled(DatabaseError error) {

            }
        });
    }

    private void updateTabInfo(final String name, final String id) {
        DatabaseReference mRef = FirebaseDatabase.getInstance().getReference().child("tabs").child(name);
        DatabaseReference siteRef = mRef.child("site");
        DatabaseReference iconRef = mRef.child("icon");

        siteRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot data) {
                SharedPreferences.Editor actionEdit = drawerActions.edit();
                actionEdit.putString(name, data.getValue(String.class));
                actionEdit.commit();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

        iconRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot data) {
                SharedPreferences.Editor iconEdit = drawerIcons.edit();
                iconEdit.putInt(name, data.getValue(Integer.class));
                iconEdit.commit();

                adjustTab(id, name);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    //tutaj ustawiamy co się dzieje jak coś klikniemy w bocznym pasku
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        String name = drawerNames.getString(Integer.toString(id), null);

        Bundle tabInfo = new Bundle();
        Fragment newInstance = WebTab.newInstance(); //Jak się pozbędziemy zakomentowanych rzeczy w switch to trzeba usunąć inicjalizajcę tu

        if (name == null) {
            switch (id) {
                case R.id.nav_main_menu:
                    newInstance = WebTab.newInstance();
                    setTitle("FDNT");
                    tabInfo.putString("adress", "https://dzielo.pl/");
                    break;
                case R.id.nav_o_fundacji:
                    newInstance = OFundacji.newInstance();
                    setTitle("O Fundacji");
                    break;
                case R.id.nav_nasz_patron:
                     newInstance = NaszPatron.newInstance();
                    setTitle("Nasz Patron");
                    break;
                case R.id.nav_dla_darczyncy:
                    newInstance = DlaDarczyncy.newInstance();
                    setTitle("Dla Darczyńcy");
                    break;
                case R.id.nav_materialy_prasowe:
                    newInstance = MaterialyPrasowe.newInstance();
                    setTitle("Materiały Prasowe");
                    break;
                case R.id.nav_kontakt:
                    newInstance = Kontakt.newInstance();
                    setTitle("Kontakt");
                    break;
                default:
                    newInstance = WebTab.newInstance();
            }
        }
        else {
            String site = drawerActions.getString(name, "");
            newInstance = WebTab.newInstance();
            setTitle(name);
            tabInfo.putString("adress", site);
        }

        newInstance.setArguments(tabInfo);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, newInstance)
                .commitNow();

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void logInOut(View view) {
        if (!Dane.ifLogged()) {
            Intent intent = new Intent(this, Logowanie.class);
            startActivity(intent);
        } else {
            FirebaseAuth.getInstance().signOut();
            restart();
        }
    }

    public void startUstawienia(MenuItem item) {
        Intent intent = new Intent(this, UstawieniaAX.class);
        startActivity(intent);
    }




}