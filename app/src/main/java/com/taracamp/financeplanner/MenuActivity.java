/**#################################################################################################
 * Author: Wladimir Tarasov
 * Date: 25.03.2019
 *################################################################################################*/
package com.taracamp.financeplanner;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

public class MenuActivity extends AppCompatActivity {

    /**#############################################################################################
     * Controls
     *############################################################################################*/
    private RelativeLayout transactionsRelativeLayout;
    private RelativeLayout accountsRelativeLayout;
    private RelativeLayout calendarRelativeLayout;
    private RelativeLayout userRelativeLayout;
    private RelativeLayout logoutRelativeLayout;

    /**#############################################################################################
     * Properties
     *############################################################################################*/

    /**#############################################################################################
     * Lifecycles
     *############################################################################################*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        this._initializeControls();
    }

    /**#############################################################################################
     * Activity Events
     *############################################################################################*/
    @Override
    public void onBackPressed() {
        //startActivity(new Intent(getApplicationContext(),TransactionsActivity.class));
    }

    /**#############################################################################################
     * Control & Events
     *############################################################################################*/
    private void _initializeControls(){
        this.transactionsRelativeLayout = findViewById(R.id.transactionsRelativeLayout);
        this.accountsRelativeLayout = findViewById(R.id.accountsRelativeLayout);
        this.calendarRelativeLayout = findViewById(R.id.calendarRelativeLayout);
        this.userRelativeLayout = findViewById(R.id.userRelativeLayout);
        this.logoutRelativeLayout = findViewById(R.id.logoutRelativeLayout);
        this._initializeControlEvents();
    }

    private void _initializeControlEvents(){
        this.transactionsRelativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),TransactionsActivity.class));
            }
        });
        this.accountsRelativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),AccountsActivity.class));
            }
        });
        this.calendarRelativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        this.userRelativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        this.logoutRelativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    /**#############################################################################################
     * Firebase Auth
     *############################################################################################*/

    /**#############################################################################################
     * Private Methoden
     *############################################################################################*/
}
