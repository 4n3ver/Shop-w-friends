package com.howdoicomputer.android.shoppingwithfriends.model.database;

import com.howdoicomputer.android.shoppingwithfriends.model.Account;

/**
 * {@link LoginModel} provides a way for presenter that handle login and the data/model to interact.
 *
 * @author Yoel Ivan
 * @version %I%, %G%
 */
public interface LoginModel {

    /**
     * Check whether a user has been authenticated on this client.
     *
     * @param listener object contained information on what to do when certain event triggered
     */
    public void checkAuthentication(final AuthenticationStateListener listener);

    /**
     * Attempt to log in into the system using passed arguments.
     *
     * @param userName {@link String} representation of the user name
     * @param password {@link String} representation of the password
     * @param listener object contained information on what to do when certain event triggered
     */
    public void login(String userName, String password, final AuthenticationStateListener listener);

    /**
     * Attempt to create new account.
     *
     * @param userName {@link String} representation of the user name
     * @param email    {@link String} representation of the email
     * @param password {@link String} representation of the password
     * @param listener object contained information on what to do when certain event triggered
     * @return <code>Account</code> if the login succeeded or <code>null</code> otherwise
     */
    public void register(String name, String userName, String email, String password,
            final RegisterStateListener listener);

    /**
     * {@link AuthenticationStateListener} provides a way for the presenter to be notified on
     * authentication state change.
     */
    public interface AuthenticationStateListener {

        /**
         * This method will be called by the model upon authenticated.
         */
        public void onAuthenticated(Account acc);

        /**
         * This method will be called by the model upon authentication error.
         *
         * @param error exception thrown upon error
         */
        public void onError(DatabaseError error);
    }

    /**
     * {@link RegisterStateListener} provides a way for the presenter to be notified on
     * register state change.
     */
    public interface RegisterStateListener {

        /**
         * This method will be called by the model upon registration success.
         */
        public void onSuccess();

        /**
         * This method will be called by the model upon registration error.
         *
         * @param error exception thrown upon error
         */
        public void onError(DatabaseError error);
    }

}
