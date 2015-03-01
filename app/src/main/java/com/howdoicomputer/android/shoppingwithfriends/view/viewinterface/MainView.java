package com.howdoicomputer.android.shoppingwithfriends.view.viewinterface;

/**
 * {@link MainView} provides a way for presenter that handle the main app and the GUI to interact.
 *
 * @author Yoel Ivan
 * @version %I%, %G%
 */
public interface MainView {
    public AppStateListener getAppStateListener();

    public ViewObjectUtil getObjectUtil();

    public void refreshView();


}
