package com.taracamp.financeplanner;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.taracamp.financeplanner.Login.LoginActivity;

public class SplashActivity extends AppCompatActivity {

    private static final String TAG = "familyplan.debug";
    private static final String CLASS = "SplashActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        Log.d(TAG,CLASS+".onCreate()");

        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
        startActivity(intent);
    }
}
