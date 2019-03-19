/**#################################################################################################
 * Author: Wladimir Tarasov
 * Date: 19.03.2019
 *################################################################################################*/
package com.taracamp.financeplanner;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.taracamp.financeplanner.Adapters.AccountAdapter;
import com.taracamp.financeplanner.Core.FirebaseManager;
import com.taracamp.financeplanner.Models.User;

public class AccountsActivity extends AppCompatActivity {

    /**#############################################################################################
     * Controls
     *############################################################################################*/
    private RecyclerView accountsRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private Button addAccountButton;

    /**#############################################################################################
     * Properties
     *############################################################################################*/
    private User currentUser;
    private FirebaseManager firebaseManager;

    /**#############################################################################################
     * Lifecycles
     *############################################################################################*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accounts);

        this._loginUser();
    }

    @Override
    public void onStart() {
        super.onStart();
        if (this.firebaseManager != null) this.firebaseManager.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
        if (this.firebaseManager != null) this.firebaseManager.onStop();
    }

    /**#############################################################################################
     * Controls & Events
     *############################################################################################*/
    private void _initializeControlEvents(){
        this.addAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                _addAccount();
            }
        });
    }

    private void _initializeControls(){
        this.accountsRecyclerView = findViewById(R.id.accountsRecyclerView);
        this.accountsRecyclerView.setHasFixedSize(true);
        this.accountsRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        this.addAccountButton = findViewById(R.id.addAccountButton);

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
                    if (currentUser!=null) {
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
        this.mAdapter = new AccountAdapter(currentUser.getAccounts());
        this.accountsRecyclerView.setAdapter(this.mAdapter);
    }

    private void _addAccount(){

    }
}
