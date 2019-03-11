package com.taracamp.financeplanner.Core;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.taracamp.financeplanner.Models.Transaction;
import com.taracamp.financeplanner.Models.User;

public class FirebaseManager {
    public FirebaseAuth mAuth;
    public FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseDatabase database;

    public FirebaseManager(){
        this.database = FirebaseDatabase.getInstance();
    }

    public void onStart(){
        mAuth.addAuthStateListener(mAuthListener);
    }

    public void onStop(){
        if (mAuthListener != null) mAuth.removeAuthStateListener(mAuthListener);
    }

    public boolean saveObject(Object object){
        Node node;
        if (object instanceof User)node = new UserNode(this.database.getReference("users").getRef());
        else if (object instanceof Transaction)node = new TransactionNode(this.database.getReference("transactions").getRef());
        else return false;
        return node.save(object);
    }

    public DatabaseReference getRootReference()
    {
        return this.database.getReference();
    }

    public DatabaseReference getUserReference(String token){
        return this.getRootReference().child("users").child(token).getRef();
    }

    public DatabaseReference getTransactionsReference(){
        //// TODO: 11.03.2019 Muss noch implementiert werden
        return this.getRootReference();
    }
}
