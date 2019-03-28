/**#################################################################################################
 * Author: Wladimir Tarasov
 * Date: 28.03.2019
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

import com.taracamp.financeplanner.Adapters.AccountSpinnerAdapter;
import com.taracamp.financeplanner.Core.FirebaseManager;
import com.taracamp.financeplanner.Core.Message;
import com.taracamp.financeplanner.Models.Account;
import com.taracamp.financeplanner.Models.Enums.TransactionTypeEnum;
import com.taracamp.financeplanner.Models.Transaction;
import com.taracamp.financeplanner.Models.User;
import com.taracamp.financeplanner.R;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class AddTransactionNegativeFragment extends Fragment {

    /**#############################################################################################
     * Controls
     *############################################################################################*/
    private EditText addTransactionNegativeNameEditText;
    private Button addTransactionNegativeOpenTemplatesButton;
    private EditText addTransactionNegativeDescriptionEditText;
    private EditText addTransactionNegativeValueTextInputEditText;
    private Spinner addTransactionFromAccountSpinner;
    private Switch addTransactionNegativeForcastSwitch;
    private EditText addTransactionNegativeForecastDateEditText;
    private Button addTransactionNegativeButton;

    /**#############################################################################################
     * Properties
     *############################################################################################*/
    private static FirebaseManager firebaseManager;
    private boolean isForecastEnabled = false;
    private static User currentUser;
    private static List<Transaction> transactions;
    private static List<Account> accounts;

    /**#############################################################################################
     * Constructer
     *############################################################################################*/
    public AddTransactionNegativeFragment() {}

    public static AddTransactionNegativeFragment newInstance(FirebaseManager _firebaseManager,User _currentUser) {
        AddTransactionNegativeFragment fragment = new AddTransactionNegativeFragment();
        firebaseManager = _firebaseManager;
        currentUser = _currentUser;

        if (currentUser.getTransactions()!=null)transactions = currentUser.getTransactions();
        else transactions = new ArrayList<>();

        if (currentUser.getAccounts()!=null)accounts = currentUser.getAccounts();
        else accounts = new ArrayList<>();
        return fragment;
    }

    /**#############################################################################################
     * Lifecycles
     *############################################################################################*/
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_add_transaction_negative, container, false);
        this._initializeControls(view);
        this._loadAccountSpinner();
        return view;
    }

    /**#############################################################################################
     * Control Events
     *############################################################################################*/
    private void _initializeControls(View view){
        this.addTransactionNegativeNameEditText = view.findViewById(R.id.addTransactionNegativeNameEditText);
        this.addTransactionNegativeOpenTemplatesButton = view.findViewById(R.id.addTransactionNegativeOpenTemplatesButton);
        this.addTransactionNegativeDescriptionEditText = view.findViewById(R.id.addTransactionNegativeDescriptionEditText);
        this.addTransactionNegativeValueTextInputEditText = view.findViewById(R.id.addTransactionNegativeValueTextInputEditText);
        this.addTransactionFromAccountSpinner = view.findViewById(R.id.addTransactionFromAccountSpinner);
        this.addTransactionNegativeForcastSwitch = view.findViewById(R.id.addTransactionNegativeForcastSwitch);
        this.addTransactionNegativeForecastDateEditText = view.findViewById(R.id.addTransactionNegativeForecastDateEditText);
        this.addTransactionNegativeButton = view.findViewById(R.id.addTransactionNegativeButton);
        this._initializeControlEvents();
    }

    private void _initializeControlEvents(){
        this.addTransactionNegativeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        this.addTransactionNegativeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                _addTransaction();
            }
        });
        this.addTransactionNegativeForcastSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                isForecastEnabled = isChecked;
                if (isChecked)addTransactionNegativeForecastDateEditText.setVisibility(View.VISIBLE);
                else addTransactionNegativeForecastDateEditText.setVisibility(View.GONE);
            }
        });
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
            this.currentUser.setAccounts(this.accounts);

            if (this.firebaseManager.saveObject(this.currentUser)){
                Message.show(getActivity(),"Eine neue Transaktion wurde angelegt", Message.Mode.SUCCESS);
            }else{
                Message.show(getActivity(),"Transaktion konnte nicht angelegt werden", Message.Mode.ERROR);
            }
        }
    }

    private Transaction _getTransaction(){
        Transaction newTransaction = new Transaction();
        newTransaction.setTransactionName(this.addTransactionNegativeNameEditText.getText().toString());
        newTransaction.setTransactionValue(Double.parseDouble(this.addTransactionNegativeValueTextInputEditText.getText().toString()));
        newTransaction.setTransactionDate(new Date());
        newTransaction.setTransactionCreateDate(new Date());
        newTransaction.setTransactionToAccount(accounts.get(this.addTransactionFromAccountSpinner.getSelectedItemPosition()));
        newTransaction.setTransactionType(TransactionTypeEnum.POSITIVE.toString());
        newTransaction.setTransactionDescription(this.addTransactionNegativeDescriptionEditText.getText().toString());
        newTransaction.setTransactionForecast(isForecastEnabled);
        //newTransaction.setTransactionCategory(null); //// TODO: 27.03.2019 überprüfen ob notwendig

        return newTransaction;
    }

    private boolean _checkTransactionValidation(Transaction transaction){
        //// TODO: 11.03.2019 Muss noch implementiert werden
        return true;
    }

    private void _changeAccountValueByTransaction(Transaction transaction){

    }

    private void _loadAccountSpinner(){
        AccountSpinnerAdapter accountSpinnerAdapter = new AccountSpinnerAdapter(getActivity(),android.R.layout.simple_spinner_item,this.accounts);
        addTransactionFromAccountSpinner.setAdapter(accountSpinnerAdapter);
    }
}
