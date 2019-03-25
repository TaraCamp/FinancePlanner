/**#################################################################################################
 * Author: Wladimir Tarasov
 * Date: 12.03.2019
 *################################################################################################*/
package com.taracamp.financeplanner;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.LinearLayout;
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
import com.taracamp.financeplanner.Models.Enums.TransactionTypeEnum;
import com.taracamp.financeplanner.Models.Transaction;
import com.taracamp.financeplanner.Models.TransactionTypeValueHelper;
import com.taracamp.financeplanner.Models.User;

import java.util.ArrayList;
import java.util.Calendar;
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
    private TextInputEditText addTransactionForecastDateTextInputEditText;
    private Button addTransactionButton;
    private LinearLayout addTransactionFromAccountLinearLayout;
    private LinearLayout addTransactionToAccountLinearLayout;
    private TextInputLayout addTransactionForecastDateTextInputLayout;

    private AccountSpinnerAdapter accountSpinnerAdapter;
    private DatePickerDialog addTransactionForecastDatePickerDialog;
    private Calendar addTransactionForecastDateCalendar;

    /**#############################################################################################
     * Properties
     *############################################################################################*/
    private User currentUser;
    private List<Transaction> transactions;
    private List<Account> accounts;
    private FirebaseManager firebaseManager;
    private String transactionTypeSelectedValue;
    private boolean isForecastEnabled = false;

    /**#############################################################################################
     * Lifecycles
     *############################################################################################*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this._loginUser();
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
     * Activity Events
     *############################################################################################*/
    @Override
    public void onBackPressed() {
        startActivity(new Intent(getApplicationContext(),MainActivity.class));
    }

    /**#############################################################################################
     * Control & Events
     *############################################################################################*/
    private void _initializeControlEvents(){

        this.addTransactionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                _addTransactionToFirebaseDatabase();
            }
        });
        this.addTransactionTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                TransactionTypeValueHelper transactionTypeValueHelper = (TransactionTypeValueHelper) parent.getSelectedItem();
                transactionTypeSelectedValue = transactionTypeValueHelper.getId();
                _updateUI(transactionTypeSelectedValue);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                transactionTypeSelectedValue = "POSITIVE";
                _updateUI(transactionTypeSelectedValue);
            }
        });

        this.addTransactionForcastSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                isForecastEnabled = isChecked;
                if (isChecked)addTransactionForecastDateTextInputLayout.setVisibility(View.VISIBLE);
                else addTransactionForecastDateTextInputLayout.setVisibility(View.GONE);
            }
        });
        this.addTransactionForecastDateTextInputEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus)
                {
                    addTransactionForecastDatePickerDialog = new DatePickerDialog(AddTransactionActivity.this, new DatePickerDialog.OnDateSetListener() {

                        @Override
                        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth)
                        {
                            addTransactionForecastDateCalendar = Calendar.getInstance();
                            addTransactionForecastDateCalendar.set(Calendar.YEAR,year);
                            addTransactionForecastDateCalendar.set(Calendar.MONTH,month);
                            addTransactionForecastDateCalendar.set(Calendar.DAY_OF_MONTH,dayOfMonth);

                            addTransactionForecastDateTextInputEditText.setText(DateUtils.formatDateTime(AddTransactionActivity.this,
                                    addTransactionForecastDateCalendar.getTimeInMillis(),DateUtils.FORMAT_SHOW_DATE));
                        }
                    },Calendar.getInstance().get(Calendar.YEAR),Calendar.getInstance().get(Calendar.MONTH),Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
                    addTransactionForecastDatePickerDialog.show();
                }
            }
        });
    }

    private void _initializeControls(){
        this.setContentView(R.layout.activity_add_transaction);
        this.addTransactionNameTextInputEditText = findViewById(R.id.addTransactionNameTextInputEditText);
        this.addTransactionDescriptionTextInputEditText = findViewById(R.id.addTransactionDescriptionTextInputEditText);
        this.addTransactionValueTextInputEditText = findViewById(R.id.addTransactionValueTextInputEditText);
        this.addTransactionFromAccountSpinner = findViewById(R.id.addTransactionFromAccountSpinner);
        this.addTransactionToAccountSpinner = findViewById(R.id.addTransactionToAccountSpinner);
        this.addTransactionTypeSpinner = findViewById(R.id.addTransactionTypeSpinner);
        this.addTransactionForcastSwitch = findViewById(R.id.addTransactionForcastSwitch);
        this.addTransactionButton = findViewById(R.id.addTransactionButton);
        this.addTransactionForecastDateTextInputEditText = findViewById(R.id.addTransactionForecastDateTextInputEditText);
        this.addTransactionFromAccountLinearLayout = findViewById(R.id.addTransactionFromAccountLinearLayout);
        this.addTransactionToAccountLinearLayout = findViewById(R.id.addTransactionToAccountLinearLayout);
        this.addTransactionForecastDateTextInputLayout = findViewById(R.id.addTransactionForecastDateTextInputLayout);

        this._loadTransactionTypeSpinner();
        this._loadAccountSpinner();

        this._initializeControlEvents();
    }

    /**#############################################################################################
     * Firebase Auth
     *############################################################################################*/
    private void _loginUser(){
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
                    if (currentUser!=null) _loadData(currentUser);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {}
        });
    }

    /**#############################################################################################
     * Private Methoden
     *############################################################################################*/
    private void _loadData(User currentUser){
        this.accounts = currentUser.getAccounts();
        this.transactions = currentUser.getTransactions();
        this._initializeControls();
    }

    private void _addTransactionToFirebaseDatabase(){
        Transaction newTransaction = this._getTransaction();
        if(this._checkTransactionValidation(newTransaction)){
            this.transactions.add(newTransaction);
            this.currentUser.setTransactions(this.transactions);
            this._changeAccountValueByTransaction(newTransaction);
            this.currentUser.setAccounts(this.accounts);
            if(this.firebaseManager.saveObject(this.currentUser)){
                Message.show(getApplicationContext(),"Eine neue Transaktion wurde angelegt", Message.Mode.SUCCESS);
                startActivity(new Intent(getApplicationContext(),MainActivity.class));
            }
        }
    }

    private void _loadTransactionTypeSpinner(){
        ArrayAdapter<TransactionTypeValueHelper> adapter =
                new ArrayAdapter<>(getApplicationContext(),android.R.layout.simple_spinner_dropdown_item,this._getTransactionTypeList());
        this.addTransactionTypeSpinner.setAdapter(adapter);
        this.addTransactionTypeSpinner.setSelection(adapter.getPosition(adapter.getItem(1)));
    }

    private List<TransactionTypeValueHelper> _getTransactionTypeList(){
        ArrayList<TransactionTypeValueHelper> transactionTypeValueHelpers = new ArrayList<>();
        transactionTypeValueHelpers.add(new TransactionTypeValueHelper(TransactionTypeEnum.POSITIVE.toString(),"Einnahmen"));
        transactionTypeValueHelpers.add(new TransactionTypeValueHelper(TransactionTypeEnum.NEGATIVE.toString(),"Ausgaben"));
        transactionTypeValueHelpers.add(new TransactionTypeValueHelper(TransactionTypeEnum.NEUTRAL.toString(),"Transfer"));
        return transactionTypeValueHelpers;
    }

    private void _loadAccountSpinner(){
        accountSpinnerAdapter = new AccountSpinnerAdapter(getApplicationContext(),android.R.layout.simple_spinner_item,accounts);
        addTransactionFromAccountSpinner.setAdapter(accountSpinnerAdapter);
        addTransactionToAccountSpinner.setAdapter(accountSpinnerAdapter);
    }

    private Transaction _getTransaction(){
        Transaction newTransaction = new Transaction();
        newTransaction.setTransactionName(this.addTransactionNameTextInputEditText.getText().toString());
        newTransaction.setTransactionValue(Double.parseDouble(this.addTransactionValueTextInputEditText.getText().toString()));
        newTransaction.setTransactionDate(new Date());
        newTransaction.setTransactionCreateDate(new Date());
        newTransaction.setTransactionFromAccount(accounts.get(this.addTransactionFromAccountSpinner.getSelectedItemPosition()));
        newTransaction.setTransactionToAccount(accounts.get(this.addTransactionToAccountSpinner.getSelectedItemPosition()));
        newTransaction.setTransactionType(this.transactionTypeSelectedValue); // Get from spinner
        newTransaction.setTransactionDescription(this.addTransactionDescriptionTextInputEditText.getText().toString());
        newTransaction.setTransactionForecast(isForecastEnabled);
        newTransaction.setTransactionCategory(null);

        return newTransaction;
    }

    private boolean _checkTransactionValidation(Transaction transaction){
        //// TODO: 11.03.2019 Muss noch implementiert werden
        return true;
    }

    private void _changeAccountValueByTransaction(Transaction transaction){
        Account toAccount,fromAccount;
        switch (transaction.getTransactionType()){
            case "POSITIVE":{
                toAccount = transaction.getTransactionToAccount();
                Double oldValue = toAccount.getAccountValue();
                toAccount.setAccountValue(oldValue + transaction.getTransactionValue());

                this.accounts.set(this.addTransactionToAccountSpinner.getSelectedItemPosition(),toAccount);
                break;
            }
            case "NEGATIVE":{
                fromAccount = transaction.getTransactionFromAccount();
                Double oldValue = fromAccount.getAccountValue();
                fromAccount.setAccountValue(oldValue - transaction.getTransactionValue());

                int position = this.addTransactionFromAccountSpinner.getSelectedItemPosition();
                this.accounts.set(position,fromAccount);
                break;
            }
            case "NEUTRAL":{
                fromAccount = transaction.getTransactionFromAccount();
                toAccount = transaction.getTransactionToAccount();

                Double oldFromAccountValue = fromAccount.getAccountValue();
                Double oldToAccountValue = toAccount.getAccountValue();

                fromAccount.setAccountValue(oldFromAccountValue - transaction.getTransactionValue());
                toAccount.setAccountValue(oldToAccountValue + transaction.getTransactionValue());

                this.accounts.set(this.addTransactionFromAccountSpinner.getSelectedItemPosition(),fromAccount);
                this.accounts.set(this.addTransactionToAccountSpinner.getSelectedItemPosition(),toAccount);
                break;
            }
        }
    }

    private void _updateUI(String transactionType){
        if (transactionType.equals(TransactionTypeEnum.POSITIVE.toString())){
            this.addTransactionFromAccountLinearLayout.setVisibility(View.GONE);
            this.addTransactionToAccountLinearLayout.setVisibility(View.VISIBLE);
        } else if (transactionType.equals(TransactionTypeEnum.NEGATIVE.toString())){
            this.addTransactionFromAccountLinearLayout.setVisibility(View.VISIBLE);
            this.addTransactionToAccountLinearLayout.setVisibility(View.GONE);
        }else{
            this.addTransactionFromAccountLinearLayout.setVisibility(View.VISIBLE);
            this.addTransactionToAccountLinearLayout.setVisibility(View.VISIBLE);
        }
    }
}
