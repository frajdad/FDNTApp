package fdnt.app.android.utils;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.Toast;

import fdnt.app.android.post.MailSender;

public class FdnContactEventHandler {
    //methods called when on click in different views in foundation contact tab

    final String tel = "tel:225304828";
    final String locURI = "https://www.google.com/maps/place/Dzie%C5%82o+Nowego+Tysi%C4%85clecia.+Fundacja/@52.239936,20.9829355,19z/data=!4m5!3m4!1s0x471ecc81b5b10f39:0x78e923cda8fbf5ad!8m2!3d52.2398794!4d20.9830964";
    final String mail = "mailto:dzielo@episkopat.pl";
    final String NIP = "527-23-16-033";

    public void showFdnCoords(View view) {
        GlobalUtil.createUriIntent(view.getContext(), locURI);
    }
    public void callToFdn(View view) {
        GlobalUtil.createUriIntent(view.getContext(), tel);
    }
    public void mailToFdn(View view) {
        if (GlobalUtil.ifLoggedToPost(view.getContext())) {
            Intent intent = new Intent(view.getContext(), MailSender.class);
            intent.putExtra("to", "dzielo@episkopat.pl");
            view.getContext().startActivity(intent);
        } else {
            GlobalUtil.createUriIntent(view.getContext(), mail);
        }
    }
    public void copyFdnNIP(View view) {
        ClipboardManager clipboard = (ClipboardManager) view.getContext().getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("nip", NIP);
        clipboard.setPrimaryClip(clip);
        Toast.makeText(view.getContext(), "Skopiowano!", Toast.LENGTH_SHORT).show();
    }
}
