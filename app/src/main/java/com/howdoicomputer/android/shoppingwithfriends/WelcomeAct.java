package com.howdoicomputer.android.shoppingwithfriends;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.Toast;

import com.firebase.client.AuthData;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.Map;

/**
 * {@link WelcomeAct} Activity that are responsible for user registration and authentication.
 *
 * @author Yoel Ivan (yivan3@gatech.edu)
 * @version 0.04 02/05/15
 */
public class WelcomeAct extends ActionBarActivity {
    private static final String TAG = WelcomeAct.class.getSimpleName();
    private static Firebase mAccDatabase;
    private static Map<String, String> accMap;  //TODO: change way data stored in database
    private ProgressDialog mConnProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Firebase.setAndroidContext(getApplicationContext());
        mAccDatabase = new Firebase("https://crackling-heat-6364.firebaseio.com/");

        /* fetch username-email db from server */
        mAccDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                accMap = (Map<String, String>) ((Map<String, Object>) snapshot.getValue()).get("userAccount");
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                Toast.makeText(getApplicationContext(), "The read failed: " + firebaseError.getMessage(),
                        Toast.LENGTH_SHORT).show();
            }
        });

        /* setup the progress dialog that is displayed later when connecting to the server */
        mConnProgressDialog = new ProgressDialog(this);
        mConnProgressDialog.setTitle("Loading");
        mConnProgressDialog.setMessage("Connecting...");
        mConnProgressDialog.setCancelable(false);

        tryAuth();  // check whether user has logged in before

        setContentView(R.layout.activity_welcome);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.welcomeFragmentContainer, new WelcomeFragment())
                    .commit();
        }
    }

    /**
     * Check whether user has logged in before. If user has logged in before {@link AppActivity} will be launched
     */
    private void tryAuth() {
        mConnProgressDialog.show();

        /* check if the user is authenticated with Firebase already */
        mAccDatabase.addAuthStateListener(new Firebase.AuthStateListener() {
            @Override
            public void onAuthStateChanged(AuthData authData) {
                mConnProgressDialog.hide();
                startMainAct(authData);
            }
        });
    }

    /**
     * Once a user is logged in, start the {@link AppActivity}.
     */
    private void startMainAct(AuthData authData) {
        if (authData != null) { // check login token validity
            Intent mainApp = new Intent();
            mainApp.setClass(getApplicationContext(), AppActivity.class);
            startActivity(mainApp);
            finish();
        }
    }

    /**
     * Authenticate with user provided username and password.
     *
     * @param usrName  {@link java.lang.String} representation of username
     * @param password {@link java.lang.String} representation of password
     */
    private void login(String usrName, String password) {
        // TODO: implement text watcher/listener for error checking
        mConnProgressDialog.show();
        String email = accMap.get(usrName);
        if (email == null) {    // check whether username exists
            mConnProgressDialog.hide();
            showErrorDialog("Username does not exist");
        } else {
            mAccDatabase.authWithPassword(email, password, new Firebase.AuthResultHandler() {
                @Override
                public void onAuthenticated(AuthData authData) {
                    mConnProgressDialog.hide();
                    Log.i(TAG, "auth successful");
                    startMainAct(authData);
                }

                @Override
                public void onAuthenticationError(FirebaseError firebaseError) {
                    mConnProgressDialog.hide();
                    showErrorDialog(firebaseError.toString());
                }
            });
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
    private void register(final String usrName, String email, final String pass, String passConf) {
        mConnProgressDialog.show();
        // TODO: implement text watcher/listener for error checking
        if (accMap.containsKey(usrName)) { // username taken
            showErrorDialog("Username has been taken");
        } else if (pass.equals(passConf)) { // check if both password fields are identical
            accMap.put(usrName.toString(), email);
            mAccDatabase.createUser(email, pass, new Firebase.ResultHandler() {
                @Override
                public void onSuccess() {
                    mConnProgressDialog.hide();
                    mAccDatabase.child("userAccount").setValue(accMap);
                    login(usrName, pass);
                }

                @Override
                public void onError(FirebaseError firebaseError) {
                    mConnProgressDialog.hide();
                    showErrorDialog(firebaseError.toString());
                }
            });
        } else {
            mConnProgressDialog.hide();
            showErrorDialog("Password does not match");
        }
    }

    /**
     * Show error dialog to user.
     *
     * @param message error message
     */
    private void showErrorDialog(String message) {
        new AlertDialog.Builder(this)
                .setTitle("Error")
                .setMessage(message)
                .setPositiveButton(android.R.string.ok, null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
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
        AutoCompleteTextView usrName = (AutoCompleteTextView) findViewById(R.id.frag_reg_usrName_text);
        AutoCompleteTextView email = (AutoCompleteTextView) findViewById(R.id.frag_reg_email_text);
        EditText pass = (EditText) findViewById(R.id.frag_reg_pass1_text);
        EditText passConfirmed = (EditText) findViewById(R.id.frag_reg_pass2_text);

        register(usrName.getText().toString(), email.getText().toString(), pass.getText().toString(), passConfirmed.getText().toString());
    }

    /**
     * Authenticate with user provided username and password.
     *
     * @param v reference to the login button object on <layout>fragment_login</layout>
     */
    public void auth(View v) {
        AutoCompleteTextView usrName = (AutoCompleteTextView) findViewById(R.id.frag_login_usrName_text);
        EditText pass = (EditText) findViewById(R.id.frag_login_password_text);

        login(usrName.getText().toString(), pass.getText().toString());
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
        public WelcomeFragment() {
        }


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

        public LoginFragment() {
        }

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

        public RegisterFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_register, container, false);
            return rootView;
        }

    }
}
