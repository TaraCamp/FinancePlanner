/**#################################################################################################
 * Author: Wladimir Tarasov
 * Date: 12.03.2019
 *################################################################################################*/
package com.taracamp.financeplanner.Adapters;

import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.taracamp.financeplanner.Core.Message;
import com.taracamp.financeplanner.Models.Transaction;
import com.taracamp.financeplanner.R;

import java.text.SimpleDateFormat;
import java.util.Date;
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
        TextView transactionValueCardTextView = holder.transactionValueCardTextView;

        transactionValueCardTextView.setTypeface(null, Typeface.BOLD);

        if (transaction.getTransactionName()!=null)transactionNameCardTextView.setText(transaction.getTransactionName());
        if (transaction.getTransactionDate()!=null)transactionDescriptionCardTextView.setText(this._getGermanDateFormat(transaction.getTransactionDate()));

        if (transaction.getTransactionValue()!=null){
            this._setTransactionValueTextViewColor(transactionValueCardTextView,transaction.getTransactionType());
            String transactionValueText = transactionValueCardTextView.getText() + transaction.getTransactionValue().toString() + "\u20ac";
            transactionValueCardTextView.setText(transactionValueText);
        }

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

    private String _getGermanDateFormat(Date date){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd.MM.yyyy");
        return simpleDateFormat.format(date);
    }

    private void _setTransactionValueTextViewColor(TextView control, String type){
        control.setText("");
        if (type.equals("POSITIVE")){
            control.setTextColor(Color.rgb(0,200,0));
            control.setText("+");
        }else if (type.equals("NEGATIVE")){
            control.setTextColor(Color.rgb(200,0,0));
            control.setText("-");
        }
    }

    /**#############################################################################################
     * ViewHolder
     *############################################################################################*/
    public class ViewHolder extends RecyclerView.ViewHolder
    {
        private ImageView transactionCardImageView;
        private TextView transactionNameCardTextView;
        private TextView transactionDescriptionCardTextView;
        private TextView transactionValueCardTextView;

        public ViewHolder(View itemView)
        {
            super(itemView);

            this.transactionCardImageView = itemView.findViewById(R.id.transactionCardImageView);
            this.transactionNameCardTextView = itemView.findViewById(R.id.transactionNameCardTextView);
            this.transactionDescriptionCardTextView = itemView.findViewById(R.id.transactionDescriptionCardTextView);
            this.transactionValueCardTextView = itemView.findViewById(R.id.transactionValueCardTextView);
        }
    }
}
