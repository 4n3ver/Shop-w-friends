package com.howdoicomputer.android.shoppingwithfriends.view;

import com.howdoicomputer.android.shoppingwithfriends.model.Account;

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
    public void onAuthenticated(Account acc);

    /**
     * Show error dialog to user.
     *
     * @param message error message
     */
    public void showErrorDialog(String message);

    /**
     * Show progress dialog to user
     *
     * @param title   {@link String} of title to be shown to user
     * @param message {@link String} of message to be shown to user
     */
    public void showProgressDialog(String title, String message);

    /**
     * Hide progress dialog if any
     */
    public void hideProgressDialog();

    public void loginUserNameError(String message);

    public void loginPasswordError(String message);

    public void registerUserNameError(String message);

    public void registerEmailAddressError(String message);

    public void registerPasswordError(String message);

    public void registerConfirmPasswordError(String message);
}
