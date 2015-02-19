package com.howdoicomputer.android.shoppingwithfriends.model.database;

import com.howdoicomputer.android.shoppingwithfriends.model.Account;

/**
 * {@link MainModel} provides a way for presenter that handle the main app and the data/model to
 * interact.
 *
 * @author Yoel Ivan
 * @version %I%, %G%
 */
public interface MainModel {

    /**
     * Send {@link Account} for <code>userName</code> and send it to <code>listener</code>.
     *
     * @param userName user name of the {@link Account} to be fetched
     * @param listener target listener where the {@link Account} need to be send
     */
    public void fetchAccountInfo(String userName, AccountStateListener listener);

    /**
     * @param account
     */
    public void updateAccount(Account account);

    /**
     * Ends current session on this client.
     */
    public void logout();
}
