package hu.agta.rxwebsocket.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import hu.agta.rxwebsocket.R;
import hu.agta.rxwebsocket.WebAppInterface;

public class SecondaryPage extends Fragment {

    private View mBaseView;
    private WebView mWebview;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBaseView = inflater.inflate(R.layout.fragment_secondary_page, container, false);
        mWebview = mBaseView.findViewById(R.id.web);
        mWebview.addJavascriptInterface(new WebAppInterface(getContext()), "AndroidInterface");
        mWebview.getSettings().setJavaScriptEnabled(true);
        mWebview.setWebViewClient(new MyWebViewClient());
        mWebview.setWebChromeClient(new MyWebChromeClient());
        mWebview.loadUrl("file:///android_asset/sindex.html");
        return mBaseView;
    }


    private class MyWebViewClient extends WebViewClient {
        @Override
        public void onPageFinished(WebView view, String url) {
            //Calling a javascript function in html page
            view.loadUrl("javascript:alert(showVersion('called by Android'))");
        }
    }

    private class MyWebChromeClient extends WebChromeClient {
        @Override
        public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
            Log.d("LogTag", message);
            result.confirm();
            return true;
        }
    }
}

