/**#################################################################################################
 * Author: Wladimir Tarasov
 * Date: 12.03.2019
 *################################################################################################*/
package com.taracamp.financeplanner;

import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.taracamp.financeplanner.Adapters.AddTransactionPagerAdapter;
import com.taracamp.financeplanner.Core.FirebaseManager;
import com.taracamp.financeplanner.Models.User;

public class AddTransactionActivity extends AppCompatActivity {

    /**#############################################################################################
     * Constants
     *############################################################################################*/
    // Tab index text size.
    private final static int TEXTSIZE_ACTIVE = 16;
    private final static int TEXTSIZE_DEACTIVE = 14;

    // Tab index
    private final static int TAB_OBJECT_NUMBER_ONE = 0;
    private final static int TAB_OBJECT_NUMBER_TWO = 1;
    private final static int TAB_OBJECT_NUMBER_THREE = 2;

    private final static int OFFSCREEN_PAGE_LIMIT = 2;

    /**#############################################################################################
     * Controls
     *############################################################################################*/
    private TextView addTransactionPositiveTextView;
    private TextView addTransactionNegativeTextView;
    private TextView addTransactionNeutralTextView;

    private ViewPager addTransactionViewpager;
    private AddTransactionPagerAdapter addTransactionPagerAdapter;

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
        startActivity(new Intent(getApplicationContext(),TransactionsActivity.class));
    }

    /**#############################################################################################
     * Control & Events
     *############################################################################################*/
    private void _initializeControlEvents(){
        this.addTransactionPositiveTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addTransactionViewpager.setCurrentItem(TAB_OBJECT_NUMBER_ONE);
            }
        });
        this.addTransactionNegativeTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addTransactionViewpager.setCurrentItem(TAB_OBJECT_NUMBER_TWO);
            }
        });
        this.addTransactionNeutralTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addTransactionViewpager.setCurrentItem(TAB_OBJECT_NUMBER_THREE);
            }
        });
        this.addTransactionViewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener(){
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}

            @Override
            public void onPageSelected(int position)
            {
                _changeTabs(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {}
        });
    }

    private void _initializeControls(){
        this.setContentView(R.layout.activity_add_transaction);
        this.addTransactionPositiveTextView = findViewById(R.id.addTransactionPositiveTextView);
        this.addTransactionNegativeTextView = findViewById(R.id.addTransactionNegativeTextView);
        this.addTransactionNeutralTextView = findViewById(R.id.addTransactionNeutralTextView);
        this.addTransactionViewpager = findViewById(R.id.addTransactionViewpager);
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
                    if (currentUser!=null) _loadData();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {}
        });
    }

    /**#############################################################################################
     * Private Methoden
     *############################################################################################*/
    private void _loadData(){
        this._initializeControls();
        this._setupPagerView();
        this._initializeControlEvents();
    }

    private void _setupPagerView(){
        this.addTransactionPagerAdapter = new AddTransactionPagerAdapter(getSupportFragmentManager(),this.firebaseManager,this.currentUser);
        this.addTransactionViewpager.setOffscreenPageLimit(OFFSCREEN_PAGE_LIMIT);
        this.addTransactionViewpager.setAdapter(this.addTransactionPagerAdapter);
    }

    private void _changeTabs(int position){
        switch (position)
        {
            case 0 :
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                {
                    this.addTransactionPositiveTextView.setTextColor(getColor(R.color.textTabBright));
                    this.addTransactionPositiveTextView.setTextSize(TEXTSIZE_ACTIVE);

                    this.addTransactionNegativeTextView.setTextColor(getColor(R.color.textTabLight));
                    this.addTransactionNegativeTextView.setTextSize(TEXTSIZE_DEACTIVE);

                    this.addTransactionNeutralTextView.setTextColor(getColor(R.color.textTabLight));
                    this.addTransactionNeutralTextView.setTextSize(TEXTSIZE_DEACTIVE);
                }
                break;
            case 1 :
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                {
                    this.addTransactionPositiveTextView.setTextColor(getColor(R.color.textTabLight));
                    this.addTransactionPositiveTextView.setTextSize(TEXTSIZE_DEACTIVE);

                    this.addTransactionNegativeTextView.setTextColor(getColor(R.color.textTabBright));
                    this.addTransactionNegativeTextView.setTextSize(TEXTSIZE_ACTIVE);

                    this.addTransactionNeutralTextView.setTextColor(getColor(R.color.textTabLight));
                    this.addTransactionNeutralTextView.setTextSize(TEXTSIZE_DEACTIVE);
                }
                break;
            case 2 :
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                {
                    this.addTransactionPositiveTextView.setTextColor(getColor(R.color.textTabLight));
                    this.addTransactionPositiveTextView.setTextSize(TEXTSIZE_DEACTIVE);

                    this.addTransactionNegativeTextView.setTextColor(getColor(R.color.textTabLight));
                    this.addTransactionNegativeTextView.setTextSize(TEXTSIZE_DEACTIVE);

                    this.addTransactionNeutralTextView.setTextColor(getColor(R.color.textTabBright));
                    this.addTransactionNeutralTextView.setTextSize(TEXTSIZE_ACTIVE);
                }
                break;
            default:
                break;
        }
    }
}
