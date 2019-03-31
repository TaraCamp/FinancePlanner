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
import com.wajahatkarim3.easymoneywidgets.EasyMoneyEditText;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class AddTransactionNeutralFragment extends Fragment {

    /**#############################################################################################
     * Constants
     *############################################################################################*/
    private final static int DECIMAL_NUMBER = 2;

    /**#############################################################################################
     * Controls
     *############################################################################################*/
    private EditText addTransactionNeutralNameEditText;
    private Button addTransactionNeutralOpenTemplatesButton;
    private EditText addTransactionNeutralDescriptionEditText;
    private EasyMoneyEditText addTransactionNeutralValueMoneyEditText;
    private Spinner addTransactionNeutralToAccountSpinner;
    private Spinner addTransactionNeutralFromAccountSpinner;
    private Button addTransactionTransferButton;

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
    public AddTransactionNeutralFragment() { }

    public static AddTransactionNeutralFragment newInstance(FirebaseManager _firebaseManager,User _currentUser) {
        AddTransactionNeutralFragment fragment = new AddTransactionNeutralFragment();
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
        final View view = inflater.inflate(R.layout.fragment_add_transaction_neutral, container, false);
        this._initializeControls(view);
        this._loadAccountSpinner();
        return view;
    }

    /**#############################################################################################
     * Control Events
     *############################################################################################*/
    private void _initializeControlEvents(){
        this.addTransactionTransferButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                _transfer();
            }
        });
    }

    private void _initializeControls(View view){
        this.addTransactionNeutralNameEditText = view.findViewById(R.id.addTransactionNeutralNameEditText);
        this.addTransactionNeutralOpenTemplatesButton  = view.findViewById(R.id.addTransactionNeutralOpenTemplatesButton);
        this.addTransactionNeutralValueMoneyEditText = view.findViewById(R.id.addTransactionNeutralValueMoneyEditText);
        this.addTransactionNeutralToAccountSpinner = view.findViewById(R.id.addTransactionNeutralToAccountSpinner);
        this.addTransactionNeutralFromAccountSpinner = view.findViewById(R.id.addTransactionNeutralFromAccountSpinner);
        this.addTransactionTransferButton = view.findViewById(R.id.addTransactionTransferButton);
        this._configurationMoneyEditText();
        this._initializeControlEvents();
    }

    /**#############################################################################################
     * Private Methods
     *############################################################################################*/
    private void _transfer(){
        Transaction transaction = this._getTransaction();
        if (_checkTransactionValidation(transaction)){
            this.transactions.add(transaction);
            this.currentUser.setTransactions(this.transactions);

            this._changeAccountValueByTransaction(transaction);

            this.currentUser.setAccounts(this.accounts);

            if (this.firebaseManager.saveObject(this.currentUser)){
                Message.show(getActivity(),"Transfer wurde angewendet", Message.Mode.SUCCESS);
            }else{
                Message.show(getActivity(),"Transaktion konnte nicht angelegt werden", Message.Mode.ERROR);
            }
        }
    }

    private Transaction _getTransaction(){
        Transaction newTransaction = new Transaction();
        newTransaction.setTransactionName(this.addTransactionNeutralNameEditText.getText().toString());
        newTransaction.setTransactionValue(this._getDoubleValue(this.addTransactionNeutralValueMoneyEditText.getValueString()));
        newTransaction.setTransactionDate(new Date());
        newTransaction.setTransactionCreateDate(new Date());
        newTransaction.setTransactionFromAccount(accounts.get(this.addTransactionNeutralFromAccountSpinner.getSelectedItemPosition()));
        newTransaction.setTransactionToAccount(accounts.get(this.addTransactionNeutralToAccountSpinner.getSelectedItemPosition()));
        newTransaction.setTransactionType(TransactionTypeEnum.NEUTRAL.toString());
        //newTransaction.setTransactionDescription(this.addTransactionDescriptionEditText.getText().toString());
        //newTransaction.setTransactionForecast(isForecastEnabled);
        //newTransaction.setTransactionCategory(null); //// TODO: 27.03.2019 überprüfen ob notwendig

        return newTransaction;
    }

    private Double _getDoubleValue(String value){
        BigDecimal decimalNumber = new BigDecimal(Double.parseDouble(value));
        return decimalNumber.setScale(DECIMAL_NUMBER, RoundingMode.HALF_UP).doubleValue();
    }

    private boolean _checkTransactionValidation(Transaction transaction){
        //// TODO: 11.03.2019 Muss noch implementiert werden
        return true;
    }

    private void _configurationMoneyEditText(){
        this.addTransactionNeutralValueMoneyEditText.setCurrency("€");
        this.addTransactionNeutralValueMoneyEditText.showCurrencySymbol();
        this.addTransactionNeutralValueMoneyEditText.hideCommas();
    }

    private void _changeAccountValueByTransaction(Transaction transaction){
        Account accountFrom = transaction.getTransactionFromAccount();
        Account accountTo = transaction.getTransactionToAccount();

        BigDecimal accountFromTotalValue = new BigDecimal(accountFrom.getAccountValue());
        BigDecimal accountToTotalValue = new BigDecimal(accountTo.getAccountValue());

        BigDecimal transactionValue = new BigDecimal(transaction.getTransactionValue());

        BigDecimal newAccountFromTotalValue = accountFromTotalValue.subtract(transactionValue).setScale(DECIMAL_NUMBER, RoundingMode.HALF_UP);
        BigDecimal newAccountToTotalValue = accountToTotalValue.add(transactionValue).setScale(DECIMAL_NUMBER, RoundingMode.HALF_UP);

        accountFrom.setAccountValue(newAccountFromTotalValue.doubleValue());
        accountTo.setAccountValue(newAccountToTotalValue.doubleValue());

        this.accounts.set(this.addTransactionNeutralFromAccountSpinner.getSelectedItemPosition(),accountFrom);
        this.accounts.set(this.addTransactionNeutralToAccountSpinner.getSelectedItemPosition(),accountTo);
    }

    private void _loadAccountSpinner(){
       AccountSpinnerAdapter accountSpinnerAdapter = new AccountSpinnerAdapter(getActivity(),android.R.layout.simple_spinner_item,this.accounts);
       this.addTransactionNeutralFromAccountSpinner.setAdapter(accountSpinnerAdapter);
       this.addTransactionNeutralToAccountSpinner.setAdapter(accountSpinnerAdapter);
    }
}
