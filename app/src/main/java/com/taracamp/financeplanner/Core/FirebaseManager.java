package com.taracamp.financeplanner.Core;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
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
        if (object instanceof User){
            UserNode node = new UserNode(this.database.getReference("users").getRef());
            return node.save(object);
        }
        return true;
    }
}
