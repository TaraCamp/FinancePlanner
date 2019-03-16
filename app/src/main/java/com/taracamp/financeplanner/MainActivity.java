package com.taracamp.financeplanner;

import android.content.Intent;
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
import com.taracamp.financeplanner.Models.Transaction;
import com.taracamp.financeplanner.Models.User;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "familyplan.debug";
    private static final String CLASS = "MainActivity";

    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;

    private TextView TotalValueTextView;
    private Button navigateToAddTransactionActivityButton;

    private FirebaseManager firebaseManager;
    private User currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(TAG,CLASS+".onCreate()");

        this._initializeControls();
        this._loginFirebaseUser();
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

    private void _loginFirebaseUser(){
        this.firebaseManager = new FirebaseManager();
        this.firebaseManager.mAuth = FirebaseAuth.getInstance();
        this.firebaseManager.mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser()!=null){
                    loadCurrentUser(firebaseAuth.getCurrentUser().getUid());
                }
            }
        };
    }

    private void loadCurrentUser(final String token){
        this.firebaseManager.getRootReference().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                DataSnapshot userSnapshot = dataSnapshot.child("users").child(token);
                if (userSnapshot.exists()){
                    currentUser = userSnapshot.getValue(User.class);

                    if (currentUser!=null){
                        _fillTransactionRecyclerView(currentUser.getTransactions());
                        _loadTotalValue(currentUser.getAccounts());
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {}
        });
    }

    private void _fillTransactionRecyclerView(List<Transaction> transactions){
        this.mAdapter = new TransactionAdapter(transactions);
        this.recyclerView.setAdapter(this.mAdapter);
    }

    private void _loadTotalValue(List<Account> accounts){
        this.TotalValueTextView = findViewById(R.id.TotalValueTextView);

        Double totalValue = 0.0;
        for(Account account: accounts){
            totalValue = totalValue + account.getAccountValue();
        }

        this.TotalValueTextView.setText(totalValue.toString());
    }

    private void _initializeControls(){
        this.recyclerView = findViewById(R.id.TransactionsRecyclerView);
        this.navigateToAddTransactionActivityButton = findViewById(R.id.navigateToAddTransactionActivityButton);

        this.recyclerView.setHasFixedSize(true);
        this.layoutManager = new LinearLayoutManager(this);
        this.recyclerView.setLayoutManager(this.layoutManager);

        this._initializeControlEvents();
    }

    private void _initializeControlEvents(){
        this.navigateToAddTransactionActivityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),AddTransactionActivity.class);
                startActivity(intent);
            }
        });
    }
}
