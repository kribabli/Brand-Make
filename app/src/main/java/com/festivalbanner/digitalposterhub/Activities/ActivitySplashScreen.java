package com.festivalbanner.digitalposterhub.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.WindowManager;

import com.festivalbanner.digitalposterhub.AnalyticsApplication;
import com.festivalbanner.digitalposterhub.R;
import com.festivalbanner.digitalposterhub.Utills.Admin;
import com.festivalbanner.digitalposterhub.Utills.SharedPrefrenceConfig;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.teamup.app_sync.AppSyncToast;

public class ActivitySplashScreen extends AppCompatActivity {
    Handler handler;
    Context context;
    Tracker mTracker;
    SharedPrefrenceConfig sharedprefconfig;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        context = ActivitySplashScreen.this;
        Admin.intializeLocalRoom(this);

        sharedprefconfig = new SharedPrefrenceConfig(context);


        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        AnalyticsApplication application = (AnalyticsApplication) getApplication();
        mTracker = application.getDefaultTracker();
        Log.d("Amit", "Value 1111 " + mTracker);


        handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                finish();
                if (Admin.tinyDB.getBoolean("login")) {
                    Admin.Handle_activity_opener(context, ActivityHome.class);
                } else {
                    Admin.Handle_activity_opener(context, MainLoginPage.class);
                }
            }
        }, 2000);


    }

    @Override
    protected void onResume() {
        super.onResume();
        mTracker.setScreenName("Image~" + "Google Analytics Testing");
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());
    }

}
