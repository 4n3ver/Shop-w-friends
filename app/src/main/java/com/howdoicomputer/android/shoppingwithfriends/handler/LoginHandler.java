package com.howdoicomputer.android.shoppingwithfriends.handler;

import com.firebase.client.FirebaseException;
import com.howdoicomputer.android.shoppingwithfriends.model.Database;
import com.howdoicomputer.android.shoppingwithfriends.model.LoginModel;
import com.howdoicomputer.android.shoppingwithfriends.view.WelcomeView;

/**
 * {@link LoginHandler} handles event from login view GUI and process related data from the model.
 *
 * @author Yoel Ivan
 * @version %I%, %G%
 */
public class LoginHandler {
    private static LoginModel db;
    private WelcomeView view;

    /**
     * Construct {@link LoginHandler} object.
     *
     * @param view reference to the implementing GUI
     */
    public LoginHandler(WelcomeView view) {
        db = new Database();
        this.view = view;
    }

    /**
     * Check whether a user has been authenticated on this client.
     */
    public void checkAuthentication() {
        db.checkAuthentication(new LoginModel.AuthenticationStateListener() {
            @Override
            public void onAuthenticated() {
                view.onAuthenticated();
            }
        });
    }

    /**
     * Authenticate with user provided username and password.
     *
     * @param usrName  {@link String} representation of username
     * @param password {@link String} representation of password
     */
    public void login(String usrName, String password) {
        try {
            db.login(usrName, password, new LoginModel.AuthenticationStateListener() {
                @Override
                public void onAuthenticated() {
                    view.onAuthenticated();
                }
            });
        } catch (FirebaseException e) {
            throw e;
        }
    }

    /**
     * Register new account with provided information.
     *
     * @param usrName  user provided <code>usrName</code>
     * @param email    user provided <code>email</code>
     * @param pass     user provided <code>pass</code>
     * @param passConf user provided <code>passConf</code>
     */
    public void register(final String usrName, String email, final String pass, String passConf) {
        if (db.userIsRegistered(usrName)) { // username taken
            throw new IllegalArgumentException("Username has been taken");
        } else if (!pass.equals(passConf)) { // check if both password fields are identical
            throw new IllegalArgumentException("Password does not match");
        } else {
            try {
                db.register(usrName, email, pass, new LoginModel.RegisterStateListener() {
                    @Override
                    public void onSuccess() {
                        login(usrName, pass);
                    }
                });
            } catch (FirebaseException e) {
                throw e;
            }
        }
    }
}
