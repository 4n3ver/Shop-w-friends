package com.howdoicomputer.android.shoppingwithfriends.view.viewinterface;

import com.howdoicomputer.android.shoppingwithfriends.model.pojo.Account;

/**
 * {@link MainView} provides a way for presenter that handle the main app and the GUI to interact.
 *
 * @author Yoel Ivan
 * @version %I%, %G%
 */
public interface MainView {

    /**
     * This method is to be called by presenter on logged out.
     */
    public void onLoggedOut();

    public void onAccountChanged(Account changedAccount);
}
