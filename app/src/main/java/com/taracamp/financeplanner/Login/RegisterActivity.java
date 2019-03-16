/**#################################################################################################
 * Author: Wladimir Tarasov
 * Date: 13.03.2019
 *################################################################################################*/
package com.taracamp.financeplanner.Login;

import android.content.Intent;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import android.support.annotation.NonNull;

import com.taracamp.financeplanner.Core.FirebaseManager;
import com.taracamp.financeplanner.MainActivity;
import com.taracamp.financeplanner.Models.Account;
import com.taracamp.financeplanner.Models.Transaction;
import com.taracamp.financeplanner.Models.TransactionCategory;
import com.taracamp.financeplanner.Models.User;
import com.taracamp.financeplanner.R;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class RegisterActivity extends AppCompatActivity {
    private static final String TAG = "familyplan.debug";
    private static final String CLASS = "RegisterActivity";

    /**#############################################################################################
     * Controls
     *############################################################################################*/
    private TextInputEditText EmailEditText;
    private TextInputEditText UsernameEditeText;
    private TextInputEditText PasswordEditText;
    private Button RegisterButton;
    private Button BackToLoginPageButton;

    /**#############################################################################################
     * Properties
     *############################################################################################*/
    private FirebaseManager firebaseManager;
    private FirebaseAuth mAuth;

    /**#############################################################################################
     * Lifecycles
     *############################################################################################*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        Log.d(TAG,CLASS+".onCreate()");

        this._initializeControls();

    }

    /**#############################################################################################
     * Events
     *############################################################################################*/

    private void _attachEvents(){
        this.RegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String email = EmailEditText.getText().toString();
                final String password = PasswordEditText.getText().toString();
                if (_checkValidation(email,password)){
                    _register(email,password);
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

    /**#############################################################################################
     * Private functions
     *############################################################################################*/
    private void _initializeControls(){
        this.EmailEditText = findViewById(R.id.registerEmailTextInputEditText);
        this.UsernameEditeText = findViewById(R.id.registerUsernameTextInputEditText);
        this.PasswordEditText = findViewById(R.id.registerPasswordTextInputEditText);
        this.RegisterButton = findViewById(R.id.registerConfirmButton);
        this.BackToLoginPageButton = findViewById(R.id.registerToPreviewPageButton);

        this._attachEvents();
    }

    private boolean _checkValidation(String email,String password){
        //// TODO: 16.02.2019
      return true;
    }

    private void _register(String email,String password){
        this.mAuth = FirebaseAuth.getInstance();
        this.mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            if (_createUser(mAuth.getCurrentUser())){
                                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                intent.putExtra("USERTOKEN", mAuth.getCurrentUser().getUid());
                                startActivity(intent);
                            }
                        }
                    }
                });
    }

    private Account _generateAccount(){
        //// TODO: 16.03.2019 Muss ausgelagert werden
        Account startAccount = new Account();
        startAccount.setAccountName("Hauptkonto");
        startAccount.setAccountDescription("Erstes Konto");
        startAccount.setAccountType("MAIN");
        startAccount.setAccoutCreateDate(new Date());
        startAccount.setAccountValue(0.0);

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
        this.firebaseManager = new FirebaseManager();

        List<Transaction> transactionList = new ArrayList<>();
        List<Account> accountList = new ArrayList<>();
        List<TransactionCategory> transactionCategoryList;

        Account startAccount = this._generateAccount();
        accountList.add(startAccount);

        Transaction startTransaction = this._generateTransaction();
        transactionList.add(startTransaction);

        User user = new User();
        user.setToken(firebaseUser.getUid());
        user.setEmail(firebaseUser.getEmail());
        user.setTransactions(transactionList);
        user.setAccounts(accountList);

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
