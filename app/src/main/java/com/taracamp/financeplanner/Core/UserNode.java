package com.taracamp.financeplanner.Core;

import com.google.firebase.database.DatabaseReference;
import com.taracamp.financeplanner.Models.User;

public class UserNode implements Node {
    private DatabaseReference reference;
    public UserNode(DatabaseReference ref)
    {
        reference = ref;
    }
    @Override
    public boolean save(Object obj) {
        User user = (User) obj;
        reference.child(user.getToken()).setValue(user);
        return true;
    }

    @Override
    public boolean remove(Object object) {
        return false;
    }
}
