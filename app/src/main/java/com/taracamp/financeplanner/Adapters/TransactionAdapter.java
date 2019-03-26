/**#################################################################################################
 * Author: Wladimir Tarasov
 * Date: 12.03.2019
 *################################################################################################*/
package com.taracamp.financeplanner.Adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.taracamp.financeplanner.MainActivity;
import com.taracamp.financeplanner.Models.Transaction;
import com.taracamp.financeplanner.R;
import com.taracamp.financeplanner.TransactionDetailActivity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class TransactionAdapter extends RecyclerView.Adapter<TransactionAdapter.ViewHolder> {

    /**#############################################################################################
     * Properties
     *############################################################################################*/
    private List<Transaction> transactions;
    private Context parentContext;
    private MainActivity parentActivity;

    /**#############################################################################################
     * Constructor
     *############################################################################################*/
    public TransactionAdapter(Context context,List<Transaction> myDataset) {
        this.parentContext = context;
        this.transactions = myDataset;
    }

    /**#############################################################################################
     * Adapter Methods
     *############################################################################################*/
    @Override
    public TransactionAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.transaction_card,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        Transaction transaction = this.transactions.get(position);

        //ImageView transactionCardImageView = holder.transactionCardImageView;
        TextView transactionNameCardTextView = holder.transactionNameCardTextView;
        TextView transactionDescriptionCardTextView = holder.transactionDescriptionCardTextView;
        TextView transactionValueCardTextView = holder.transactionValueCardTextView;

        transactionValueCardTextView.setTypeface(null, Typeface.BOLD);

        if (transaction.getTransactionName()!=null)transactionNameCardTextView.setText(transaction.getTransactionName());

        String transactionDate = "";
        if (transaction.getTransactionDate()!=null)transactionDate = _getGermanDateFormat(transaction.getTransactionDate());

        transactionDescriptionCardTextView.setText("(" + transactionDate + ") " + transaction.getTransactionDescription());

        if (transaction.getTransactionValue()!=null){
            this._setTransactionValueTextViewColor(transactionValueCardTextView,transaction.getTransactionType());
            transactionValueCardTextView.setText(transactionValueCardTextView.getText() + transaction.getTransactionValue().toString() + "\u20ac");
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(parentContext.getApplicationContext(), TransactionDetailActivity.class);
                intent.putExtra("POSITION",position);
                parentContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return transactions.size();
    }

    /**#############################################################################################
     * Private Methods
     *############################################################################################*/
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
        private CircleImageView transactionCardImageView;
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
