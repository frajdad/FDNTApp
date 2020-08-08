package fdnt.app.android.ui.main;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import fdnt.app.android.Dane;
import fdnt.app.android.R;
import fdnt.app.android.databinding.FoundationContactFragmentBinding;
import fdnt.app.android.post.MailSender;

public class KontaktFundacja extends Fragment {

    public static KontaktFundacja newInstance() {
        return new KontaktFundacja();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        /*
        "if something is incorrect then correct it"
        there i obtain binding object which assures me access to views
         */
        FoundationContactFragmentBinding binding = DataBindingUtil.inflate(inflater,
                 R.layout.foundation_contact_fragment, container, false);
        EventHandler handler = new EventHandler();
        binding.setHandler(handler);

        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }
}