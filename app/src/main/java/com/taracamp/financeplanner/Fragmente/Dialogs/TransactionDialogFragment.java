/**#################################################################################################
 * Author: Wladimir Tarasov
 * Date: 02.04.2019
 *################################################################################################*/
package com.taracamp.financeplanner.Fragmente.Dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import com.taracamp.financeplanner.Core.FirebaseManager;
import com.taracamp.financeplanner.Models.Transaction;
import com.taracamp.financeplanner.R;

public class TransactionDialogFragment extends DialogFragment {

    /**#############################################################################################
     * Controls
     *############################################################################################*/

    /**#############################################################################################
     * Properties
     *############################################################################################*/
    private static FirebaseManager firebaseManager;
    private static Transaction transaction;

    /**#############################################################################################
     * Constructor
     *############################################################################################*/
    public TransactionDialogFragment(){}

    public static TransactionDialogFragment newInstance(FirebaseManager _firebaseManager,Transaction _task)
    {
        TransactionDialogFragment fragment = new TransactionDialogFragment();
        firebaseManager = _firebaseManager;
        transaction = _task;

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

        getDialog().setTitle("Aufgabe : " + transaction.getTransactionName());

        return rootView;
    }

}
