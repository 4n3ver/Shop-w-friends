package com.howdoicomputer.android.shoppingwithfriends.model;

/**
 * {@link MainModel} provides a way for presenter that handle the main app and the data/model to
 * interact.
 *
 * @author Yoel Ivan
 * @version %I%, %G%
 */
public interface MainModel {

    /**
     * Ends current session on this client.
     */
    public void logout();
}
