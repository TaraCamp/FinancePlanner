/**#################################################################################################
 * Author: Wladimir Tarasov
 * Date: 19.03.2019
 *################################################################################################*/
package com.taracamp.financeplanner;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
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
import com.taracamp.financeplanner.Models.AccountTypeValueHelper;
import com.taracamp.financeplanner.Models.Enums.AccountTypeEnum;
import com.taracamp.financeplanner.Models.User;

import java.util.ArrayList;
import java.util.List;

public class AccountDetailActivity extends AppCompatActivity {

    /**#############################################################################################
     * Controls
     *############################################################################################*/
    private Spinner detailAccountTypeSpinner;
    private TextInputEditText detailAccountNameTextInputEditText;
    private TextInputEditText detailAccountDescriptionTextInputEditText;
    private TextInputEditText detailAccountValueTextInputEditText;
    private Switch detailAccountRecordToValueSwitch;
    private Button changeAccountButton;

    /**#############################################################################################
     * Properties
     *############################################################################################*/
    private User currentUser;
    private FirebaseManager firebaseManager;
    private boolean isRecordToValueEnabled;
    private String accountTypeSelectedValue;
    private List<Account> accounts;
    private Account selectedAccount;
    private int accountPosition = 0;
    private int accountTypePosition = 0;
    private List<AccountTypeValueHelper> accountTypeValueHelpers;

    /**#############################################################################################
     * Lifecycles
     *############################################################################################*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_detail);
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
        startActivity(new Intent(getApplicationContext(),AccountsActivity.class));
    }

    /**#############################################################################################
     * Controls & Events
     *############################################################################################*/
    private void _initializeControlEvents(){
        this.changeAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                _changeAccount();
            }
        });
        this.detailAccountRecordToValueSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                isRecordToValueEnabled = isChecked;
            }
        });
        this.detailAccountTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                AccountTypeValueHelper accountTypeValueHelper = (AccountTypeValueHelper) parent.getSelectedItem();
                accountTypeSelectedValue = accountTypeValueHelper.getId();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                accountTypeSelectedValue = "MAIN";
            }
        });
    }

    private void _initializeControls(){
        this.detailAccountTypeSpinner = findViewById(R.id.detailAccountTypeSpinner);
        this.detailAccountNameTextInputEditText = findViewById(R.id.detailAccountNameTextInputEditText);
        this.detailAccountDescriptionTextInputEditText = findViewById(R.id.detailAccountDescriptionTextInputEditText);
        this.detailAccountValueTextInputEditText = findViewById(R.id.detailAccountValueTextInputEditText);
        this.detailAccountRecordToValueSwitch = findViewById(R.id.detailAccountRecordToValueSwitch);
        this.changeAccountButton = findViewById(R.id.changeAccountButton);
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
        this.accountTypeValueHelpers = this._getAccountTypeList();
        this._loadAccountTypeSpinner();

        Bundle extras = getIntent().getExtras();
        if(extras != null){
            this.accountPosition = extras.getInt("POSITION");
            this.selectedAccount = this.accounts.get(this.accountPosition);
            this._setAccountTypePosition();
            this._loadSelectedAccount(selectedAccount);
        }
        else startActivity(new Intent(getApplicationContext(),AccountsActivity.class));
    }

    private void _changeAccount(){
        Account account = this._getChangedAccount(this.selectedAccount);
        if(this._checkAccount(account)){
            this.accounts.set(this.accountPosition,account);
            this.currentUser.setAccounts(this.accounts);
            if(this.firebaseManager.saveObject(this.currentUser)){
                Message.show(getApplicationContext(),"Das Konto wurde geändert", Message.Mode.SUCCESS);
                startActivity(new Intent(getApplicationContext(),AccountsActivity.class));
            }
        }
    }

    private Account _getChangedAccount(Account account){
        account.setAccountName(this.detailAccountNameTextInputEditText.getText().toString());
        account.setAccountDescription(this.detailAccountDescriptionTextInputEditText.getText().toString());
        account.setAccountValue(Double.parseDouble(detailAccountValueTextInputEditText.getText().toString()));
        account.setAccountType(this.accountTypeSelectedValue);
        account.setAccountRecordToValue(this.isRecordToValueEnabled);
        return account;
    }

    private void _loadSelectedAccount(Account account){
        this.detailAccountNameTextInputEditText.setText(account.getAccountName());
        this.detailAccountDescriptionTextInputEditText.setText(account.getAccountDescription());
        this.detailAccountTypeSpinner.setSelection(this.accountTypePosition);
        this.detailAccountRecordToValueSwitch.setChecked(account.isAccountRecordToValue());
        this.detailAccountValueTextInputEditText.setText(account.getAccountValue().toString());
    }

    private boolean _checkAccount(Account account){
        return true;
    }

    private void _loadAccountTypeSpinner(){
        ArrayAdapter<AccountTypeValueHelper> adapter =
                new ArrayAdapter<>(getApplicationContext(),android.R.layout.simple_spinner_dropdown_item,this.accountTypeValueHelpers);
        this.detailAccountTypeSpinner.setAdapter(adapter);
    }

    private List<AccountTypeValueHelper> _getAccountTypeList(){
        ArrayList<AccountTypeValueHelper> accountTypeValueHelpers = new ArrayList<>();
        accountTypeValueHelpers.add(new AccountTypeValueHelper(AccountTypeEnum.MAIN.toString(),"Hauptkonto"));
        accountTypeValueHelpers.add(new AccountTypeValueHelper(AccountTypeEnum.ONLINE.toString(),"Online Konto"));
        accountTypeValueHelpers.add(new AccountTypeValueHelper(AccountTypeEnum.BET.toString(),"Wettkonto"));
        accountTypeValueHelpers.add(new AccountTypeValueHelper(AccountTypeEnum.SAVING.toString(),"Sparkonto"));
        accountTypeValueHelpers.add(new AccountTypeValueHelper(AccountTypeEnum.DAYMONEY.toString(),"Tagesgeld"));
        accountTypeValueHelpers.add(new AccountTypeValueHelper(AccountTypeEnum.BAR.toString(),"Bar"));
        return accountTypeValueHelpers;
    }

    private void _setAccountTypePosition(){
        for(int i = 0;i<this.accountTypeValueHelpers.size();i++){
            if (this.accountTypeValueHelpers.get(i).getId().equals(this.selectedAccount.getAccountType()))this.accountTypePosition = i;
        }
    }
}
