package com.howdoicomputer.android.shoppingwithfriends.act;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;

import com.firebase.client.Firebase;
import com.howdoicomputer.android.shoppingwithfriends.handler.LoginHandler;
import com.howdoicomputer.android.shoppingwithfriends.view.WelcomeView;

/**
 * {@link WelcomeAct} Activity that are responsible for user registration and authentication.
 *
 * @author Yoel Ivan (yivan3@gatech.edu)
 * @version %I%, %G%
 */
public class WelcomeAct extends ActionBarActivity implements WelcomeView {
    private static final String TAG = WelcomeAct.class.getSimpleName();

    private LoginHandler loginHandler;
    private ProgressDialog mConnProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.howdoicomputer.android.shoppingwithfriends.R.layout.activity_welcome);

        /* setup the progress dialog that is displayed later when connecting to the server */
        mConnProgressDialog = new ProgressDialog(this); mConnProgressDialog.setTitle("Loading");
        mConnProgressDialog.setMessage("Connecting..."); mConnProgressDialog.setCancelable(false);

        mConnProgressDialog.show(); Firebase.setAndroidContext(getApplicationContext());
        loginHandler = new LoginHandler(this);
        loginHandler.checkAuthentication(); // check whether user has logged in before
        mConnProgressDialog.hide();

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(com.howdoicomputer.android.shoppingwithfriends.R.id
                                 .welcomeFragmentContainer,
                         new WelcomeFragment()).commit();
        }
    }

    /**
     * Show error dialog to user.
     *
     * @param message error message
     */
    private void showErrorDialog(String message) {
        new AlertDialog.Builder(this, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT).setTitle("Error")
                .setMessage(message).setPositiveButton(android.R.string.ok, null)
                .setIcon(android.R.drawable.ic_dialog_alert).show();
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
                com.howdoicomputer.android.shoppingwithfriends.R.id.frag_reg_usrName_text);
        AutoCompleteTextView email = (AutoCompleteTextView) findViewById(
                com.howdoicomputer.android.shoppingwithfriends.R.id.frag_reg_email_text);
        EditText pass = (EditText) findViewById(
                com.howdoicomputer.android.shoppingwithfriends.R.id.frag_reg_pass1_text);
        EditText passConfirmed = (EditText) findViewById(
                com.howdoicomputer.android.shoppingwithfriends.R.id.frag_reg_pass2_text);

        try {
            mConnProgressDialog.show(); loginHandler
                    .register(usrName.getText().toString(), email.getText().toString(),
                              pass.getText().toString(), passConfirmed.getText().toString());
        } catch (Exception e) {
            mConnProgressDialog.hide(); showErrorDialog(e.toString());
        }
    }

    /**
     * Authenticate with user provided username and password.
     *
     * @param v reference to the login button object on <layout>fragment_login</layout>
     */
    public void auth(View v) {
        AutoCompleteTextView usrName = (AutoCompleteTextView) findViewById(
                com.howdoicomputer.android.shoppingwithfriends.R.id.frag_login_usrName_text);
        EditText pass = (EditText) findViewById(
                com.howdoicomputer.android.shoppingwithfriends.R.id.frag_login_password_text);

        try {
            mConnProgressDialog.show();
            loginHandler.login(usrName.getText().toString(), pass.getText().toString());
        } catch (Exception e) {
            mConnProgressDialog.hide(); showErrorDialog(e.toString());
        }
    }

    /**
     * Go back to previous fragment.
     *
     * @param v reference to the cancel button object
     */
    public void cancel(View v) {
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

        transaction.replace(
                com.howdoicomputer.android.shoppingwithfriends.R.id.welcomeFragmentContainer,
                loginFragment);
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

        transaction.replace(
                com.howdoicomputer.android.shoppingwithfriends.R.id.welcomeFragmentContainer,
                registerFragment);
        transaction.addToBackStack(null);    // let user navigate back to previous fragment

        transaction.commit();
    }

    @Override
    public void onAuthenticated() {
        Intent mainApp = new Intent(); mainApp.setClass(getApplicationContext(), AppActivity.class);
        mConnProgressDialog.hide(); startActivity(mainApp); finish();
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
        public WelcomeFragment() {
        }


        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {

            /* change the R.layout.* argument to reuse the code */
            View rootView = inflater.inflate(
                    com.howdoicomputer.android.shoppingwithfriends.R.layout.fragment_welcome,
                    container, false); return rootView;
        }
    }

    /**
     * generated from template
     */
    public static class LoginFragment extends Fragment {

        public LoginFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater
                    .inflate(com.howdoicomputer.android.shoppingwithfriends.R.layout.fragment_login,
                             container, false); return rootView;
        }

    }

    /**
     * generated from template
     */
    public static class RegisterFragment extends Fragment {

        public RegisterFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(
                    com.howdoicomputer.android.shoppingwithfriends.R.layout.fragment_register,
                    container, false); return rootView;
        }

    }
}
