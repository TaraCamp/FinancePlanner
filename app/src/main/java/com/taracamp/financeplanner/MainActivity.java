package com.taracamp.financeplanner;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.taracamp.financeplanner.Login.LoginActivity;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "familyplan.debug";
    private static final String CLASS = "MainActivity";

    private TextView EmailTextView;
    private Button LogOutButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(TAG,CLASS+".onCreate()");

        this.init();
        this.attachEvents();

    }

    private void init(){
        this.EmailTextView = findViewById(R.id.MainEmailTextView);
        this.LogOutButton = findViewById(R.id.LogOutButton);
    }

    private void attachEvents(){
        this.LogOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
            }
        });
    }
}
