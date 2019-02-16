package com.taracamp.financeplanner.Models;

import com.google.firebase.auth.FirebaseAuth;

public class FirebaseManager {
    public FirebaseAuth mAuth = null;
    public FirebaseAuth.AuthStateListener mAuthListener = null;

    public void onStart()
    {
        mAuth.addAuthStateListener(mAuthListener);
    }

    public void onStop()
    {
        if (mAuthListener != null)
        {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }
}
