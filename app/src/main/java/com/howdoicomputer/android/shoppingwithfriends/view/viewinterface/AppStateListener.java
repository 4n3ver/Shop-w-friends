package com.howdoicomputer.android.shoppingwithfriends.view.viewinterface;

import com.howdoicomputer.android.shoppingwithfriends.model.pojo.Account;

/**
 * {@link AppStateListener} is an interface to communicate changes made in the fragment back to
 * containing activity and accessing common information.
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

    /**
     * Get the most recent updated {@link Account} object.
     *
     * @return latest {@link Account} object.
     */
    public Account getLatestAccount();

    /**
     * Get latitude and longitude based on device GPS.
     *
     * @return array of double of length 2, index 0 represent latitude and index 1 represent
     * longitude
     */
    public double[] getLocation();

    /**
     * Get the address according to current location from the device GPS.
     *
     * @return {@link String} representation of the address.
     */
    public String getAddress();

    public void randomMethod(double latitude, double longitude);

}
