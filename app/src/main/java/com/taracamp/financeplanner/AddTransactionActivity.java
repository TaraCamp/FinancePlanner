package com.taracamp.financeplanner;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.taracamp.financeplanner.Core.FirebaseManager;
import com.taracamp.financeplanner.Core.Message;
import com.taracamp.financeplanner.Models.Transaction;

import java.util.Date;

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

    private FirebaseManager firebaseManager;

    /**#############################################################################################
     * Lifecycles
     *############################################################################################*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_transaction);

        this.firebaseManager = new FirebaseManager();

        this._initializeControls();
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

    private void _insertTransaction(Transaction transaction){
        if(this.firebaseManager.saveObject(transaction)){
            Message.show(getApplicationContext(),"Eine neue Transaktion wurde angelegt", Message.Mode.SUCCESS);
            Intent intent = new Intent(getApplicationContext(),MainActivity.class);
            startActivity(intent);
        }
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

        return newTransaction;
    }

    private void _navigateToPreviousActivity(){
        //// TODO: 11.03.2019  Muss implementiert werden
    }

    private boolean _checkTransactionValidation(Transaction transaction){
        //// TODO: 11.03.2019 Muss noch implementiert werden
        return true;
    }

    /**#############################################################################################
     * Events
     *############################################################################################*/
    private void _initializeControlEvents(){
        this.addTransactionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*
                Transaction newTransaction = _createTransaction();
                boolean isTransactionValid = _checkTransactionValidation(newTransaction);
                if(isTransactionValid)_insertTransaction(newTransaction);
                */
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
