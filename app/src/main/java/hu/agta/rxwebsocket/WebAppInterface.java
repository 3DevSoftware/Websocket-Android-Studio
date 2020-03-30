package hu.agta.rxwebsocket;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.os.Build;
import android.support.v4.content.LocalBroadcastManager;
import android.webkit.JavascriptInterface;
import android.widget.Toast;

import java.util.Date;

import hu.agta.rxwebsocket.utils.AppGlobals;

public class WebAppInterface {
    private Context mContext;
    private MediaPlayer mp;

    // Instantiate the interface and set the context
    public WebAppInterface(Context c) {
        mContext = c;
    }

    /// close application
    @JavascriptInterface
    public void closeApp() {
        SwipeActivity.getInstance().finish();
    }


    @JavascriptInterface
    public void showAndroidVersion() {
        int androidVersion = android.os.Build.VERSION.SDK_INT;
        Toast.makeText(mContext, "Android Version " + androidVersion, Toast.LENGTH_SHORT).show();
    }

    @JavascriptInterface
    public void showBuildDate() {
        Date buildDate = BuildConfig.BUILD_TIME;
        Toast.makeText(mContext, buildDate.toString() , Toast.LENGTH_SHORT).show();
    }

    @JavascriptInterface
    public void showOSVersion() {
        String release = Build.VERSION.RELEASE;
        Toast.makeText(mContext, release , Toast.LENGTH_SHORT).show();
    }

    @JavascriptInterface
    public void enableSwipe() {
        Toast.makeText(mContext, "swipe enabled", Toast.LENGTH_SHORT).show();
        AppGlobals.swipeState(true);
        LocalBroadcastManager.getInstance(mContext).sendBroadcast(new Intent("update"));
    }

    @JavascriptInterface
    public void disableSwipe() {
        Toast.makeText(mContext, "swipe disabled", Toast.LENGTH_SHORT).show();
        AppGlobals.swipeState(false);
        LocalBroadcastManager.getInstance(mContext).sendBroadcast(new Intent("update"));
    }

    @JavascriptInterface
    public void playTone() {
        Toast.makeText(mContext, "Play Tone", Toast.LENGTH_SHORT).show();
        // you can add your custom sound like this:   MediaPlayer.create(mContext, R.raw.mysound.mp3);
        mp = MediaPlayer.create(mContext, RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));
        mp.start();
    }

    @JavascriptInterface
    public void showLoadingMask() {
        Intent intent = new Intent("progress");
        intent.putExtra("progress_bar", "show");
        LocalBroadcastManager.getInstance(mContext).sendBroadcast(intent);
    }
}