package com.howdoicomputer.android.shoppingwithfriends;

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
import android.widget.Toast;


public class WelcomeAct extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.welcomeFragmentContainer, new WelcomeFragment())
                    .commit();
        }
    }

    public void auth(View v) {
        AutoCompleteTextView email = (AutoCompleteTextView) findViewById(R.id.usrName_text);
        EditText pass = (EditText) findViewById(R.id.password_text);
        if (email.getText().toString().equals("user") && pass.getText().toString().equals("pass")) {
            Toast.makeText(getApplicationContext(), "OMG!!!",
                    Toast.LENGTH_LONG).show();
            Intent mainApp = new Intent();
            mainApp.setClass(getApplicationContext(), AppActivity.class);
            startActivity(mainApp);
            finish();
        }
    }

    public void loginClicked(View v) {
        Fragment loginFragment = new LoginFragment();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        transaction.replace(R.id.welcomeFragmentContainer, loginFragment);
        transaction.addToBackStack(null);    // let user navigate back to previous fragment

        transaction.commit();
    }

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
}
