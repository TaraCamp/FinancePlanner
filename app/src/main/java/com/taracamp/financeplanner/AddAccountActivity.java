/**#################################################################################################
 * Author: Wladimir Tarasov
 * Date: 20.03.2019
 *################################################################################################*/
package com.taracamp.financeplanner;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Spinner;
import android.widget.Switch;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.taracamp.financeplanner.Core.FirebaseManager;
import com.taracamp.financeplanner.Core.Message;
import com.taracamp.financeplanner.Models.Account;
import com.taracamp.financeplanner.Models.User;

import java.util.Date;
import java.util.List;

public class AddAccountActivity extends AppCompatActivity {

    /**#############################################################################################
     * Controls
     *############################################################################################*/
    private Spinner addAccountTypeSpinner;
    private TextInputEditText addAccountNameTextInputEditText;
    private TextInputEditText addAccountDescriptionTextInputEditText;
    private TextInputEditText addAccountValueTextInputEditText;
    private Switch addAccountRecordToValueSwitch;
    private Button addAccountButton;

    /**#############################################################################################
     * Properties
     *############################################################################################*/
    private User currentUser;
    private FirebaseManager firebaseManager;
    private boolean isRecordToValueEnabled = false;
    private List<Account> accounts;

    /**#############################################################################################
     * Lifecycles
     *############################################################################################*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_account);
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
        this.addAccountRecordToValueSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                isRecordToValueEnabled = isChecked;
            }
        });
    }

    private void _initializeControls(){
        this.addAccountTypeSpinner = findViewById(R.id.addAccountTypeSpinner);
        this.addAccountNameTextInputEditText = findViewById(R.id.addAccountNameTextInputEditText);
        this.addAccountDescriptionTextInputEditText = findViewById(R.id.addAccountDescriptionTextInputEditText);
        this.addAccountValueTextInputEditText = findViewById(R.id.addAccountValueTextInputEditText);
        this.addAccountRecordToValueSwitch = findViewById(R.id.addAccountRecordToValueSwitch);
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
        this.accounts = currentUser.getAccounts();
    }

    private void _addAccount(){
        Account newAccount = this._getAccount();
        if (_checkAccount(newAccount)){
            this.accounts.add(newAccount);
            this.currentUser.setAccounts(this.accounts);
            if (this.firebaseManager.saveObject(this.currentUser)){
                Message.show(getApplicationContext(),"Ein neues Konto wurde angelegt", Message.Mode.SUCCESS);
                startActivity(new Intent(getApplicationContext(),AccountsActivity.class));
            }
        }
    }

    private Account _getAccount(){
        Account account = new Account();
        account.setAccountName(this.addAccountNameTextInputEditText.getText().toString());
        account.setAccountDescription(this.addAccountDescriptionTextInputEditText.getText().toString());
        account.setAccountValue(Double.parseDouble(addAccountValueTextInputEditText.getText().toString()));
        account.setAccoutCreateDate(new Date());
        account.setAccountType("MAIN");
        account.setAccountRecordToValue(isRecordToValueEnabled);
        return account;
    }

    private boolean _checkAccount(Account account){
        return true;
    }
}
