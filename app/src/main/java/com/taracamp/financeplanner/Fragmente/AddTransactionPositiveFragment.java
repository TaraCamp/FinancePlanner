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

import com.taracamp.financeplanner.Adapters.AccountSpinnerAdapter;
import com.taracamp.financeplanner.Core.FirebaseManager;
import com.taracamp.financeplanner.Core.Message;
import com.taracamp.financeplanner.Models.Account;
import com.taracamp.financeplanner.Models.Enums.TransactionTypeEnum;
import com.taracamp.financeplanner.Models.Transaction;
import com.taracamp.financeplanner.Models.User;
import com.taracamp.financeplanner.R;
import com.wajahatkarim3.easymoneywidgets.EasyMoneyEditText;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class AddTransactionPositiveFragment extends Fragment {

    /**#############################################################################################
     * Constants
     *############################################################################################*/
    private final static int DECIMAL_NUMBER = 2;

    /**#############################################################################################
     * Controls
     *############################################################################################*/
        private EditText addTransactionNameEditText;
        private Button addTransactionOpenTemplatesButton;
        private EditText addTransactionDescriptionEditText;
        private EasyMoneyEditText addTransactionValueMoneyEditText;
        private Spinner addTransactionToAccountSpinner;
        private Switch addTransactionForcastSwitch;
        private EditText addTransactionForecastDateEditText;
        private Button addTransactionPositiveButton;

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
    public AddTransactionPositiveFragment(){}

    public static AddTransactionPositiveFragment newInstance(FirebaseManager _firebaseManager,User _currentUser)
    {
        AddTransactionPositiveFragment fragment = new AddTransactionPositiveFragment();
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        final View view = inflater.inflate(R.layout.fragment_add_transaction_positive, container, false);
        this._initializeControls(view);
        this._loadAccountSpinner();
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
                _addTransaction();
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
        this.addTransactionValueMoneyEditText = view.findViewById(R.id.addTransactionValueMoneyEditText);
        this.addTransactionToAccountSpinner = view.findViewById(R.id.addTransactionToAccountSpinner);
        this.addTransactionForcastSwitch = view.findViewById(R.id.addTransactionForcastSwitch);
        this.addTransactionForecastDateEditText = view.findViewById(R.id.addTransactionForecastDateEditText);
        this.addTransactionPositiveButton = view.findViewById(R.id.addTransactionPositiveButton);

        this._configurationMoneyEditText();
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
        newTransaction.setTransactionName(this.addTransactionNameEditText.getText().toString());
        newTransaction.setTransactionValue(this._getDoubleValue(this.addTransactionValueMoneyEditText.getValueString()));
        newTransaction.setTransactionDate(new Date());
        newTransaction.setTransactionCreateDate(new Date());
        newTransaction.setTransactionToAccount(accounts.get(this.addTransactionToAccountSpinner.getSelectedItemPosition()));
        newTransaction.setTransactionType(TransactionTypeEnum.POSITIVE.toString());
        newTransaction.setTransactionDescription(this.addTransactionDescriptionEditText.getText().toString());
        newTransaction.setTransactionForecast(isForecastEnabled);
        //newTransaction.setTransactionCategory(null); //// TODO: 27.03.2019 überprüfen ob notwendig

        return newTransaction;
    }

    private Double _getDoubleValue(String value){
        BigDecimal decimalNumber = new BigDecimal(Double.parseDouble(value));
        return decimalNumber.setScale(DECIMAL_NUMBER,RoundingMode.HALF_UP).doubleValue();
    }

    private void _configurationMoneyEditText(){
        this.addTransactionValueMoneyEditText.setCurrency("€");
        this.addTransactionValueMoneyEditText.showCurrencySymbol();
        this.addTransactionValueMoneyEditText.hideCommas();
    }

    private boolean _checkTransactionValidation(Transaction transaction){
        //// TODO: 11.03.2019 Muss noch implementiert werden
        return true;
    }

    private void _changeAccountValueByTransaction(Transaction transaction){
        Account account = transaction.getTransactionToAccount();

        BigDecimal oldValue = new BigDecimal(account.getAccountValue());
        BigDecimal transactionValue = new BigDecimal(transaction.getTransactionValue());
        BigDecimal newValue = oldValue.add(transactionValue).setScale(DECIMAL_NUMBER, RoundingMode.HALF_UP);

        account.setAccountValue(newValue.doubleValue());
        this.accounts.set(this.addTransactionToAccountSpinner.getSelectedItemPosition(),account);
    }

    private void _loadAccountSpinner(){
        AccountSpinnerAdapter accountSpinnerAdapter = new AccountSpinnerAdapter(getActivity(),android.R.layout.simple_spinner_item,this.accounts);
        addTransactionToAccountSpinner.setAdapter(accountSpinnerAdapter);
    }

}
