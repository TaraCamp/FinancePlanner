/**#################################################################################################
 * Author: Wladimir Tarasov
 * Date: 27.03.2019
 *################################################################################################*/
package com.taracamp.financeplanner.Login;

import android.content.Intent;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import android.support.annotation.NonNull;

import com.taracamp.financeplanner.Core.FirebaseManager;
import com.taracamp.financeplanner.MainActivity;
import com.taracamp.financeplanner.MenuActivity;
import com.taracamp.financeplanner.Models.Account;
import com.taracamp.financeplanner.Models.Transaction;
import com.taracamp.financeplanner.Models.TransactionCategory;
import com.taracamp.financeplanner.Models.User;
import com.taracamp.financeplanner.R;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class RegisterActivity extends AppCompatActivity {

    /**#############################################################################################
     * Controls
     *############################################################################################*/
    private TextInputEditText registerEmailTextInputEditText;
    private TextInputEditText registerUsernameTextInputEditText;
    private TextInputEditText registerPasswordTextInputEditText;
    private Button registerConfirmButton;
    private Button registerToPreviewPageButton;

    /**#############################################################################################
     * Properties
     *############################################################################################*/
    private FirebaseManager firebaseManager;

    /**#############################################################################################
     * Lifecycles
     *############################################################################################*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this._initializeControls();
    }

    /**#############################################################################################
     * Activity Events
     *############################################################################################*/
    @Override
    public void onBackPressed() {
        startActivity(new Intent(getApplicationContext(),LoginActivity.class));
    }

    /**#############################################################################################
     * Events
     *############################################################################################*/
    private void _initializeControls(){
        this.setContentView(R.layout.activity_register);
        this.registerEmailTextInputEditText = findViewById(R.id.registerEmailTextInputEditText);
        this.registerUsernameTextInputEditText = findViewById(R.id.registerUsernameTextInputEditText);
        this.registerPasswordTextInputEditText = findViewById(R.id.registerPasswordTextInputEditText);
        this.registerConfirmButton = findViewById(R.id.registerConfirmButton);
        this.registerToPreviewPageButton = findViewById(R.id.registerToPreviewPageButton);

        this._initializeControlEvents();
    }

    private void _initializeControlEvents(){
        this.registerConfirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String email = registerEmailTextInputEditText.getText().toString();
                final String password = registerPasswordTextInputEditText.getText().toString();
                if (_checkValidation(email,password)){
                    _register(email,password);
                }
            }
        });
        this.registerToPreviewPageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openLoginPage();
            }
        });
    }

    /**#############################################################################################
     * Private functions
     *############################################################################################*/
    private void _register(String email,String password){
        this.firebaseManager = new FirebaseManager();
        this.firebaseManager.mAuth = FirebaseAuth.getInstance();
        this.firebaseManager.mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            if (_createUser(firebaseManager.mAuth.getCurrentUser())){
                                startActivity(new Intent(getApplicationContext(), MenuActivity.class));
                            }
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("DEBUG",e.getMessage().toString());
                    }
        });
    }

    private boolean _checkValidation(String email,String password){
        //// TODO: 16.02.2019
        return true;
    }

    private Account _generateAccount(){
        //// TODO: 16.03.2019 Muss ausgelagert werden
        Account startAccount = new Account();
        startAccount.setAccountName("Hauptkonto");
        startAccount.setAccountDescription("Erstes Konto");
        startAccount.setAccountType("MAIN");
        startAccount.setAccoutCreateDate(new Date());
        startAccount.setAccountValue(0.0);
        startAccount.setAccountRecordToValue(true);

        return startAccount;
    }

    private Transaction _generateTransaction(){
        //// TODO: 16.03.2019 Muss ausgelagert werden
        Transaction startTransaction = new Transaction();
        startTransaction.setTransactionName("Erste Transaktion");
        startTransaction.setTransactionValue(0.0);
        startTransaction.setTransactionDate(new Date());
        startTransaction.setTransactionCreateDate(new Date());
        startTransaction.setTransactionFromAccount(null);
        startTransaction.setTransactionToAccount(null);
        startTransaction.setTransactionType("NEUTRAL");
        startTransaction.setTransactionDescription("Willkommen und viel freude mit der App!");
        startTransaction.setTransactionForecast(false);
        return startTransaction;
    }

    private boolean _createUser(FirebaseUser firebaseUser){
        User user = new User();

        List<Account> accounts = new ArrayList<>();
        accounts.add(_generateAccount());

        user.setToken(firebaseUser.getUid());
        user.setEmail(firebaseUser.getEmail());
        user.setAccounts(accounts);
        return this.firebaseManager.saveObject(user);
    }

    private void openLoginPage(){
        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
        startActivity(intent);
    }

    private void _showProgressDialog(){
        //// TODO: 16.02.2019  
    }

    private void _hideProgressDialog(){
        //// TODO: 16.02.2019
    }

    private TransactionCategory getEntry(String name, String transactionTypeEnum,
                                         String transactionCycleEnum){
        TransactionCategory transactionCategory = new TransactionCategory();
        transactionCategory.setTransactionCategoryNme(name);
        transactionCategory.setTransactionType(transactionTypeEnum);
        transactionCategory.setTransactionCycle(transactionCycleEnum);

        return transactionCategory;
    }

}
