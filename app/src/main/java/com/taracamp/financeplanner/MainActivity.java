/**#################################################################################################
 * Author: Wladimir Tarasov
 * Date: 18.03.2019
 *################################################################################################*/
package com.taracamp.financeplanner;

import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.taracamp.financeplanner.Adapters.TransactionAdapter;
import com.taracamp.financeplanner.Core.FirebaseManager;
import com.taracamp.financeplanner.Models.Account;
import com.taracamp.financeplanner.Models.User;

import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "familyplan.debug";
    private static final String CLASS = "MainActivity";

    /**#############################################################################################
     * Controls
     *############################################################################################*/
    private TextView TotalValueTextView;
    private Button navigateToAddTransactionActivityButton;
    private Button navigateToAccountsActivityButton;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;

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
        setContentView(R.layout.activity_main);
        Log.d(TAG,CLASS+".onCreate()");

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
        // your code.
    }

    /**#############################################################################################
     * Controls & Events
     *############################################################################################*/
    private void _initializeControlEvents(){
        this.navigateToAddTransactionActivityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),AddTransactionActivity.class));
            }
        });
        this.navigateToAccountsActivityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),AccountsActivity.class));
            }
        });
    }

    private void _initializeControls(){
        this.TotalValueTextView = findViewById(R.id.TotalValueTextView2);
        this.recyclerView = findViewById(R.id.TransactionsRecyclerView);
        this.navigateToAddTransactionActivityButton = findViewById(R.id.navigateToAddTransactionActivityButton);
        this.navigateToAccountsActivityButton = findViewById(R.id.navigateToAccountsActivityButton);
        this.recyclerView.setHasFixedSize(true);
        this.layoutManager = new LinearLayoutManager(this);
        this.recyclerView.setLayoutManager(this.layoutManager);

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
                    if (currentUser!=null){
                        _loadData(currentUser);
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
    private void _loadData(User currentUser){
        this._initializeControls();
        //this.mAdapter = new TransactionAdapter(this,firebaseManager,currentUser.getTransactions());
        //this.recyclerView.setAdapter(this.mAdapter);

        Collections.reverse(currentUser.getTransactions());
        Double totalValue = _getTotalValue(currentUser.getAccounts());
        _setTextViewColor(TotalValueTextView,totalValue);
        TotalValueTextView.setText("");
        TotalValueTextView.setText(totalValue.toString() + "\u20ac");
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
