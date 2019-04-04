/**#################################################################################################
 * Author: Wladimir Tarasov
 * Date: 26.03.2019
 *################################################################################################*/
package com.taracamp.financeplanner;

import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.taracamp.financeplanner.Adapters.TransactionAdapter;
import com.taracamp.financeplanner.Core.FirebaseManager;
import com.taracamp.financeplanner.Models.Account;
import com.taracamp.financeplanner.Models.Transaction;
import com.taracamp.financeplanner.Models.User;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TransactionsActivity extends AppCompatActivity {

    /**#############################################################################################
     * Controls
     *############################################################################################*/
    private TextView transactionsTotalValueTextView;
    private RecyclerView transactionsRecyclerView;
    private FloatingActionButton openAddTransactionDialogFloatingActionButton;

    private RecyclerView.Adapter mAdapter;

    /**#############################################################################################
     * Properties
     *############################################################################################*/
    private FirebaseManager firebaseManager;
    private User currentUser;

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
        this.firebaseManager.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
        this.firebaseManager.onStop();
    }

    /**#############################################################################################
     * Activity Events
     *############################################################################################*/
    @Override
    public void onBackPressed() {
        startActivity(new Intent(getApplicationContext(),MenuActivity.class));
    }

    /**#############################################################################################
     * Controls & Events
     *############################################################################################*/
    private void _initializeControls(){
        this.setContentView(R.layout.activity_transactions);
        this.transactionsTotalValueTextView = findViewById(R.id.transactionsTotalValueTextView);
        this.transactionsRecyclerView = findViewById(R.id.transactionsRecyclerView);
        this.openAddTransactionDialogFloatingActionButton = findViewById(R.id.openAddTransactionDialogFloatingActionButton);
        this._initializeControlEvents();
    }

    private void _initializeControlEvents(){
        this.openAddTransactionDialogFloatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),AddTransactionActivity.class));
            }
        });
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
                    if (currentUser!=null){
                        _loadData();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {}
        });
    }

    /**#############################################################################################
     * Private Methods
     *############################################################################################*/
    private void _loadData(){
        this._initializeControls();

        if (this.currentUser.getTransactions()!=null)Collections.reverse(this.currentUser.getTransactions());
        else this.currentUser.setTransactions(new ArrayList<Transaction>());

        this._loadRecyclerViewData();
        this._loadTotalValueTextView();
    }

    private void _loadTotalValueTextView(){
        Double totalValue;
        if (this.currentUser.getAccounts()!=null) totalValue = _getTotalValue(this.currentUser.getAccounts());
        else totalValue = 0.0;

        this._setTextViewColor(transactionsTotalValueTextView,totalValue);
        this.transactionsTotalValueTextView.setText("");
        this.transactionsTotalValueTextView.setText(totalValue.toString() + "\u20ac");
    }

    private void _loadRecyclerViewData(){
        this.transactionsRecyclerView.setHasFixedSize(true);
        this.transactionsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        this.transactionsRecyclerView.addItemDecoration(new DividerItemDecoration(this,LinearLayoutManager.VERTICAL));
        this.transactionsRecyclerView.setHasFixedSize(true);
        this.mAdapter = new TransactionAdapter(this,currentUser.getTransactions(),firebaseManager,currentUser);
        this.transactionsRecyclerView.setAdapter(this.mAdapter);
    }

    private Double _getTotalValue(List<Account> accounts){
        Double totalValue = 0.0;
        for(Account account: accounts)if (account.isAccountRecordToValue())totalValue = totalValue + account.getAccountValue();
        return totalValue;
    }

    private void _setTextViewColor(TextView control, Double totalValue){
        if (totalValue>0)control.setTextColor(Color.rgb(0,200,0));
        else control.setTextColor(Color.rgb(200,0,0));
    }
}
