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

public class RegisterActivity extends AppCompatActivity {
    private static final String TAG = "familyplan.debug";
    private static final String CLASS = "RegisterActivity";

    private EditText EmailEditText;
    private EditText UsernameEditeText;
    private EditText PasswordEditText;
    private Button RegisterButton;
    private Button BackToLoginPageButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        Log.d(TAG,CLASS+".onCreate()");
        this.init();
        this.attachEvents();
    }

    private void init(){
        this.EmailEditText = findViewById(R.id.EmailEditText);
        this.UsernameEditeText = findViewById(R.id.UsernameEditText);
        this.PasswordEditText = findViewById(R.id.PasswordEditText);
        this.RegisterButton = findViewById(R.id.RegisterButton);
        this.BackToLoginPageButton = findViewById(R.id.BackToLoginPageButton);
    }

    private void attachEvents(){
        this.RegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                register();
            }
        });
        this.BackToLoginPageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openLoginPage();
            }
        });
    }

    private void register(){
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
    }

    private void openLoginPage(){
        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
        startActivity(intent);
    }

}
