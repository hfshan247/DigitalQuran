package com.example.hussainfarooq.digitalquran;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

public class SplashScreen extends Activity {

    private static final int SPLASH_TIME_OUT = 4000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // If the Android version is lower than Jellybean, use this call to hide
        // the status bar.
//        if (Build.VERSION.SDK_INT < 16) {
//            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
//        }
//        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
//        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);

        //requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.activity_splash_screen);

        //Splash Screen
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent homeIntent = new Intent(SplashScreen.this, Home.class);
                startActivity(homeIntent);
                finish();
            }
        }, SPLASH_TIME_OUT);
        //End of Splash Screen
    }
}