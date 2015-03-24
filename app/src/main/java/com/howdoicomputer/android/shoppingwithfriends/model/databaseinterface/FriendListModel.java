package com.howdoicomputer.android.shoppingwithfriends.model.databaseinterface;

import com.howdoicomputer.android.shoppingwithfriends.model.pojo.FriendList;

/**
 * Created by Yoel Ivan on 2/15/2015.
 */
public interface FriendListModel extends MainModel {

    /**
     * Fetch friend'saccount information
     *
     * @param friendList the friendlist
     * @param listener   the listener
     */
    public void fetchFriendAccountInfo(FriendList friendList, AccountStateListener listener);

}
