/**#################################################################################################
 * Author: Wladimir Tarasov
 * Date: 12.03.2019
 *################################################################################################*/
package com.taracamp.financeplanner;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Switch;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.taracamp.financeplanner.Adapters.AccountSpinnerAdapter;
import com.taracamp.financeplanner.Core.FirebaseManager;
import com.taracamp.financeplanner.Core.Message;
import com.taracamp.financeplanner.Models.Account;
import com.taracamp.financeplanner.Models.Transaction;
import com.taracamp.financeplanner.Models.TransactionTypeValueHelper;
import com.taracamp.financeplanner.Models.User;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class AddTransactionActivity extends AppCompatActivity {

    /**#############################################################################################
     * Controls
     *############################################################################################*/
    private TextInputEditText addTransactionNameTextInputEditText;
    private TextInputEditText addTransactionDescriptionTextInputEditText;
    private TextInputEditText addTransactionValueTextInputEditText;
    private Spinner addTransactionFromAccountSpinner;
    private Spinner addTransactionToAccountSpinner;
    private Spinner addTransactionTypeSpinner;
    private Switch addTransactionForcastSwitch;
    private Button addTransactionButton;
    private Button addTransactionCancelButton;

    private AccountSpinnerAdapter accountSpinnerAdapter;

    private User currentUser;
    private List<Transaction> transactions;
    private List<Account> accounts;
    private FirebaseManager firebaseManager;

    private String transactionTypeSelectedValue;

    /**#############################################################################################
     * Lifecycles
     *############################################################################################*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_transaction);

        this._loginFirebaseUser();
    }

    @Override
    public void onStart() {
        super.onStart();
        if (this.firebaseManager != null) this.firebaseManager.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
        if (this.firebaseManager != null) this.firebaseManager.onStop();
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
        this.addTransactionCancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                _navigateToPreviousActivity();
            }
        });

        this.addTransactionTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                TransactionTypeValueHelper transactionTypeValueHelper = (TransactionTypeValueHelper) parent.getSelectedItem();
                transactionTypeSelectedValue = transactionTypeValueHelper.getId();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                transactionTypeSelectedValue = "POSITIVE";
            }
        });
    }

    /**#############################################################################################
     * Private Methoden
     *############################################################################################*/
    private void _loginFirebaseUser(){
        this.firebaseManager = new FirebaseManager();
        this.firebaseManager.mAuth = FirebaseAuth.getInstance();
        this.firebaseManager.mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser()!=null){
                    _setUser(firebaseAuth.getCurrentUser().getUid());
                }
            }
        };
    }

    private void _setUser(final String token){
        this.firebaseManager.getRootReference().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                DataSnapshot userSnapshot = dataSnapshot.child("users").child(token);
                if (userSnapshot.exists()){
                    currentUser = userSnapshot.getValue(User.class);
                    if (currentUser!=null) {
                        transactions = currentUser.getTransactions();
                        accounts = currentUser.getAccounts();

                        _initializeControls();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {}
        });
    }

    private void _initializeControls(){
        this.addTransactionNameTextInputEditText = findViewById(R.id.addTransactionNameTextInputEditText);
        this.addTransactionDescriptionTextInputEditText = findViewById(R.id.addTransactionDescriptionTextInputEditText);
        this.addTransactionValueTextInputEditText = findViewById(R.id.addTransactionValueTextInputEditText);
        this.addTransactionFromAccountSpinner = findViewById(R.id.addTransactionFromAccountSpinner);
        this.addTransactionToAccountSpinner = findViewById(R.id.addTransactionToAccountSpinner);
        this.addTransactionTypeSpinner = findViewById(R.id.addTransactionTypeSpinner);
        this.addTransactionForcastSwitch = findViewById(R.id.addTransactionForcastSwitch);
        this.addTransactionButton = findViewById(R.id.addTransactionButton);
        this.addTransactionCancelButton = findViewById(R.id.addTransactionCancelButton);

        this._loadTransactionTypeSpinner();
        this._loadAccountSpinner();

        this._initializeControlEvents();
    }

    private void _loadTransactionTypeSpinner(){
        ArrayList<TransactionTypeValueHelper> transactionTypeValueHelpers = new ArrayList<>();
        transactionTypeValueHelpers.add(new TransactionTypeValueHelper("POSITIVE","Einnahmen"));
        transactionTypeValueHelpers.add(new TransactionTypeValueHelper("NEGATIVE","Ausgaben"));
        transactionTypeValueHelpers.add(new TransactionTypeValueHelper("NEUTRAL","Transfer"));

        ArrayAdapter<TransactionTypeValueHelper> adapter = new ArrayAdapter<>(getApplicationContext(),android.R.layout.simple_spinner_dropdown_item,transactionTypeValueHelpers);

        this.addTransactionTypeSpinner.setAdapter(adapter);
        this.addTransactionTypeSpinner.setSelection(adapter.getPosition(transactionTypeValueHelpers.get(1)));
    }

    private void _loadAccountSpinner(){
        accountSpinnerAdapter = new AccountSpinnerAdapter(getApplicationContext(),android.R.layout.simple_spinner_item,accounts);
        addTransactionFromAccountSpinner.setAdapter(accountSpinnerAdapter);
        addTransactionToAccountSpinner.setAdapter(accountSpinnerAdapter);
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

    private Transaction _createTransaction(){
        Transaction newTransaction = new Transaction();

        newTransaction.setTransactionName(this.addTransactionNameTextInputEditText.getText().toString());
        newTransaction.setTransactionValue(Double.parseDouble(this.addTransactionValueTextInputEditText.getText().toString()));
        newTransaction.setTransactionDate(new Date());
        newTransaction.setTransactionCreateDate(new Date());
        newTransaction.setTransactionFromAccount(accounts.get(this.addTransactionFromAccountSpinner.getSelectedItemPosition())); // get from sspinner
        newTransaction.setTransactionToAccount(accounts.get(this.addTransactionToAccountSpinner.getSelectedItemPosition())); //get from spinner
        newTransaction.setTransactionType(this.transactionTypeSelectedValue); // Get from spinner
        newTransaction.setTransactionDescription(this.addTransactionDescriptionTextInputEditText.getText().toString());
        newTransaction.setTransactionForecast(false); // // TODO: 17.03.2019  Muss noch behoben werden
        newTransaction.setTransactionCategory(null);

        return newTransaction;
    }

    private void _navigateToPreviousActivity(){
        Intent intent = new Intent(getApplicationContext(),MainActivity.class);
        startActivity(intent);
    }

    private boolean _checkTransactionValidation(Transaction transaction){
        //// TODO: 11.03.2019 Muss noch implementiert werden
        return true;
    }





}
