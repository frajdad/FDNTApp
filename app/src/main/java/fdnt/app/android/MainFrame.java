package fdnt.app.android;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

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
import java.util.concurrent.Semaphore;

import javax.mail.MessagingException;

import fdnt.app.android.notifications.Util;
import fdnt.app.android.post.MailLogging;
import fdnt.app.android.post.MailSender;
import fdnt.app.android.post.PostItemFragment;
import fdnt.app.android.ui.main.AboutPatron;
import fdnt.app.android.ui.main.CoRobimy;
import fdnt.app.android.ui.main.DzienPapieski;
import fdnt.app.android.ui.main.FoundationContact;
import fdnt.app.android.ui.main.GdzieJestesmy;
import fdnt.app.android.ui.main.HelpNowTab;
import fdnt.app.android.ui.main.HowToHelpTab;
import fdnt.app.android.ui.main.JanPawelIi;
import fdnt.app.android.ui.main.KimJestemy;
import fdnt.app.android.ui.main.KontaktBiuro;
import fdnt.app.android.ui.main.KontaktZarzad;
import fdnt.app.android.ui.main.MailLogIn;
import fdnt.app.android.ui.main.Modlitwy;
import fdnt.app.android.ui.main.WebTab;
import fdnt.app.android.ui.main.recview.RecViewUtil;
import fdnt.app.android.utils.GlobalUtil;

public class MainFrame extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    private FirebaseAnalytics mFirebaseAnalytics;
    private NavigationView navigationView;
    private SharedPreferences preferences;
    private SharedPreferences drawerNames, drawerActions, drawerIcons;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        RecViewUtil.loadStaffDataFromFile(getApplicationContext());
        setContentView(R.layout.activity_main);

        //Pasek górny
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Pasek poczny z opcjami
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        openMain(savedInstanceState);

        drawerNames = getSharedPreferences(GlobalUtil.userName() + "name", MODE_PRIVATE); //id->name
        drawerActions = getSharedPreferences(GlobalUtil.userName() + "act", MODE_PRIVATE); //name->site
        drawerIcons = getSharedPreferences(GlobalUtil.userName() + "icon", MODE_PRIVATE); //name->icon

        //Tutaj ustawiam widzialnosc poszczegolnych elementow paska bocznego
        Menu nav_Menu = navigationView.getMenu();
        if (GlobalUtil.ifLogged()) {
            nav_Menu.findItem(R.id.opcje_dla_zalogowanych).setVisible(true);

            openProperFragment();

            SharedPreferences data = getSharedPreferences("post", Context.MODE_PRIVATE);
            if (data.getString("pass", "").equals("")) {
                nav_Menu.findItem(R.id.nav_post).setVisible(false);
            }
        } else {
            nav_Menu.findItem(R.id.opcje_dla_zalogowanych).setVisible(false);
        }


   /*     if (PreferenceManager
                .getDefaultSharedPreferences(this)
                .getBoolean("dark_mode", true)) {
            getWindow().getDecorView().setBackgroundColor(getResources().getColor(R.color.checkedText));
            setTheme(R.style.AppTheme_NoActionBarDark);
        }*/

        adjustTabs();

        //Pobieramy treści związane z tym, jakie zakładki wyświetlić (w osobnym wątku).
        new Thread(new Runnable() {
            public void run() {
                updateTabs();
            }
        }).start();

        GlobalUtil.this_activity = this;
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        manageMailLogInButtonVisibility ();
        //wysyłąnie danych do analizy w Firebase
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        Bundle bundle = new Bundle();
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);
        System.gc ();
        displayNotifications();

        if (GlobalUtil.ifLogged()) {
            Util.scheduleJob(this);
        }
    }

    private void manageMailLogInButtonVisibility() {
        Menu nav_Menu = navigationView.getMenu();
        if(!GlobalUtil.ifLoggedToPost(this) && GlobalUtil.ifLogged ())
            nav_Menu.findItem (R.id.mail_log).setVisible (true);
        else {
            nav_Menu.findItem (R.id.mail_log).setVisible (false);
            nav_Menu.findItem(R.id.nav_post).setVisible(true);
        }
        
    }
    
    private void openMain(Bundle savedInstanceState) {
        Bundle tabInfo = new Bundle();
        if (PreferenceManager
                .getDefaultSharedPreferences(this)
                .getBoolean("dzielo_site", true)) {
            tabInfo.putString("adress", "https://dzielo.pl/");
        }
        else {
            tabInfo.putString("adress", "file:///android_asset/offline.html");
        }

        WebTab mainTab = WebTab.newInstance();
        mainTab.setArguments(tabInfo);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, mainTab)
                    .commitNow();
        }

        navigationView.getMenu().getItem(0).setChecked(true);
    }

    // Być może chcemy otworzyć inną zakładkę niż główną. Funkcja ta zbiera takie informacje i otwiera odpowiednią
    private void openProperFragment() {
        Bundle extras = getIntent().getExtras();

        if (extras != null) {
            Log.d("extra_fr", extras.getString("fragment", ""));
            if (!extras.getString("post_log", "").equals("")) {
                // Przenosimy do logowania do poczty
                navigationView.getMenu().findItem(R.id.mail_log).setChecked(true);
                Fragment newInstance = MailLogIn.newInstance();
                setTitle("Logowanie do poczty");
                openTab(newInstance, new Bundle());
            }
            else {
                String fragmentName = extras.getString("fragment","");

                if (fragmentName.equals("post")) {
                    openTab(new PostItemFragment(), new Bundle());
                    setTitle("Poczta");
                }
                else if (!fragmentName.equals("")) {
                    String site = drawerActions.getString(fragmentName, "");
                    Fragment newInstance = WebTab.newInstance();
                    setTitle(fragmentName);
                    Bundle tabInfo = new Bundle();
                    tabInfo.putString("adress", site);
                    openTab(newInstance, tabInfo);
                }
            }
        }
    }

    public void restart() {
        finish();
        startActivity(getIntent());
    }

    private void displayNotifications() {
        try {
            final String tekst = (String) getIntent().getExtras().getString("value", "");

            if (!tekst.equals("")) {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage(Html.fromHtml("<i>" + tekst + "</i>"));

                builder.setNeutralButton("KOPIUJ", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                        ClipData clip = ClipData.newPlainText("fdnt-powiadomienie", tekst);
                        clipboard.setPrimaryClip(clip);
                        Toast.makeText(GlobalUtil.this_activity, "Skopiowano!", Toast.LENGTH_SHORT).show();
                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();
            }
        }
        catch (NullPointerException e) {
        }
    }


    //Co się dzieje jak klikamy "wstecz"
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }
        else {
            openMain(null);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //tu robimy cos, żeby było widać odpowiednie napisy na samej górze paska bocznego
        //nie wiem czy to najlepsze miejsce na to, ale działa
        if (GlobalUtil.ifLogged()) {
            TextView email_text = findViewById(R.id.miejsce_na_email);
            email_text.setText(GlobalUtil.userEmail());
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

    //Wczytujemy z pamięci telefonu i ładujemy zakładki do jakich mamy dostęp
    private void adjustTabs() {
        Menu nav_Menu = navigationView.getMenu();
        Set<String> items = drawerNames.getAll().keySet();
        if(items == null || items.size() == 0) return;
        if (nav_Menu.findItem(Integer.parseInt(items.iterator ().next ())) != null) return;
        for(String item: items) {
            int order = Integer.parseInt (item);
            String name = drawerNames.getString(item, "!");
            MenuItem newItem = nav_Menu.add(R.id.opcje_dla_zalogowanych, order, order, name);
            newItem.setIcon(getIcon(drawerIcons.getInt(name, 0)));
            newItem.setCheckable(true); //Chcemy aby zakładka się zaznaczała po pliknięciu
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
        DatabaseReference tabsData = mRef.child("users").child(GlobalUtil.userName());

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
                int value = Integer.parseInt(data.getValue().toString());
                iconEdit.putInt(name, value);
                iconEdit.commit();

                adjustTab(id, name);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private short chosenTab = 0;

    void onOFundacjiVisibilityChange(boolean state, Menu menu) {
        menu.findItem(R.id.nav_kim_jestesmy).setVisible(state);
        menu.findItem(R.id.nav_co_robimy).setVisible(state);
        menu.findItem(R.id.nav_gdzie_jestesmy).setVisible(state);
    }

    void onNaszPatronVisibilityChange(boolean state, Menu menu) {
        menu.findItem(R.id.nav_jan_pawel).setVisible(state);
        menu.findItem(R.id.nav_my_o_patronie).setVisible(state);
        menu.findItem(R.id.nav_dzien_papieski).setVisible(state);
        menu.findItem(R.id.nav_modlitwy).setVisible(state);
    }

    void onDlaDarczyncyVisibilityChange(boolean state, Menu menu) {
        menu.findItem(R.id.nav_sposoby).setVisible(state);
        menu.findItem(R.id.nav_pomoz).setVisible(state);
    }

    void onKontaktVisibilityChange(boolean state, Menu menu) {
        menu.findItem(R.id.nav_fundacja).setVisible(state);
        menu.findItem(R.id.nav_biuro).setVisible(state);
        menu.findItem(R.id.nav_zarzad).setVisible(state);
        menu.findItem(R.id.nav_admin).setVisible(state);
    }

    void hideTabs(Menu menu) {
        onOFundacjiVisibilityChange(false, menu);
        onNaszPatronVisibilityChange(false, menu);
        onKontaktVisibilityChange(false, menu);
        onDlaDarczyncyVisibilityChange(false, menu);
    }

    void openTab(final Fragment newInstance, Bundle tabInfo) {
        newInstance.setArguments (tabInfo);
        getSupportFragmentManager ().beginTransaction ()
                .replace (R.id.container, newInstance).commitNow ();
        final DrawerLayout drawer = findViewById(R.id.drawer_layout);
        new Handler().postDelayed (new Runnable () {
            @Override
            public void run () {
                drawer.closeDrawer(GravityCompat.START);
            }
        }, 100);
    }

    //tutaj ustawiamy co się dzieje jak coś klikniemy w bocznym pasku
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        String name = drawerNames.getString(Integer.toString(id), null);

        Menu menu = navigationView.getMenu();
        Bundle tabInfo = new Bundle();
        Fragment newInstance;

        if (name == null) {
            switch (id) {
                case R.id.nav_main_menu:
                    setTitle("FDNT");
                    newInstance = WebTab.newInstance();
                    if (PreferenceManager
                            .getDefaultSharedPreferences(this)
                            .getBoolean("dzielo_site", true)) {
                        tabInfo.putString("adress", "https://dzielo.pl/");
                    }
                    else {
                        tabInfo.putString("adress", "file:///android_asset/offline.html");
                    }
                    openTab(newInstance, tabInfo);
                    break;

                case R.id.nav_o_fundacji:
                    hideTabs(menu);
                    if (chosenTab != 1) {
                    onOFundacjiVisibilityChange(true, menu);
                        chosenTab = 1;
                    }
                    else {
                        chosenTab = 0;
                    }
                    break;
                case R.id.nav_kim_jestesmy:
                    setTitle("O Fundacji");
                    newInstance = KimJestemy.newInstance();
                    openTab(newInstance, tabInfo);
                    break;
                case R.id.nav_gdzie_jestesmy:
                    setTitle("O Fundacji");
                    newInstance = GdzieJestesmy.newInstance();
                    openTab(newInstance, tabInfo);
                    break;
                case R.id.nav_co_robimy:
                    setTitle("O Fundacji");
                    newInstance = CoRobimy.newInstance();
                    openTab(newInstance, tabInfo);
                    break;

                case R.id.nav_nasz_patron:
                    hideTabs(menu);
                    if (chosenTab != 2) {
                        onNaszPatronVisibilityChange(true, menu);
                        chosenTab = 2;
                    }
                    else {
                        chosenTab = 0;
                    }
                    break;
                case R.id.nav_jan_pawel:
                    newInstance = JanPawelIi.newInstance();
                    setTitle("Nasz Patron");
                    openTab(newInstance, tabInfo);
                    break;
                case R.id.nav_my_o_patronie:
                    newInstance = AboutPatron.newInstance();
                    setTitle("Nasz Patron");
                    openTab(newInstance, tabInfo);
                    break;
                case R.id.nav_dzien_papieski:
                    newInstance = DzienPapieski.newInstance();
                    setTitle("Nasz Patron");
                    openTab(newInstance, tabInfo);
                    break;
                case R.id.nav_modlitwy:
                    newInstance = Modlitwy.newInstance();
                    setTitle("Nasz Patron");
                    openTab(newInstance, tabInfo);
                    break;

                case R.id.nav_dla_darczyncy:
                    hideTabs(menu);
                    if (chosenTab != 3) {
                        onDlaDarczyncyVisibilityChange(true, menu);
                        chosenTab = 3;
                    } else {
                        chosenTab = 0;
                    }
                    break;
                case R.id.nav_sposoby:
                    newInstance = HowToHelpTab.newInstance();
                    setTitle("Dla Darczyńcy");
                    openTab(newInstance, tabInfo);
                    break;
                case R.id.nav_pomoz:
                    newInstance = HelpNowTab.newInstance();
                    setTitle("Dla Darczyńcy");
                    openTab(newInstance, tabInfo);
                    break;

                case R.id.nav_dzielo_tv:
                    setTitle("Dzieło TV");
                    GlobalUtil.createUriIntent(navigationView.getContext(),
                            "https://www.youtube.com/user/DzieloTV");
                    break;

                case R.id.nav_kontakt:
                    hideTabs(menu);
                    if (chosenTab != 5) {
                        onKontaktVisibilityChange(true, menu);
                        chosenTab = 5;
                    } else {
                        chosenTab = 0;
                    }
                    break;
                case R.id.nav_fundacja:
                    newInstance = FoundationContact.newInstance();
                    setTitle("Kontakt");
                    openTab(newInstance, tabInfo);
                    break;
                case R.id.nav_biuro:
                    newInstance = KontaktBiuro.newInstance();
                    setTitle("Kontakt");
                    openTab(newInstance, tabInfo);
                    break;
                case R.id.nav_zarzad:
                    newInstance = KontaktZarzad.newInstance();
                    setTitle("Kontakt");
                    openTab(newInstance, tabInfo);
                    break;
                case R.id.nav_admin:
                    setTitle("Administracja");
                    GlobalUtil.sendMail("aplikacja@dzielo.pl", navigationView.getContext());
                    break;
                case R.id.nav_settings:
                    newInstance = new UstawieniaAX();
                    setTitle("Ustawienia");
                    openTab(newInstance, tabInfo);
                    break;
                case R.id.nav_post:
                    newInstance = new PostItemFragment();
                    setTitle("Poczta");
                    openTab(newInstance, tabInfo);
                    break;
                case R.id.mail_log:
                    newInstance = MailLogIn.newInstance ();
                    setTitle("Logowanie do poczty");
                    openTab(newInstance, tabInfo);
                    break;
            }
        }
        else {
            String site = drawerActions.getString(name, "");
            newInstance = WebTab.newInstance();
            setTitle(name);
            tabInfo.putString("adress", site);
            openTab(newInstance, tabInfo);
        }

        return true;
    }

    public void logInOut(View view) {
        if (!GlobalUtil.ifLogged()) {
            Intent intent = new Intent(this, Logowanie.class);
            startActivity(intent);
        } else {
            FirebaseAuth.getInstance().signOut();
            SharedPreferences.Editor mailClear = getSharedPreferences("post", MODE_PRIVATE).edit();
            mailClear.clear();
            mailClear.apply();
            restart();
        }
    }
    
    // Po kliknięciu przycisku wysyłania emaila
    public void writeEmail(View view) {
        Intent intent = new Intent(this, MailSender.class);
        startActivity(intent);
    }
    
    public void onLogInMailPressed (View view) {
        final String password = ((EditText)findViewById (R.id.mail_password)).getText ().toString ();
        final String email = GlobalUtil.userEmail ();
        final boolean[] success = {true};
        final Semaphore semaphore = new Semaphore(0);
        Thread thread = new Thread (new Runnable () {
            @Override
            public void run () {
                try {
                    MailLogging.openSessions(email, password);
                    MailLogging.testConnection(email, password);
                    SharedPreferences data = MainFrame.this.getSharedPreferences("post",
                            Context.MODE_PRIVATE);
                    SharedPreferences.Editor dataEdit = data.edit();
                    dataEdit.putString("pass", password);
                    dataEdit.apply ();
                } catch (MessagingException e) {
                    success[0] = false;
                }
                semaphore.release ();
            }
        });
        thread.start ();
        try {
            semaphore.acquire ();
            manageMailLogInButtonVisibility ();
        } catch (InterruptedException e) {
            e.printStackTrace ();
        }
        if(success[0]){
            setTitle("FDNT");
            Fragment newInstance = WebTab.newInstance();
            Bundle tabInfo = new Bundle();
            if (PreferenceManager
                    .getDefaultSharedPreferences(this)
                    .getBoolean("dzielo_site", true)) {
                tabInfo.putString("adress", "https://dzielo.pl/");
            }
            else {
                tabInfo.putString("adress", "file:///android_asset/offline.html");
            }
            openTab(newInstance, tabInfo);
        }
        else {
            ((EditText) findViewById(R.id.mail_password)).setText("");
            Toast.makeText(this, "Złe hasło!", Toast.LENGTH_LONG).show();
        }
    }
    
}
