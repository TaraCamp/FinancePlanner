package com.taracamp.financeplanner.Adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.taracamp.financeplanner.Core.FirebaseManager;
import com.taracamp.financeplanner.Fragmente.AddTransactionPositiveFragment;

public class AddTransactionPagerAdapter extends FragmentPagerAdapter {
    private FirebaseManager firebaseManager;

    public AddTransactionPagerAdapter(FragmentManager fragmentManager, FirebaseManager firebaseManager)
    {
        super(fragmentManager);
        this.firebaseManager = firebaseManager;
    }

    @Override
    public Fragment getItem(int position)
    {
        switch(position)
        {
            case 0 : return AddTransactionPositiveFragment.newInstance(firebaseManager);
            case 1 : return AddTransactionPositiveFragment.newInstance(firebaseManager);
            case 2 : return AddTransactionPositiveFragment.newInstance(firebaseManager);
            default: return null;
        }
    }

    @Override
    public int getCount()
    {
        return 3;
    }
}
