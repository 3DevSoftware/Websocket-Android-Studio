package hu.agta.rxwebsocket.fragments;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;

import hu.agta.rxwebsocket.ListActivity;
import hu.agta.rxwebsocket.R;
import hu.agta.rxwebsocket.WebAppInterface;
import hu.agta.rxwebsocket.utils.AppGlobals;
import hu.agta.rxwebsocket.utils.CircularTextView;

public class PrimaryPage extends Fragment {

    private View mBaseView;
    private WebView mWebview;
    private LinearLayout button;
    private CircularTextView lCount;
    private CircularTextView rCount;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBaseView = inflater.inflate(R.layout.fragment_primary_page, container, false);
        mWebview = mBaseView.findViewById(R.id.web);
        button = mBaseView.findViewById(R.id.layout);
        lCount = mBaseView.findViewById(R.id.l_count);
        rCount = mBaseView.findViewById(R.id.r_count);
        lCount.setSolidColor("#4CAF50");
        rCount.setSolidColor("#F44336");

        lCount.setText(AppGlobals.getStringFromSharedPreferences(AppGlobals.KEY_L_COUNT));
        rCount.setText(AppGlobals.getStringFromSharedPreferences(AppGlobals.KEY_R_COUNT));
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                button.setVisibility(View.GONE);
                startActivity(new Intent(getActivity().getApplicationContext(), ListActivity.class));
            }
        });

        mWebview.addJavascriptInterface(new WebAppInterface(getContext()), "AndroidInterface");
        mWebview.getSettings().setJavaScriptEnabled(true);
        mWebview.setWebViewClient(new MyWebViewClient());
        mWebview.setWebChromeClient(new MyWebChromeClient());

        mWebview.loadUrl("file:///android_asset/pindex.html");
        return mBaseView;
    }


    private class MyWebViewClient extends WebViewClient {
        @Override
        public void onPageFinished(WebView view, String url) {
            // page loaded, do your related work
        }
    }

    private class MyWebChromeClient extends WebChromeClient {
        @Override
        public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
            result.confirm();
            return true;
        }
    }
}
