package com.taracamp.financeplanner.Login;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import android.support.annotation.NonNull;
import android.widget.ProgressBar;

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

    private FirebaseAuth mAuth;

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
                final String email = EmailEditText.getText().toString();
                final String password = PasswordEditText.getText().toString();
                if (checkValidation(email,password)){
                    register(email,password);
                }
            }
        });
        this.BackToLoginPageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openLoginPage();
            }
        });
    }

    private boolean checkValidation(String email,String password){
        //// TODO: 16.02.2019
      return true;
    }

    private void register(String email,String password){
        this.mAuth = FirebaseAuth.getInstance();
        this.mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();
                            Log.d(TAG,CLASS+".register() successful");

                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                            startActivity(intent);
                        }else{
                            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                            startActivity(intent);
                        }
                    }
                });
    }

    private void openLoginPage(){
        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
        startActivity(intent);
    }

    private void showProgressDialog(){
        //// TODO: 16.02.2019  
    }

    private void hideProgressDialog(){
        //// TODO: 16.02.2019
    }

    private void createUser(){

    }
}
