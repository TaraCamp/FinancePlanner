/**#################################################################################################
 * Author: Wladimir Tarasov
 * Date: 12.03.2019
 *################################################################################################*/
package com.taracamp.financeplanner.Adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.taracamp.financeplanner.Core.Message;
import com.taracamp.financeplanner.Models.Transaction;
import com.taracamp.financeplanner.R;

import java.util.List;

public class TransactionAdapter extends RecyclerView.Adapter<TransactionAdapter.ViewHolder> {

    private List<Transaction> mDataset;

    public TransactionAdapter(List<Transaction> myDataset) {
        this.mDataset = myDataset;
    }

    @Override
    public TransactionAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.transaction_card,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Transaction transaction = this.mDataset.get(position);

        //ImageView transactionCardImageView = holder.transactionCardImageView;
        TextView transactionNameCardTextView = holder.transactionNameCardTextView;
        TextView transactionDescriptionCardTextView = holder.transactionDescriptionCardTextView;

        transactionNameCardTextView.setText(transaction.getName());
        transactionDescriptionCardTextView.setText(transaction.getValue().toString());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Message.show(v.getContext(),"Transaktion Klick", Message.Mode.SUCCESS);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    /**#############################################################################################
     * ViewHolder
     *############################################################################################*/
    public class ViewHolder extends RecyclerView.ViewHolder
    {
        private ImageView transactionCardImageView;
        private TextView transactionNameCardTextView;
        private TextView transactionDescriptionCardTextView;

        public ViewHolder(View itemView)
        {
            super(itemView);

            this.transactionCardImageView = itemView.findViewById(R.id.transactionCardImageView);
            this.transactionNameCardTextView = itemView.findViewById(R.id.transactionNameCardTextView);
            this.transactionDescriptionCardTextView = itemView.findViewById(R.id.transactionDescriptionCardTextView);
        }
    }
}
