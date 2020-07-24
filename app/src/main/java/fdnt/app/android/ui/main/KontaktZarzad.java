package fdnt.app.android.ui.main;

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
import fdnt.app.android.ui.main.recview.ManagementRecyclerAdapter;

public class KontaktZarzad extends Fragment {
    public static KontaktZarzad newInstance() {
        return new KontaktZarzad();
    }

    RecyclerView managementRecyclerView;
    ManagementRecyclerAdapter managementRecyclerAdapter;
    String[] roles = {"Przewodniczący", "Wiceprzewodniczący", "Wiceprzewodniczący"};
    String[] mails = {"dariusz.kowalczyk@dzielo.pl", "pawel.walkiewicz@dzielo.pl", "marek.zdrojewski@dzielo.pl"};
    List<String> managementPersonRoles , managementPersonMails;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.kontakt_zarzad_fragment, container, false);

        managementPersonRoles = new ArrayList<String>(Arrays.asList(roles));
        managementPersonMails = new ArrayList<String>(Arrays.asList(mails));

        managementRecyclerView = (RecyclerView) rootView.findViewById(R.id.managementRecyclerView);
        managementRecyclerAdapter = new ManagementRecyclerAdapter(managementPersonRoles, managementPersonMails);

        managementRecyclerView.setAdapter(managementRecyclerAdapter);
        managementRecyclerView.setHasFixedSize(true);
        managementRecyclerView.setItemViewCacheSize(7);
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

}