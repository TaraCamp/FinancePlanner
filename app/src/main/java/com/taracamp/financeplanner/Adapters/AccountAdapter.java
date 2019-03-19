/**#################################################################################################
 * Author: Wladimir Tarasov
 * Date: 19.03.2019
 *################################################################################################*/
package com.taracamp.financeplanner.Adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.taracamp.financeplanner.AccountDetailActivity;
import com.taracamp.financeplanner.AccountsActivity;
import com.taracamp.financeplanner.Models.Account;
import com.taracamp.financeplanner.R;

import java.util.List;

public class AccountAdapter extends RecyclerView.Adapter<AccountAdapter.ViewHolder> {

    /**#############################################################################################
     * Properties
     *############################################################################################*/
    private List<Account> accounts;
    private Context parentContext;
    private AccountsActivity parentActivity;

    /**#############################################################################################
     * Constructor
     *############################################################################################*/
    public AccountAdapter(Context context,List<Account> myDataset) {
        this.parentContext = context;
        this.accounts = myDataset;
    }

    /**#############################################################################################
     * Adapter Methods
     *############################################################################################*/
    @Override
    public AccountAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.account_card,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        Account account = this.accounts.get(position);

        //ImageView accountCardImageView = holder.accountCardImageView;
        TextView accountNameCardTextView = holder.accountNameCardTextView;
        TextView accountDescriptionCardTextView = holder.accountDescriptionCardTextView;
        TextView accountValueCardTextView = holder.accountValueCardTextView;

        accountValueCardTextView.setTypeface(null, Typeface.BOLD);

        if (account.getAccountName()!=null)accountNameCardTextView.setText(account.getAccountName());
        if (account.getAccountDescription()!=null)accountDescriptionCardTextView.setText(account.getAccountDescription());

        if (account.getAccountValue()!=null)accountValueCardTextView.setText(account.getAccountValue().toString());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(parentContext.getApplicationContext(), AccountDetailActivity.class);
                intent.putExtra("POSITION",position);
                parentContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return accounts.size();
    }

    /**#############################################################################################
     * ViewHolder
     *############################################################################################*/
    public class ViewHolder extends RecyclerView.ViewHolder
    {
        private ImageView accountCardImageView;
        private TextView accountNameCardTextView;
        private TextView accountDescriptionCardTextView;
        private TextView accountValueCardTextView;

        public ViewHolder(View itemView)
        {
            super(itemView);

            this.accountCardImageView = itemView.findViewById(R.id.accountCardImageView);
            this.accountNameCardTextView = itemView.findViewById(R.id.accountNameCardTextView);
            this.accountDescriptionCardTextView = itemView.findViewById(R.id.accountDescriptionCardTextView);
            this.accountValueCardTextView = itemView.findViewById(R.id.accountValueCardTextView);
        }
    }
}
