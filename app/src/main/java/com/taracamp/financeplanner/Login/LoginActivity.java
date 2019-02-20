package com.taracamp.financeplanner.Login;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.taracamp.financeplanner.Core.FirebaseManager;
import com.taracamp.financeplanner.Core.Message;
import com.taracamp.financeplanner.MainActivity;
import com.taracamp.financeplanner.R;

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "familyplan.debug";
    private static final String CLASS = "LoginActivity";

    private EditText EmailEditText;
    private EditText PasswordEditText;
    private Button LoginButton;
    private Button ToRegisterPageButton;

    private FirebaseManager firebaseManager;

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
                String email = EmailEditText.getText().toString();
                String password = PasswordEditText.getText().toString();
                if (checkValidation(email,password)) login(email,password);
            }
        });
        this.ToRegisterPageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openRegisterPage();
            }
        });
    }

    private void login(String email,String password){
        this.firebaseManager = new FirebaseManager();
        this.firebaseManager.mAuth = FirebaseAuth.getInstance();
        this.firebaseManager.mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            Message.show(getApplicationContext(),"Anmeldung war erfolgreich!",Message.Mode.SUCCESS);
                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                            startActivity(intent);
                        }
                    }
                });
    }

    private boolean checkValidation(String email,String password){
        return true;
    }

    private void openRegisterPage(){
        Intent intent = new Intent(getApplicationContext(), RegisterActivity.class);
        startActivity(intent);
    }
}
