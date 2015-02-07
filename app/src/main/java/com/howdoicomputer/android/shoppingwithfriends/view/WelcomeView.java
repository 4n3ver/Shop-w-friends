package com.howdoicomputer.android.shoppingwithfriends.view;

/**
 * {@link WelcomeView} provides a way for presenter that handle login and the GUI to interact.
 *
 * @author Yoel Ivan
 * @version %I%, %G%
 */
public interface WelcomeView {

    /**
     * This method is to be called by presenter on authenticated.
     */
    public void onAuthenticated();
}
