package com.howdoicomputer.android.shoppingwithfriends.act;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;

import com.firebase.client.Firebase;
import com.howdoicomputer.android.shoppingwithfriends.R;
import com.howdoicomputer.android.shoppingwithfriends.handler.LoginHandler;
import com.howdoicomputer.android.shoppingwithfriends.model.Account;
import com.howdoicomputer.android.shoppingwithfriends.view.WelcomeView;

/**
 * {@link WelcomeAct} Activity that are responsible for user registration and authentication.
 *
 * @author Yoel Ivan (yivan3@gatech.edu)
 * @version %I%, %G%
 */
public class WelcomeAct extends ActionBarActivity implements WelcomeView {
    private LoginHandler loginHandler;
    private ProgressDialog mConnProgressDialog;
    private View mSplash;
    private boolean isAuthenticated = false;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState); setContentView(R.layout.activity_welcome);
        mSplash = findViewById(R.id.splash);

        /* setup the progress dialog that is displayed later when connecting to the server */
        mConnProgressDialog = new ProgressDialog(this); mConnProgressDialog.setCancelable(false);

        Firebase.setAndroidContext(getApplicationContext());
        loginHandler = new LoginHandler(this);
        loginHandler.checkAuthentication(); // check whether user has logged in before


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mSplash.setVisibility(View.GONE);
                if (savedInstanceState == null && !isAuthenticated) {
                    getSupportFragmentManager().beginTransaction()
                            .add(R.id.welcomeFragmentContainer, new WelcomeFragment()).commit();
                }
            }
        }, 2000);
    }

    @Override
    public void showErrorDialog(String message) {
        new AlertDialog.Builder(this, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT).setTitle("Error")
                .setMessage(message).setPositiveButton(android.R.string.ok, null)
                .setIcon(android.R.drawable.ic_dialog_alert).show();
    }

    @Override
    public void showProgressDialog(final String title, final String message) {
        mConnProgressDialog.setTitle(title); mConnProgressDialog.setMessage(message);
        mConnProgressDialog.show();
    }

    @Override
    public void hideProgressDialog() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mConnProgressDialog.hide();
            }
        }, 2000);
    }

    @Override
    public void loginUserNameError(String message) {
        AutoCompleteTextView loginUserName = (AutoCompleteTextView) findViewById(
                R.id.frag_login_usrName_text); loginUserName.setError(message);
        loginUserName.requestFocus();
    }

    @Override
    public void loginPasswordError(String message) {
        EditText loginPassword = (EditText) findViewById(R.id.frag_login_password_text);
        loginPassword.setError(message); loginPassword.requestFocus();
    }

    @Override
    public void registerUserNameError(String message) {
        AutoCompleteTextView usrName = (AutoCompleteTextView) findViewById(
                R.id.frag_reg_usrName_text); usrName.setError(message); usrName.requestFocus();

    }

    @Override
    public void registerEmailAddressError(String message) {
        AutoCompleteTextView email = (AutoCompleteTextView) findViewById(R.id.frag_reg_email_text);
        email.setError(message); email.requestFocus();
    }

    @Override
    public void registerPasswordError(String message) {
        EditText pass = (EditText) findViewById(R.id.frag_reg_pass1_text); pass.setError(message);
        pass.requestFocus();

    }

    @Override
    public void registerConfirmPasswordError(String message) {
        EditText passConfirmed = (EditText) findViewById(R.id.frag_reg_pass2_text);
        passConfirmed.setError(message); passConfirmed.requestFocus();
    }

    @Override
    public void onAuthenticated(Account acc) {
        isAuthenticated = true; Intent mainApp = new Intent();
        mainApp.setClass(getApplicationContext(), AppActivity.class); startActivity(mainApp);
        finish();
    }

    /*
        ######################################
        ##      Button binded method        ##
        ######################################
     */

    /**
     * Create new account and launch the {@link AppActivity}.
     *
     * @param v reference to the login button object on <layout>fragment_register</layout>
     */
    public void reg(View v) {
        AutoCompleteTextView usrName = (AutoCompleteTextView) findViewById(
                R.id.frag_reg_usrName_text);
        AutoCompleteTextView email = (AutoCompleteTextView) findViewById(R.id.frag_reg_email_text);
        EditText pass = (EditText) findViewById(R.id.frag_reg_pass1_text);
        EditText passConfirmed = (EditText) findViewById(R.id.frag_reg_pass2_text);

        try {

            loginHandler.register(usrName.getText().toString(), email.getText().toString(),
                                  pass.getText().toString(), passConfirmed.getText().toString());
        } catch (Exception e) {

            showErrorDialog(e.toString());
        }
    }

    /**
     * Authenticate with user provided username and password.
     *
     * @param v reference to the login button object on <layout>fragment_login</layout>
     */
    public void auth(View v) {
        AutoCompleteTextView loginUserName = (AutoCompleteTextView) findViewById(
                R.id.frag_login_usrName_text);
        EditText loginPassword = (EditText) findViewById(R.id.frag_login_password_text);

        loginHandler.login(loginUserName.getText().toString(), loginPassword.getText().toString());
    }

    /**
     * Go back to previous fragment.
     *
     * @param v reference to the cancel button object
     */
    public void cancel(View v) {
        ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE))
                .hideSoftInputFromWindow(v.getWindowToken(), 0);
        getSupportFragmentManager().popBackStackImmediate();
    }

    /**
     * Replace Welcome fragment with Login fragment.
     *
     * @param v reference to the login button object on <layout>fragment_welcome</layout>
     */
    public void loginClicked(View v) {
        Fragment loginFragment = new LoginFragment();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        transaction.replace(R.id.welcomeFragmentContainer, loginFragment);
        transaction.addToBackStack(null);    // let user navigate back to previous fragment

        transaction.commit();
    }

    /**
     * Replace Welcome fragment with Register fragment.
     *
     * @param v reference to the register button object on <layout>fragment_welcome</layout>
     */
    public void registerClicked(View v) {
        Fragment registerFragment = new RegisterFragment();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        transaction.replace(R.id.welcomeFragmentContainer, registerFragment);
        transaction.addToBackStack(null);    // let user navigate back to previous fragment

        transaction.commit();
    }

    /*
        ######################################################
        ##       WelcomeAct.java Fragments' classes         ##
        ######################################################
     */

    /**
     * generated from template
     */
    public static class WelcomeFragment extends Fragment {

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {

            /* change the R.layout.* argument to reuse the code */
            View rootView = inflater.inflate(R.layout.fragment_welcome, container, false);
            return rootView;
        }
    }

    /**
     * generated from template
     */
    public static class LoginFragment extends Fragment {

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_login, container, false);
            return rootView;
        }

    }

    /**
     * generated from template
     */
    public static class RegisterFragment extends Fragment {

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_register, container, false);
            return rootView;
        }

    }
}