package com.howdoicomputer.android.shoppingwithfriends.view.viewinterface;

import com.howdoicomputer.android.shoppingwithfriends.model.pojo.Account;

/**
 * Created by Yoel Ivan on 2/24/2015.
 */
public interface AppStateListener {
    void onAccountChanged(Account changedAccount);

    void onLoggedOut();
}
