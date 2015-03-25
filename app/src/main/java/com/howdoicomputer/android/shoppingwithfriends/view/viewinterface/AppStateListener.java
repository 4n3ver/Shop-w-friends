package com.howdoicomputer.android.shoppingwithfriends.view.viewinterface;

import android.location.Location;

import com.howdoicomputer.android.shoppingwithfriends.model.pojo.Account;

/**
 * {@link AppStateListener} is an interface to communicate changes made in the fragment back to
 * containing activity.
 *
 * @author Yoel Ivan (yivan3@gatech.edu)
 * @version 1.0
 */
public interface AppStateListener {

    /**
     * Pass {@link Account} object which states has been changed.
     *
     * @param changedAccount {@link Account} object which state changed
     */
    public void onAccountChanged(Account changedAccount);

    /**
     * This method is to be called when user logging out.
     */
    public void onLoggedOut();

    public Account getLatestAccount();

    public Location getLocation();

    public String getAddress();

}
