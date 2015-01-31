package com.howdoicomputer.android.shoppingwithfriends;

import android.content.Context;
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

import com.firebase.client.AuthData;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.Map;

/**
 * @author Yoel Ivan (yivan3@gatech.edu)
 */
public class WelcomeAct extends ActionBarActivity {
    private static Firebase accDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Firebase.setAndroidContext(getApplicationContext());
        accDB = new Firebase("https://crackling-heat-6364.firebaseio.com/");
        setContentView(R.layout.activity_welcome);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.welcomeFragmentContainer, new WelcomeFragment())
                    .commit();
        }
    }

    //TODO: This really need clean up *sigh*
    public void auth(View v) {

        accDB.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot snapshot) {
                Map<String, String> accMap = (Map<String, String>) ((Map<String, Object>) snapshot.getValue()).get("userAccount");
                AutoCompleteTextView usrName = (AutoCompleteTextView) findViewById(R.id.frag_login_usrName_text);
                EditText pass = (EditText) findViewById(R.id.frag_login_password_text);

                accDB.authWithPassword(accMap.get(usrName.getText().toString()), pass.getText().toString(), new Firebase.AuthResultHandler() {
                    @Override
                    public void onAuthenticated(AuthData authData) {
                        Toast.makeText(getApplicationContext(), "User ID: " + authData.getUid() + ", Provider: " + authData.getProvider(),
                                Toast.LENGTH_SHORT).show();
                        Intent mainApp = new Intent();
                        mainApp.setClass(getApplicationContext(), AppActivity.class);
                        startActivity(mainApp);
                        finish();
                    }

                    @Override
                    public void onAuthenticationError(FirebaseError firebaseError) {
                        Toast.makeText(getApplicationContext(), firebaseError.getMessage(),
                                Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                Toast.makeText(getApplicationContext(), "The read failed: " + firebaseError.getMessage(),
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void reg(View v) {
        final AutoCompleteTextView usrName = (AutoCompleteTextView) findViewById(R.id.frag_reg_usrName_text);
        final AutoCompleteTextView email = (AutoCompleteTextView) findViewById(R.id.frag_reg_email_text);
        final EditText pass = (EditText) findViewById(R.id.frag_reg_pass1_text);
        final EditText passConfirmed = (EditText) findViewById(R.id.frag_reg_pass2_text);

        accDB.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                final Map<String, String> accMap = (Map<String, String>) ((Map<String, Object>) snapshot.getValue()).get("userAccount");

                if (accMap.containsKey(usrName.getText().toString())) { // username taken

                    Toast.makeText(getApplicationContext(), "username has been taken",
                            Toast.LENGTH_SHORT).show();
                } else if (pass.getText().toString().equals(passConfirmed.getText().toString())) { // check if both password fields are identical
                    accMap.put(usrName.getText().toString(), email.getText().toString());

                    accDB.createUser(email.getText().toString(), pass.getText().toString(), new Firebase.ResultHandler() {
                        @Override
                        public void onSuccess() {
                            accDB.child("userAccount").setValue(accMap);
                            Toast.makeText(getApplicationContext(), "Success",
                                    Toast.LENGTH_SHORT).show();

                            // clean up
                            usrName.setText("");
                            email.setText("");
                            pass.setText("");
                            passConfirmed.setText("");
                        }

                        @Override
                        public void onError(FirebaseError firebaseError) {
                            Toast.makeText(getApplicationContext(), firebaseError.toString(),
                                    Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    Toast.makeText(getApplicationContext(), "password does not match",
                            Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                Toast.makeText(getApplicationContext(), "The read failed: " + firebaseError.getMessage(),
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void saveToken(String token) {
        Writer writer = null;
        try {
            OutputStream out = getApplicationContext().openFileOutput("loginToken", Context.MODE_PRIVATE);
            writer = new OutputStreamWriter(out);
            writer.write(token);
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), e.toString(),
                    Toast.LENGTH_LONG).show();
        } finally {
            if (writer != null) {
                try {
                    writer.close();
                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), e.toString(),
                            Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    public void loginClicked(View v) {
        Fragment loginFragment = new LoginFragment();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        transaction.replace(R.id.welcomeFragmentContainer, loginFragment);
        transaction.addToBackStack(null);    // let user navigate back to previous fragment

        transaction.commit();
    }

    public void registerClicked(View v) {
        Fragment registerFragment = new RegisterFragment();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        transaction.replace(R.id.welcomeFragmentContainer, registerFragment);
        transaction.addToBackStack(null);    // let user navigate back to previous fragment

        transaction.commit();
    }

    /*
     * Below are inner static class
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
