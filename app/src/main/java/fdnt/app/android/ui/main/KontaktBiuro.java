package fdnt.app.android.ui.main;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import fdnt.app.android.R;
import fdnt.app.android.ui.main.recview.OfficeRecyclerAdapter;

public class KontaktBiuro extends Fragment {
    public static KontaktBiuro newInstance() {
        return new KontaktBiuro();
    }

    String[] roles = {
            "Koordynator ds. formacji stypendystów",
            "Asystentka Zarządu",
            "Menadżer ds. stypendialnych",
            "Menedżer ds. Dnia Papieskiego",
            "Menedżer ds. kontaktu z Darczyńcami",
            "Menedżer ds. kampanii społecznych",
            "Kierownik Biura Prasowego"
    };
    String[] mails = {
            "lukasz.nycz@dzielo.pl",
            "sekretariat@dzielo.pl",
            "stypendia@dzielo.pl",
            "dzienpapieski@dzielo.pl",
            "darczyncy@dzielo.pl",
            "paulina.worozbit@dzielo.pl",
            "biuroprasowe@dzielo.pl"
    };
    String[] tels = {
            "503504407",
            "668286129",
            "734445490",
            "602830082",
            "668285959",
            "881678857",
            "662506859"
    };
    List<String> officePersonRoles, officePersonMails, officePersonTels;
    RecyclerView officeRecyclerView;
    OfficeRecyclerAdapter officeRecyclerAdapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View rootView =  inflater.inflate(R.layout.kontakt_biuro_fragment, container, false);
        officePersonRoles = new ArrayList<String>(Arrays.asList(roles));
        officePersonMails = new ArrayList<String>(Arrays.asList(mails));
        officePersonTels = new ArrayList<String>(Arrays.asList(tels));

        officeRecyclerView = rootView.findViewById(R.id.officeRecyclerView);
        officeRecyclerAdapter = new OfficeRecyclerAdapter(officePersonRoles ,officePersonMails, officePersonTels);
        officeRecyclerView.setAdapter(officeRecyclerAdapter);

        officeRecyclerView.setHasFixedSize(true);
        officeRecyclerView.setItemViewCacheSize(20);
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

}