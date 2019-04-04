/**#################################################################################################
 * Author: Wladimir Tarasov
 * Date: 02.04.2019
 *################################################################################################*/
package com.taracamp.financeplanner.Fragmente.Dialogs;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.taracamp.financeplanner.Core.FirebaseManager;
import com.taracamp.financeplanner.Models.Transaction;
import com.taracamp.financeplanner.Models.User;
import com.taracamp.financeplanner.R;

public class TransactionDialogFragment extends DialogFragment {

    /**#############################################################################################
     * Controls
     *############################################################################################*/
    private EditText transactionDialogTransactionValueEditText;
    private Button transactionDialogChangeButton;

    /**#############################################################################################
     * Properties
     *############################################################################################*/
    private static FirebaseManager firebaseManager;
    private static Transaction transaction;
    private static User currentUser;
    private static int index;

    /**#############################################################################################
     * Constructor
     *############################################################################################*/
    public TransactionDialogFragment(){}

    public static TransactionDialogFragment newInstance(FirebaseManager _firebaseManager, Transaction _task, User _currentUser,int _index)
    {
        TransactionDialogFragment fragment = new TransactionDialogFragment();
        firebaseManager = _firebaseManager;
        transaction = _task;
        currentUser = _currentUser;
        index = _index;

        return fragment;
    }

    /**#############################################################################################
     * Lifecycles
     *############################################################################################*/
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.Dialog);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.fragment_transaction_dialog, container, false);
        this._initializeControls(rootView);
        getDialog().setTitle(transaction.getTransactionName());
        this.transactionDialogTransactionValueEditText.setText(transaction.getTransactionValue().toString());
        return rootView;
    }

    /**#############################################################################################
     * Controls & Events
     *############################################################################################*/
    private void _initializeControls(View root){
        this.transactionDialogTransactionValueEditText = root.findViewById(R.id.transactionDialogTransactionValueEditText);
        this.transactionDialogChangeButton = root.findViewById(R.id.transactionDialogChangeButton);
        this._initializeControlEvents();
    }

    private void _initializeControlEvents(){
        this.transactionDialogChangeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                _changeTransactionValue(index,transactionDialogTransactionValueEditText.getText().toString());
                getDialog().cancel();
            }
        });
    }

    /**#############################################################################################
     * Private Methods
     *############################################################################################*/
    private void _changeTransactionValue(int index,String value) {
        if (_checkValidation(value)){
            transaction.setTransactionValue(Double.parseDouble(value));
            currentUser.getTransactions().set(index,transaction);
            firebaseManager.saveObject(currentUser);
        }
    }

    private boolean _checkValidation(String value){
        return true;
    }
}
