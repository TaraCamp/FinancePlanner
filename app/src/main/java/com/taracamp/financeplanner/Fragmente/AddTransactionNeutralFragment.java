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

public class AddTransactionNeutralFragment extends Fragment {

    /**#############################################################################################
     * Controls
     *############################################################################################*/
    private EditText addTransactionNeutralNameEditText;
    private Button addTransactionNeutralOpenTemplatesButton;
    private EditText addTransactionNeutralDescriptionEditText;
    private EditText addTransactionNeutralValueTextInputEditText;
    private Spinner addTransactionNeutralToAccountSpinner;
    private Spinner getAddTransactionNeutralFromoAccountSpinner;
    private Switch addTransactionNeutralForcastSwitch;
    private EditText addTransactionNeutralForecastDateEditText;
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
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_transaction_neutral, container, false);
    }

    /**#############################################################################################
     * Control Events
     *############################################################################################*/
    private void _initializeControlEvents(){

    }

    private void _initializeControls(View view){
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
        newTransaction.setTransactionName(this.addTransactionNeutralNameEditText.getText().toString());
        //newTransaction.setTransactionValue(Double.parseDouble(this.addTransactionValueTextInputEditText.getText().toString()));
        newTransaction.setTransactionDate(new Date());
        newTransaction.setTransactionCreateDate(new Date());
        //newTransaction.setTransactionToAccount(accounts.get(this.addTransactionToAccountSpinner.getSelectedItemPosition()));
        newTransaction.setTransactionType(TransactionTypeEnum.NEUTRAL.toString());
        //newTransaction.setTransactionDescription(this.addTransactionDescriptionEditText.getText().toString());
        //newTransaction.setTransactionForecast(isForecastEnabled);
        //newTransaction.setTransactionCategory(null); //// TODO: 27.03.2019 überprüfen ob notwendig

        return newTransaction;
    }

    private boolean _checkTransactionValidation(Transaction transaction){
        //// TODO: 11.03.2019 Muss noch implementiert werden
        return true;
    }

    private void _changeAccountValueByTransaction(Transaction transaction){
        Account accountTo = transaction.getTransactionToAccount();
        Account accountFrom = transaction.getTransactionFromAccount();

        Double accountFromTotalValue = accountFrom.getAccountValue();
        Double accountToTotalValue = accountTo.getAccountValue();

        accountFrom.setAccountValue(accountFromTotalValue - transaction.getTransactionValue());
        accountTo.setAccountValue(accountToTotalValue + transaction.getTransactionValue());


        this.accounts.set(this.addTransactionNeutralToAccountSpinner.getSelectedItemPosition(),accountTo);
        // der andere auch noch?
    }

    private void _loadAccountSpinner(){
       // AccountSpinnerAdapter accountSpinnerAdapter = new AccountSpinnerAdapter(getActivity(),android.R.layout.simple_spinner_item,this.accounts);
       // addTransactionToAccountSpinner.setAdapter(accountSpinnerAdapter);
    }
}
