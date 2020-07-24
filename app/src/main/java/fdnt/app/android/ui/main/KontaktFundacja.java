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
import androidx.fragment.app.Fragment;

import fdnt.app.android.Dane;
import fdnt.app.android.R;
import fdnt.app.android.post.MailSender;

public class KontaktFundacja extends Fragment {
    public static KontaktFundacja newInstance() {
        return new KontaktFundacja();
    }

    private View.OnClickListener showFoundationLocalization = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            final String locURI = "https://www.google.com/maps/place/Dzie%C5%82o+Nowego+Tysi%C4%85clecia.+Fundacja/@52.239936,20.9829355,19z/data=!4m5!3m4!1s0x471ecc81b5b10f39:0x78e923cda8fbf5ad!8m2!3d52.2398794!4d20.9830964";
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(locURI));
            startActivity(intent);
        }
    };

    private View.OnClickListener callToFoundation = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            final String tel = "tel:225304828";
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(tel));
            startActivity(intent);
        }
    };

    private View.OnClickListener mailToFoundation = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            final String mail = "mailto:dzielo@episkopat.pl";
            if(Dane.ifLoggedToPost()) {
                Intent intent = new Intent(getActivity(), MailSender.class);
                intent.putExtra("to", "mailto:dzielo@episkopat.pl");
                startActivity(intent);
            }
            else {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(mail));
                startActivity(intent);
            }
        }
    };

    private  View.OnClickListener copyFoundationNIP = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            final String NIP = "527-23-16-033";
            ClipboardManager clipboard = (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData clip = ClipData.newPlainText("nip", NIP);
            clipboard.setPrimaryClip(clip);
            Toast.makeText(getActivity(), "Skopiowano!", Toast.LENGTH_SHORT).show();
        }
    };


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.kontakt_fundacja_fragment, container, false);

        ImageView localizationIcon = (ImageView) rootView.findViewById(R.id.ikonatrasa);
        TextView localizationHeader = (TextView) rootView.findViewById(R.id.adres);
        TextView localizationText = (TextView) rootView.findViewById(R.id.adres_tekst);
        localizationIcon.setOnClickListener(showFoundationLocalization);
        localizationHeader.setOnClickListener(showFoundationLocalization);
        localizationText.setOnClickListener(showFoundationLocalization);

        ImageView callIcon = (ImageView) rootView.findViewById(R.id.ikonatel);
        TextView callHeader = (TextView) rootView.findViewById(R.id.telefon);
        TextView callText = (TextView) rootView.findViewById(R.id.tel_tekst);
        callIcon.setOnClickListener(callToFoundation);
        callHeader.setOnClickListener(callToFoundation);
        callText.setOnClickListener(callToFoundation);

        ImageView mailIcon = (ImageView) rootView.findViewById(R.id.ikonamail);
        TextView mailHeader = (TextView) rootView.findViewById(R.id.mail);
        TextView mailText = (TextView) rootView.findViewById(R.id.mail_tekst);
        mailIcon.setOnClickListener(mailToFoundation);
        mailHeader.setOnClickListener(mailToFoundation);
        mailText.setOnClickListener(mailToFoundation);

        ImageView nipIcon = (ImageView) rootView.findViewById(R.id.ikonanip);
        TextView nipHeader = (TextView) rootView.findViewById(R.id.nip);
        TextView nipText = (TextView) rootView.findViewById(R.id.nip_tekst);
        nipIcon.setOnClickListener(copyFoundationNIP);
        nipHeader.setOnClickListener(copyFoundationNIP);
        nipText.setOnClickListener(copyFoundationNIP);

        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }
}