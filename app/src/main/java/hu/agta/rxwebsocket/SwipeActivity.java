package hu.agta.rxwebsocket;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ProgressBar;

import hu.agta.rxwebsocket.fragments.PrimaryPage;
import hu.agta.rxwebsocket.fragments.SecondaryPage;
import hu.agta.rxwebsocket.utils.AppGlobals;
import hu.agta.rxwebsocket.utils.CustomViewpager;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class SwipeActivity extends AppCompatActivity {

    private final int FIVE_SECONDS = 5 * 1000;
    private RxWebSocket rxWebSocket;
    public ProgressBar mProgressBar;
    private CustomViewpager viewPager;
    private MyPagerAdapter myPagerAdapter;

    private static SwipeActivity sInstance;

    public static SwipeActivity getInstance() {
        return sInstance;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_swipe);
        sInstance = SwipeActivity.this;
        mProgressBar = findViewById(R.id.progressbar);

        myPagerAdapter = new MyPagerAdapter(getSupportFragmentManager());
        viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(myPagerAdapter);
        viewPager.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
        viewPager.setCurrentItem(1);

        if (AppGlobals.isSwipeEnabled()) {
            viewPager.setPagingEnabled(false);

        } else {
            viewPager.setPagingEnabled(true);
        }

        rxWebSocket = new RxWebSocket("ws://echo.websocket.org");
        rxWebSocket.onOpen()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(socketOpenEvent -> {
                    Snackbar.make(viewPager, "WebSocket opened.", Snackbar.LENGTH_SHORT).show();
                }, Throwable::printStackTrace);

        rxWebSocket.onClosed()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(socketClosedEvent -> {
                    Snackbar.make(viewPager, "WebSocket closed.", Snackbar.LENGTH_SHORT).show();
                }, Throwable::printStackTrace);

        rxWebSocket.onClosing()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(socketClosingEvent -> {
                    Snackbar.make(viewPager, "WebSocket is closing.", Snackbar.LENGTH_SHORT).show();
                }, Throwable::printStackTrace);

        rxWebSocket.onTextMessage()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(socketMessageEvent -> {
                    startActivity(new Intent(getApplicationContext(), ListActivity.class));
//                    viewPager.addMessage(socketMessageEvent);
                }, Throwable::printStackTrace);

        rxWebSocket.onFailure()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(socketFailureEvent -> {
                    Snackbar.make(viewPager, "WebSocket failure! " + socketFailureEvent.getException().getMessage(), Snackbar.LENGTH_SHORT).show();
                }, Throwable::printStackTrace);

        rxWebSocket.connect();

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("update");
        intentFilter.addAction("progress");
        LocalBroadcastManager.getInstance(getApplicationContext()).registerReceiver(new MyReceiver(), intentFilter);

    }

    private class MyPagerAdapter extends FragmentStatePagerAdapter {
        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return new SecondaryPage();

                case 1:
                    return new PrimaryPage();
            }
            return null;
        }

        @Override
        public int getCount() {
            return 2;
        }
    }


    /// apply the enable an disable update
    private class MyReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals("update")) {
                System.out.println("on");
                myPagerAdapter.notifyDataSetChanged();
                viewPager.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
                viewPager.setCurrentItem(1);
                if (!AppGlobals.isSwipeEnabled()) {
                    viewPager.setPagingEnabled(false);
                } else {
                    viewPager.setPagingEnabled(true);
                }
            } else if (intent.getAction().equals("progress")) {
                String s = intent.getExtras().getString("progress_bar");
                if (s.equals("show")) {
                    // showing progress bar for 5 seconds
                    mProgressBar.setVisibility(View.VISIBLE);
                    new Handler().postDelayed(() -> mProgressBar.setVisibility(View.INVISIBLE), FIVE_SECONDS);
                } else {
                    mProgressBar.setVisibility(View.INVISIBLE);
                }
            }
        }
    }

}