package com.howdoicomputer.android.shoppingwithfriends.view.act;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;

import com.firebase.client.Firebase;
import com.google.gson.Gson;
import com.howdoicomputer.android.shoppingwithfriends.R;
import com.howdoicomputer.android.shoppingwithfriends.handler.LoginHandler;
import com.howdoicomputer.android.shoppingwithfriends.model.database.Database;
import com.howdoicomputer.android.shoppingwithfriends.model.pojo.Account;
import com.howdoicomputer.android.shoppingwithfriends.view.viewinterface.WelcomeView;

/**
 * {@link WelcomeAct} Activity that are responsible for user registration and authentication.
 *
 * @author Yoel Ivan (yivan3@gatech.edu)
 * @version %I%, %G%
 */
public class WelcomeAct extends ActionBarActivity implements WelcomeView {
    private LoginHandler        loginHandler;
    private ProgressDialog      mConnProgressDialog;
    private AlertDialog.Builder mErrorDialog;
    private View                mSplash;

    private boolean isAuthenticated = false;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        mSplash = findViewById(R.id.splash);

        Firebase.setAndroidContext(getApplicationContext());
        loginHandler = new LoginHandler(this);
        loginHandler.checkAuthentication(); // check whether user has logged in before


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mSplash.setVisibility(View.GONE);
                if (savedInstanceState == null && !isAuthenticated) {
                    getSupportFragmentManager().beginTransaction().add(
                            R.id.welcomeFragmentContainer, new WelcomeFragment()).commit();
                }
            }
        }, 2000);

        /* setup the progress dialog that is displayed later when connecting to the server */
        mConnProgressDialog = new ProgressDialog(this);
        mConnProgressDialog.setCancelable(false);

        /* setup the error dialog that is displayed later when input error detected */
        mErrorDialog = new AlertDialog.Builder(this, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT)
                .setPositiveButton(android.R.string.ok, null).setIcon(
                        android.R.drawable.ic_dialog_alert);
    }

    @Override
    public void showErrorDialog(String message) {
        mErrorDialog.setTitle("Error").setMessage(message).show();
    }

    @Override
    public void showProgressDialog(final String title, final String message) {
        mConnProgressDialog.setTitle(title);
        mConnProgressDialog.setMessage(message);
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
                R.id.frag_login_usrName_text);
        loginUserName.setError(message);
        loginUserName.requestFocus();
    }

    @Override
    public void loginPasswordError(String message) {
        EditText loginPassword = (EditText) findViewById(R.id.frag_login_password_text);
        loginPassword.setError(message);
        loginPassword.requestFocus();
    }

    @Override
    public void registerUserNameError(String message) {
        AutoCompleteTextView usrName = (AutoCompleteTextView) findViewById(
                R.id.frag_reg_usrName_text);
        usrName.setError(message);
        usrName.requestFocus();

    }

    @Override
    public void registerEmailAddressError(String message) {
        AutoCompleteTextView email = (AutoCompleteTextView) findViewById(R.id.frag_reg_email_text);
        email.setError(message);
        email.requestFocus();
    }

    @Override
    public void registerPasswordError(String message) {
        EditText pass = (EditText) findViewById(R.id.frag_reg_pass1_text);
        pass.setError(message);
        pass.requestFocus();

    }

    @Override
    public void registerConfirmPasswordError(String message) {
        EditText passConfirmed = (EditText) findViewById(R.id.frag_reg_pass2_text);
        passConfirmed.setError(message);
        passConfirmed.requestFocus();
    }

    @Override
    public void registerNameError(String message) {
        AutoCompleteTextView name = (AutoCompleteTextView) findViewById(R.id.frag_reg_name_text);
        name.setError(message);
        name.requestFocus();
    }

    @Override
    public void onAuthenticated(Account acc) {
        isAuthenticated = true;
        Intent mainApp = new Intent();
        mainApp.setClass(getApplicationContext(), AppActivity.class);
        mainApp.putExtra("Account", new Gson().toJson(acc));
        startActivity(mainApp);
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (!isAuthenticated) {
            Database.destroyInstance();
        }
    }

    /*
        ######################################
        ##      Button binded method        ##
        ######################################
     */

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
}