/**#################################################################################################
 * Author: Wladimir Tarasov
 * Date: 12.03.2019
 *################################################################################################*/
package com.taracamp.financeplanner;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.taracamp.financeplanner.Core.FirebaseManager;
import com.taracamp.financeplanner.Core.Message;
import com.taracamp.financeplanner.Models.Transaction;
import com.taracamp.financeplanner.Models.User;

import java.util.Date;
import java.util.List;

public class AddTransactionActivity extends AppCompatActivity {

    /**#############################################################################################
     * Controls
     *############################################################################################*/
    private TextView transactionNameTextView;
    private EditText transactionNameEditText;
    private TextView accountFromTextView;
    private EditText accountFromEditText;
    private TextView accountToTextView;
    private EditText accountToEditText;
    private TextView transactionValueTextView;
    private EditText transactionValueEditText;
    private TextView transactionForecastTextView;
    private ToggleButton transactionForecastToggleButton;
    private Button addTransactionButton;
    private Button closeAddTransactionActivityButton;

    private User currentUser;
    private List<Transaction> transactions;
    private FirebaseManager firebaseManager;

    /**#############################################################################################
     * Lifecycles
     *############################################################################################*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_transaction);

        this._loginFirebaseUser();
        this._initializeControls();
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

    /**#############################################################################################
     * Private Methoden
     *############################################################################################*/
    private void _initializeControls(){
        this.transactionNameTextView = findViewById(R.id.transactionNameTextView);
        this.transactionNameEditText = findViewById(R.id.transactionNameEditText);
        this.accountFromTextView = findViewById(R.id.accountFromTextView);
        this.accountFromEditText = findViewById(R.id.accountFromEditText);
        this.accountToTextView = findViewById(R.id.accountToTextView);
        this.accountToEditText = findViewById(R.id.accountToEditText);
        this.transactionValueTextView = findViewById(R.id.transactionValueTextView);
        this.transactionValueEditText = findViewById(R.id.transactionValueEditText);
        this.transactionForecastTextView = findViewById(R.id.transactionForecastTextView);
        this.transactionForecastToggleButton = findViewById(R.id.transactionForecastToggleButton);
        this.addTransactionButton = findViewById(R.id.addTransactionButton);
        this.closeAddTransactionActivityButton = findViewById(R.id.closeAddTransactionActivityButton);

        this._initializeControlEvents();
    }

    private Transaction _createTransaction(){
        // Neue Transaktion wird erstellt
        Transaction newTransaction = new Transaction();
        newTransaction.setToken(null);
        newTransaction.setName(this.transactionNameEditText.getText().toString());
        newTransaction.setAccountFrom(null);
        newTransaction.setAccountTo(null);
        newTransaction.setValue(Double.parseDouble(this.transactionValueEditText.getText().toString()));
        newTransaction.setTransactionDate(new Date());
        newTransaction.setTransactionForecast(this.transactionForecastToggleButton.getKeepScreenOn());
        newTransaction.setTransactionType(null);

        return newTransaction;
    }

    private void _navigateToPreviousActivity(){
        Intent intent = new Intent(getApplicationContext(),MainActivity.class);
        startActivity(intent);
    }

    private void _addTransactionToFirebaseDatabase(){
        Transaction newTransaction = this._createTransaction();
        boolean isTransactionValid = this._checkTransactionValidation(newTransaction);
        if(isTransactionValid){
            this.transactions.add(newTransaction);
            this.currentUser.setTransactions(this.transactions);
            if(this.firebaseManager.saveObject(this.currentUser)){
                Message.show(getApplicationContext(),"Eine neue Transaktion wurde angelegt", Message.Mode.SUCCESS);
                Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                startActivity(intent);
            }
        }
    }

    private boolean _checkTransactionValidation(Transaction transaction){
        //// TODO: 11.03.2019 Muss noch implementiert werden
        return true;
    }

    private void _loginFirebaseUser(){
        this.firebaseManager = new FirebaseManager();
        this.firebaseManager.mAuth = FirebaseAuth.getInstance();
        this.firebaseManager.mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser()!=null){
                    _loadCurrentUser(firebaseAuth.getCurrentUser().getUid());
                }
            }
        };
    }

    private void _loadCurrentUser(final String token){
        this.firebaseManager.getRootReference().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                DataSnapshot userSnapshot = dataSnapshot.child("users").child(token);
                if (userSnapshot.exists()){
                    currentUser = userSnapshot.getValue(User.class);
                    if (currentUser!=null) transactions = currentUser.getTransactions();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {}
        });
    }

    /**#############################################################################################
     * Events
     *############################################################################################*/
    private void _initializeControlEvents(){
        this.addTransactionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                _addTransactionToFirebaseDatabase();
            }
        });
        this.closeAddTransactionActivityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                _navigateToPreviousActivity();
            }
        });
    }
}
