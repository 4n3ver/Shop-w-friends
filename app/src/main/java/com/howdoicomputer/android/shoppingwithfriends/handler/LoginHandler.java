package com.howdoicomputer.android.shoppingwithfriends.handler;

import com.howdoicomputer.android.shoppingwithfriends.model.Account;
import com.howdoicomputer.android.shoppingwithfriends.model.database.Database;
import com.howdoicomputer.android.shoppingwithfriends.model.database.DatabaseError;
import com.howdoicomputer.android.shoppingwithfriends.model.database.LoginModel;
import com.howdoicomputer.android.shoppingwithfriends.view.WelcomeView;

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
    public void login(final String usrName, final String password) {
        boolean error = false;
        if (password.length() == 0) {
            view.loginPasswordError("This field is required");
            error = true;
        }
        if (usrName.length() == 0) {
            view.loginUserNameError("This field is required");
            error = true;
        }
        if (!error) {
            view.showProgressDialog("Logging in", "Please wait...");
            db.login(usrName, password, new AuthenticationStateListener());
            view.hideProgressDialog();
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
    public void register(final String name, final String usrName, final String email,
            final String pass, final String passConf) {
        EmailValidator emailValidator = EmailValidator.getInstance();
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
        if (!emailValidator.isValid(email)) {
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
        if (!error) {
            view.showProgressDialog("Registering", "Please wait...");
            db.register(name, usrName, email.toLowerCase(), pass,
                    new LoginModel.RegisterStateListener() {
                        @Override
                        public void onSuccess() {
                            login(usrName, pass);
                        }

                        @Override
                        public void onError(DatabaseError error) {
                            if (error.getCode() == DatabaseError.USERNAME_TAKEN) {
                                view.showErrorDialog("Username has been taken");
                            } else if (error.getCode() == DatabaseError.EMAIL_TAKEN) {
                                view.showErrorDialog("Email has been registered");
                            } else {
                                view.showErrorDialog(error.toString());
                            }
                        }
                    });
            view.hideProgressDialog();
        }
    }

    /**
     * Implementation of {@link LoginModel.AuthenticationStateListener}.
     */
    private class AuthenticationStateListener implements LoginModel.AuthenticationStateListener {
        @Override
        public void onAuthenticated(Account acc) {
            view.onAuthenticated(acc);
        }

        @Override
        public void onError(DatabaseError error) {
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