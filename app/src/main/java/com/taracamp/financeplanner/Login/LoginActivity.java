package com.taracamp.financeplanner.Login;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.taracamp.financeplanner.MainActivity;
import com.taracamp.financeplanner.R;

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "familyplan.debug";
    private static final String CLASS = "LoginActivity";

    private EditText EmailEditText;
    private EditText PasswordEditText;
    private Button LoginButton;
    private Button ToRegisterPageButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Log.d(TAG,CLASS+".onCreate()");
        this.init();
        this.attachEvents();
    }

    private void init(){
        this.EmailEditText = findViewById(R.id.EmailEditText);
        this.PasswordEditText = findViewById(R.id.PasswordEditText);
        this.LoginButton = findViewById(R.id.LoginButton);
        this.ToRegisterPageButton = findViewById(R.id.RegisterButton);
    }

    private void attachEvents(){
        this.LoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });
        this.ToRegisterPageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openRegisterPage();
            }
        });
    }

    private void login(){
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
    }

    private void openRegisterPage(){
        Intent intent = new Intent(getApplicationContext(), RegisterActivity.class);
        startActivity(intent);
    }
}
