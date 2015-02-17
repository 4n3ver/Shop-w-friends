package com.howdoicomputer.android.shoppingwithfriends.model;

/**
 * Created by Yoel Ivan on 2/15/2015.
 */
public interface FriendListModel {
    public void fetchAccountInfo(String userName, AccountStateListener listener);

    public void updateAccount(Account account);
}
