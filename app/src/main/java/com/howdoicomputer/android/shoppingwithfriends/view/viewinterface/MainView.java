package com.howdoicomputer.android.shoppingwithfriends.view.viewinterface;

import com.howdoicomputer.android.shoppingwithfriends.model.pojo.Account;

/**
 * {@link MainView} provides a way for presenter that handle the main app and the GUI to interact.
 *
 * @author Yoel Ivan
 * @version %I%, %G%
 */
public interface MainView {
    public AppStateListener getAppStateListener();

    public ViewObjectUtil getUiUtil();

    public void refreshView();

    public void updateAccount(Account acc);

}
