package com.howdoicomputer.android.shoppingwithfriends.model.databaseinterface;

import com.howdoicomputer.android.shoppingwithfriends.model.database.DatabaseError;
import com.howdoicomputer.android.shoppingwithfriends.model.pojo.Account;

/**
 * {@link FetchAccountResultListener} provides a way for the presenter to find an account
 */
public interface FetchAccountResultListener {
    /**
     * This method will be called by the model upon account found.
     */
    void onFound(Account account);

    /**
     * This method will be called by the model if account not exist.
     */
    void onNotFound();

    /**
     * This method will be called by the model on error.
     *
     * @param error exception thrown upon error
     */
    void onError(DatabaseError error);
}
