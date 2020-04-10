package fdnt.app.android.ui.main;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import fdnt.app.android.R;

public class WebTab extends Fragment {

    private WebView myWebView;

    public static WebTab newInstance() {
        return new WebTab();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.main_fragment, container, false);
        return view;
    }

    private void loadTab(String adress) {
        getActivity().findViewById(R.id.progressBar).setVisibility(View.VISIBLE);
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
    }

    private WebViewClient myClient = new WebViewClient() {
        public void onPageFinished(WebView view, String url) {
            //chowamy kręcące się kółko
            try {
                getActivity().findViewById(R.id.progressBar).setVisibility(View.INVISIBLE);
            }
            catch (NullPointerException e) {
            }
        }

        @SuppressWarnings("deprecation")
        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
            myWebView.loadUrl("file:///android_asset/offline.html");
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
}
