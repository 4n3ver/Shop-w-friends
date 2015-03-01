package com.howdoicomputer.android.shoppingwithfriends.model.databaseinterface;

import com.howdoicomputer.android.shoppingwithfriends.model.database.DatabaseError;
import com.howdoicomputer.android.shoppingwithfriends.model.pojo.Account;

/**
 * Created by Yoel Ivan on 2/28/2015.
 */
public interface AccountStateListener {

    void onError(DatabaseError databaseError);

    void onAccountChanged(Account account);
}
