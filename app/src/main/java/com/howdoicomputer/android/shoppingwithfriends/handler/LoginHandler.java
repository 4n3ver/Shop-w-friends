package com.howdoicomputer.android.shoppingwithfriends.handler;

import com.howdoicomputer.android.shoppingwithfriends.model.database.Database;
import com.howdoicomputer.android.shoppingwithfriends.model.database.DatabaseError;
import com.howdoicomputer.android.shoppingwithfriends.model.databaseinterface.LoginModel;
import com.howdoicomputer.android.shoppingwithfriends.model.pojo.Account;
import com.howdoicomputer.android.shoppingwithfriends.view.viewinterface.WelcomeView;

import org.apache.commons.validator.routines.EmailValidator;

/**
 * {@link LoginHandler} handles event from login view GUI and process related data from the model.
 *
 * @author Yoel Ivan
 * @version %I%, %G%
 */
public class LoginHandler {
    private LoginModel  db;
    private WelcomeView view;

    /**
     * Construct {@link LoginHandler} object.
     *
     * @param view reference to the implementing GUI
     */
    public LoginHandler(WelcomeView view) {
        db = Database.getInstace();
        this.view = view;
    }

    /**
     * Check whether a user has been authenticated on this client.
     */
    public void checkAuthentication() {
        db.checkAuthentication(new AuthenticationStateListener());
    }

    /**
     * Authenticate with user provided username and password.
     *
     * @param usrName  {@link String} representation of username
     * @param password {@link String} representation of password
     */
    public void login(String usrName, String password) {
        if (checkLoginInput(usrName, password)) {
            view.showProgressDialog("Logging in", "Please wait...");
            db.login(usrName, password, new AuthenticationStateListener());

        }
    }

    private boolean checkLoginInput(String usrName, String password) {
        boolean error = false;
        if (password.length() == 0) {
            view.loginPasswordError("This field is required");
            error = true;
        }
        if (usrName.length() == 0) {
            view.loginUserNameError("This field is required");
            error = true;
        }
        return !error;
    }

    /**
     * Register new account with provided information.
     *
     * @param usrName  user provided <code>usrName</code>
     * @param email    user provided <code>email</code>
     * @param pass     user provided <code>pass</code>
     * @param passConf user provided <code>passConf</code>
     */
    public void register(String name, final String usrName, String email, final String pass,
            String passConf) {
        if (checkRegisterInput(name, usrName, email, pass, passConf)) {
            view.showProgressDialog("Registering", "Please wait...");
            db.register(name, usrName, email.toLowerCase(), pass,
                    new LoginModel.RegisterStateListener() {

                        @Override
                        public void onSuccess() {
                            login(usrName, pass);
                            view.hideProgressDialog();
                        }

                        @Override
                        public void onError(DatabaseError error) {
                            view.hideProgressDialog();
                            if (error.getCode() == DatabaseError.USERNAME_TAKEN) {
                                view.showErrorDialog("Username has been taken");
                            } else if (error.getCode() == DatabaseError.EMAIL_TAKEN) {
                                view.showErrorDialog("Email has been registered");
                            } else {
                                view.showErrorDialog(error.toString());
                            }
                        }
                    });
        }
    }

    private boolean checkRegisterInput(String name, String usrName, String email, String pass,
            String passConf) {
        boolean error = false;
        if (!pass.equals(passConf)) { // check if both password fields are identical
            view.registerPasswordError("Password does not match");
            view.registerConfirmPasswordError("Password does not match");
            error = true;
        }
        if (pass.length() < 6) {
            view.registerPasswordError("Password has to be longer than 6 characters");
            view.registerConfirmPasswordError("Password has to be longer than 6 characters");
            error = true;
        }
        if (!EmailValidator.getInstance().isValid(email)) {
            view.registerEmailAddressError("Invalid email");
            error = true;
        }
        if (usrName.length() == 0) {
            view.registerUserNameError("This field is required");
            error = true;
        }
        if (name.length() == 0) {
            view.registerNameError("This field is required");
            error = true;
        }
        return !error;
    }

    /**
     * Implementation of {@link LoginModel.AuthenticationStateListener}.
     */
    private class AuthenticationStateListener implements LoginModel.AuthenticationStateListener {
        @Override
        public void onAuthenticated(Account acc) {
            view.onAuthenticated(acc);
            view.hideProgressDialog();
        }

        @Override
        public void onNotAuthenticated() {
            view.showWelcome();
        }

        @Override
        public void onError(DatabaseError error) {
            view.hideProgressDialog();
            if (error.getCode() == DatabaseError.INVALID_PASSWORD) {
                view.showErrorDialog("Invalid password");
            } else if (error.getCode() == DatabaseError.USERNAME_NOT_EXIST) {
                view.showErrorDialog("Username does not exist");
            } else {
                view.showErrorDialog(error.toString());
            }
        }
    }
}