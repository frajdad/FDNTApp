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


    List<String> officePersonRoles, officePersonMails, officePersonTels;
    RecyclerView officeRecyclerView;
    OfficeRecyclerAdapter officeRecyclerAdapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View rootView =  inflater.inflate(R.layout.kontakt_biuro_fragment, container, false);

        officeRecyclerView = rootView.findViewById(R.id.officeRecyclerView);
        officeRecyclerAdapter = new OfficeRecyclerAdapter();
        officeRecyclerView.setAdapter(officeRecyclerAdapter);

        officeRecyclerView.setHasFixedSize(true);
        officeRecyclerView.setItemViewCacheSize(7);
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

}