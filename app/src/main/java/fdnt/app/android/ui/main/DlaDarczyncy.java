package fdnt.app.android.ui.main;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import fdnt.app.android.MainFrame;
import fdnt.app.android.R;

public class DlaDarczyncy extends Fragment {
    public static DlaDarczyncy newInstance() { return new DlaDarczyncy(); }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.dla_darczyncy_fragment, container, false);
        ImageView barSendSMS = (ImageView) rootView.findViewById(R.id.belka_sms);
        ImageView barPay = (ImageView) rootView.findViewById(R.id.belka_wplac);
        ImageView barDownload = (ImageView) rootView.findViewById(R.id.belka_pobierz);
        ImageView barTransferPercent = (ImageView) rootView.findViewById(R.id.belka_przekaz);


        // wysyłanie sms po kliknieciu
        barSendSMS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(rootView.getContext());
                alertDialog.setMessage("Aby wysłać SMS i wspomóc naszą Fundację kliknij WYŚLIJ " +
                        "(4,92 zł z VAT). Dziękujemy!");

                alertDialog.setNegativeButton("Anuluj",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });

                alertDialog.setPositiveButton("Wyślij",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                Intent i = new Intent(Intent.ACTION_VIEW);
                                i.setData(Uri.parse("smsto:"));
                                i.setType("vnd.android-dir/mms-sms");
                                i.putExtra("address", new String("74265"));
                                i.putExtra("sms_body", "STYPENDIA");
                                startActivity(Intent.createChooser(i, "Send sms via:"));
                            }
                        });
                alertDialog.show();
            }
        });
        barPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String URL = "https://dzielo.pl/dla-darczyncy/wplata/";
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(URL));
                startActivity(intent);
            }
        });
        barDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String URL = "https://dzielo.pl/wp-content/uploads/up_media/przelewFDNT.pdf";
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(URL));
                startActivity(intent);
            }
        });
        barTransferPercent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String URL = "https://2018.pit-format-online.pl/?rid=b1b5566183bd3f337c9a1f4bd2b2daa0f0728ad2";
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(URL));
                startActivity(intent);
            }
        });
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    private void sendSMS() {

    }
}
