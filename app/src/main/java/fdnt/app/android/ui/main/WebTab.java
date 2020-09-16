package fdnt.app.android.ui.main;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.CookieSyncManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.preference.PreferenceManager;

import fdnt.app.android.R;
import fdnt.app.android.utils.NetworkChangeReceiver;

public class WebTab extends Fragment implements NetworkChangeReceiver.ConnectionChangeCallback {

    private WebView myWebView;
    private NetworkChangeReceiver networkChangeReceiver;
    private String lastUrl;
    private static WebTab instance;
    public static WebTab newInstance() { return new WebTab ();}
    private int counter = 0;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        IntentFilter intentFilter = new
                IntentFilter ("android.net.conn.CONNECTIVITY_CHANGE");
    
        networkChangeReceiver = new NetworkChangeReceiver();
        CookieSyncManager.createInstance(getContext ());
        getActivity ().registerReceiver(networkChangeReceiver, intentFilter);
    
        networkChangeReceiver.setConnectionChangeCallback(this);
        View view = inflater.inflate(R.layout.main_fragment, container, false);
        return view;
    }

    public void loadTab(String adress) {
        lastUrl = adress;
        myWebView.loadUrl(adress);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        //Wyświetlanie treści napisanej w html i różne ustawienia Webview
        myWebView = getView().findViewById(R.id.webView);
        myWebView.getSettings().setJavaScriptEnabled(true);
        myWebView.setWebViewClient(myClient);

        loadTab(getArguments().getString("adress"));

        if (PreferenceManager
                .getDefaultSharedPreferences(getContext())
                .getBoolean("dark_mode", true)) {
           // if(WebViewFeature.isFeatureSupported(WebViewFeature.FORCE_DARK)) {
             //   WebSettingsCompat.setForceDark(myWebView.getSettings(), WebSettingsCompat.FORCE_DARK_ON);
           // }
        }
    }

    private WebViewClient myClient = new WebViewClient() {

        @SuppressWarnings("deprecation")
        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
            AlertDialog.Builder noInternetDialog = new AlertDialog.Builder(getContext ());
            noInternetDialog.setMessage ("Jesteś offline. Sprawdź swoje łącze internetowe i " +
                    "spróbuj później.");
            noInternetDialog.setNeutralButton ("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            noInternetDialog.show ();
            myWebView.loadUrl("file:///android_asset/offline.html");
            counter++;
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            if (url.contains("mailto:")) {
                sendEmail(url);
                return true;
            } else if (url.contains("tel:")) {
                callTo(url);
                return true;
            } else {
                // Handle what t do if the link doesn't contain or start with mailto:
                view.loadUrl(url);
            }
            return true;
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            CookieSyncManager.getInstance().sync();
            if (url.contains("dzielo.pl")) {
                myWebView.loadUrl(
                        "javascript:(function() {document.getElementById('header').style.display = 'none'; " +
                                "document.getElementsByClassName('slider-section')[0].style.top = '0px'; " +
                                "document.getElementsByClassName('main-section')[0].style.top = '0px'; })()");
            }
        }
    };

    public void callTo(String command) {
        Intent callIntent = new Intent(Intent.ACTION_DIAL);
        callIntent.setData(Uri.parse(command));
        startActivity(callIntent);
    }

    public void sendEmail(String command) {
        Intent emailIntent = new Intent(Intent.ACTION_VIEW);
        emailIntent.setData(Uri.parse(command));
        startActivity(Intent.createChooser(emailIntent, "Send mail..."));
    }

    @Override
    public void onConnectionChange (boolean isConnected) {
        if (isConnected && lastUrl != null && counter >= 1) {
            Toast.makeText(getActivity(), "Internet powrócił :)",
                    Toast.LENGTH_LONG).show();
            loadTab(lastUrl);
        }
    }
    
    @Override
    public void onStop() {
        try {
            getActivity ().unregisterReceiver (networkChangeReceiver);
        } catch (Exception e) {
        
        }
        super.onStop ();
    }
    
}
