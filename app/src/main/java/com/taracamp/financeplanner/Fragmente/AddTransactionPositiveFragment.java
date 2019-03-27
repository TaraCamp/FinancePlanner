/**#################################################################################################
 * Author: Wladimir Tarasov
 * Date: 26.03.2019
 *################################################################################################*/
package com.taracamp.financeplanner.Fragmente;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;

import com.taracamp.financeplanner.Core.FirebaseManager;
import com.taracamp.financeplanner.Models.Account;
import com.taracamp.financeplanner.Models.Transaction;
import com.taracamp.financeplanner.Models.User;
import com.taracamp.financeplanner.R;

import java.util.List;

public class AddTransactionPositiveFragment extends Fragment {

    /**#############################################################################################
     * Controls
     *############################################################################################*/
        private EditText addTransactionNameEditText;
        private Button addTransactionOpenTemplatesButton;
        private EditText addTransactionDescriptionEditText;
        private EditText addTransactionValueTextInputEditText;
        private Spinner addTransactionToAccountSpinner;
        private Switch addTransactionForcastSwitch;
        private EditText addTransactionForecastDateEditText;
        private Button addTransactionPositiveButton;

    /**#############################################################################################
     * Properties
     *############################################################################################*/
    private static FirebaseManager firebaseManager;
    private boolean isForecastEnabled = false;
    private User currentUser;
    private List<Transaction> transactions;
    private List<Account> accounts;

    /**#############################################################################################
     * Constructer
     *############################################################################################*/
    public AddTransactionPositiveFragment(){}

    public static AddTransactionPositiveFragment newInstance(FirebaseManager firebaseManager)
    {
        AddTransactionPositiveFragment fragment = new AddTransactionPositiveFragment();
        firebaseManager = firebaseManager;
        return fragment;
    }

    /**#############################################################################################
     * Lifecycles
     *############################################################################################*/
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_add_transaction_positive, container, false);
        this._initializeControls(view);
        return view;
    }

    /**#############################################################################################
     * Control Events
     *############################################################################################*/
    private void _initializeControlEvents(){
        this.addTransactionOpenTemplatesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        this.addTransactionPositiveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        this.addTransactionForcastSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                isForecastEnabled = isChecked;
                if (isChecked)addTransactionForecastDateEditText.setVisibility(View.VISIBLE);
                else addTransactionForecastDateEditText.setVisibility(View.GONE);
            }
        });
    }

    private void _initializeControls(View view){
        this.addTransactionNameEditText = view.findViewById(R.id.addTransactionNameEditText);
        this.addTransactionOpenTemplatesButton = view.findViewById(R.id.addTransactionOpenTemplatesButton);
        this.addTransactionDescriptionEditText = view.findViewById(R.id.addTransactionDescriptionEditText);
        this.addTransactionValueTextInputEditText = view.findViewById(R.id.addTransactionValueTextInputEditText);
        this.addTransactionToAccountSpinner = view.findViewById(R.id.addTransactionToAccountSpinner);
        this.addTransactionForcastSwitch = view.findViewById(R.id.addTransactionForcastSwitch);
        this.addTransactionForecastDateEditText = view.findViewById(R.id.addTransactionForecastDateEditText);
        this.addTransactionPositiveButton = view.findViewById(R.id.addTransactionPositiveButton);
        this._initializeControlEvents();
    }

    /**#############################################################################################
     * Private Methods
     *############################################################################################*/
    private void _addTransaction(){
        Transaction transaction = this._getTransaction();
        if (_checkTransactionValidation(transaction)){
            this.transactions.add(transaction);
            this.currentUser.setTransactions(this.transactions);
            this._changeAccountValueByTransaction(transaction);

        }
    }

    private Transaction _getTransaction(){
        Transaction newTransaction = new Transaction();
        //newTransaction.setTransactionName(this.addTransactionNameTextInputEditText.getText().toString());
        //newTransaction.setTransactionValue(Double.parseDouble(this.addTransactionValueTextInputEditText.getText().toString()));
        //newTransaction.setTransactionDate(new Date());
        //newTransaction.setTransactionCreateDate(new Date());
        //newTransaction.setTransactionFromAccount(accounts.get(this.addTransactionFromAccountSpinner.getSelectedItemPosition()));
        //newTransaction.setTransactionToAccount(accounts.get(this.addTransactionToAccountSpinner.getSelectedItemPosition()));
        //newTransaction.setTransactionType(this.transactionTypeSelectedValue); // Get from spinner
        //newTransaction.setTransactionDescription(this.addTransactionDescriptionTextInputEditText.getText().toString());
        //newTransaction.setTransactionForecast(isForecastEnabled);
        //newTransaction.setTransactionCategory(null);

        return newTransaction;
    }

    private boolean _checkTransactionValidation(Transaction transaction){
        //// TODO: 11.03.2019 Muss noch implementiert werden
        return true;
    }

    private void _changeAccountValueByTransaction(Transaction transaction){

    }
}
