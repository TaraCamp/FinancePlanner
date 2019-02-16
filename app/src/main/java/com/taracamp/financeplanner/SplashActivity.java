package com.taracamp.financeplanner;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.taracamp.financeplanner.Login.LoginActivity;
import com.taracamp.financeplanner.Models.FirebaseManager;

public class SplashActivity extends AppCompatActivity {

    private static final String TAG = "familyplan.debug";
    private static final String CLASS = "SplashActivity";

    private FirebaseManager firebaseManager;
    private FirebaseUser currentUser;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        Log.d(TAG,CLASS+".onCreate()");
        FirebaseApp.initializeApp(this);
        this.login();
    }

    @Override
    public void onStart() {
        super.onStart();
        this.firebaseManager.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
        this.firebaseManager.onStop();
    }

    private void login(){
        this.firebaseManager = new FirebaseManager();
        this.firebaseManager.mAuth = FirebaseAuth.getInstance();
        this.firebaseManager.mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                final FirebaseUser user = firebaseAuth.getCurrentUser();
                Intent intent;
                if (user!=null){
                    loadUser();
                    intent = new Intent(getApplicationContext(),MainActivity.class);
                }else intent = new Intent(getApplicationContext(), LoginActivity.class);

                startActivity(intent);
            }
        };
    }

    private void loadUser(){

    }
}
