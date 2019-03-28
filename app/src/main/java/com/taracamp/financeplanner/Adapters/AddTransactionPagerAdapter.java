package com.taracamp.financeplanner.Adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.taracamp.financeplanner.Core.FirebaseManager;
import com.taracamp.financeplanner.Fragmente.AddTransactionNegativeFragment;
import com.taracamp.financeplanner.Fragmente.AddTransactionPositiveFragment;
import com.taracamp.financeplanner.Models.User;

public class AddTransactionPagerAdapter extends FragmentPagerAdapter {
    private FirebaseManager firebaseManager;
    private User currentUser;

    public AddTransactionPagerAdapter(FragmentManager fragmentManager, FirebaseManager firebaseManager,User currentUser)
    {
        super(fragmentManager);
        this.firebaseManager = firebaseManager;
        this.currentUser = currentUser;
    }

    @Override
    public Fragment getItem(int position)
    {
        switch(position)
        {
            case 0 : return AddTransactionPositiveFragment.newInstance(firebaseManager,currentUser);
            case 1 : return AddTransactionNegativeFragment.newInstance(firebaseManager,currentUser);
            case 2 : return AddTransactionPositiveFragment.newInstance(firebaseManager,currentUser);
            default: return null;
        }
    }

    @Override
    public int getCount()
    {
        return 3;
    }
}
