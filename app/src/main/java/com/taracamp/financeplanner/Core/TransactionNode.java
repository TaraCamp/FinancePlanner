package com.taracamp.financeplanner.Core;

import com.google.firebase.database.DatabaseReference;
import com.taracamp.financeplanner.Models.Transaction;

public class TransactionNode implements Node {
    private DatabaseReference reference;

    public TransactionNode(DatabaseReference ref)
    {
        reference = ref;
    }

    @Override
    public boolean save(Object object) {
        Transaction transaction = (Transaction) object;
        reference.child(transaction.getToken()).setValue(transaction);
        return true;
    }

    @Override
    public boolean remove(Object object) {
        return false;
    }
}
