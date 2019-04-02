/**#################################################################################################
 * Author: Wladimir Tarasov
 * Date: 22.03.2019
 *################################################################################################*/
package com.taracamp.financeplanner;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Switch;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.taracamp.financeplanner.Core.FirebaseManager;
import com.taracamp.financeplanner.Models.User;

public class                 TransactionDetailActivity extends AppCompatActivity {

    /**#############################################################################################
     * Controls
     *############################################################################################*/
    private Spinner detailTransactionTypeSpinner;
    private Spinner detailTransactionCategorySpinner;
    private TextInputEditText detailTransactionNameTextInputEditText;
    private TextInputEditText detailTransactionDescriptionTextInputEditText;
    private TextInputEditText detailTransactionValueTextInputEditText;
    private Spinner detailTransactionFromAccountSpinner;
    private Spinner detailTransactionToAccountSpinner;
    private Switch detailTransactionForcastSwitch;
    private Button changeTransactionButton;

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
        setContentView(R.layout.activity_transaction_detail);
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
     * Activity Events
     *############################################################################################*/
    @Override
    public void onBackPressed() {
        startActivity(new Intent(getApplicationContext(),MainActivity.class));
    }

    /**#############################################################################################
     * Controls & Events
     *############################################################################################*/
    private void _initializeControlEvents(){
        this.changeTransactionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        this.detailTransactionTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void _initializeControls(){
        this.detailTransactionTypeSpinner = findViewById(R.id.detailTransactionTypeSpinner);
        this.detailTransactionCategorySpinner = findViewById(R.id.detailTransactionCategorySpinner);
        this.detailTransactionNameTextInputEditText = findViewById(R.id.detailTransactionNameTextInputEditText);
        this.detailTransactionDescriptionTextInputEditText = findViewById(R.id.detailTransactionDescriptionTextInputEditText);
        this.detailTransactionValueTextInputEditText = findViewById(R.id.detailTransactionValueTextInputEditText);
        this.detailTransactionFromAccountSpinner = findViewById(R.id.detailTransactionFromAccountSpinner);
        this.detailTransactionToAccountSpinner = findViewById(R.id.detailTransactionToAccountSpinner);
        this.detailTransactionForcastSwitch = findViewById(R.id.detailTransactionForcastSwitch);
        this.changeTransactionButton = findViewById(R.id.changeTransactionButton);
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
    }
}
