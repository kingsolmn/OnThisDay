package net.trs.onthisday;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.google.analytics.tracking.android.EasyTracker;


public class MainActivity extends FragmentActivity {

    private static final String TAG = "OnThisDay - Main";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.container, new MainFragment())
                    .commit();
        }
    }

    @Override
    public void onStart(){
        super.onStart();
        EasyTracker.getInstance(this).activityStart(this);
    }

    @Override
    public void onStop(){
        EasyTracker.getInstance(this).activityStop(this);
    }
}
