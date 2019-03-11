package com.taracamp.financeplanner;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.taracamp.financeplanner.Models.Transaction;

import java.util.List;

public class TransactionAdapter extends RecyclerView.Adapter<TransactionAdapter.ViewHolder> {

    private List<Transaction> mDataset;


    // Provide a suitable constructor (depends on the kind of dataset)
    public TransactionAdapter(List<Transaction> myDataset) {
        mDataset = myDataset;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public TransactionAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                              int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.transaction_card,parent,false);

        return new ViewHolder(view);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // - get element from your dataset at this position


    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        private View view;

        public ViewHolder(View itemView)
        {
            super(itemView);

            view = itemView;
        }
    }

}
