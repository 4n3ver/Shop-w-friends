package com.howdoicomputer.android.shoppingwithfriends.model;

import com.firebase.client.AuthData;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.Map;

/**
 * {@link Database} where all cool data hang...
 *
 * @author Yoel Ivan
 * @version %I%, %G%
 */
public class Database implements LoginModel, MainModel {
    private static Firebase mAccDatabase;
    private static Map<String, String> accMap;  //TODO: change the way data stored in database

    public Database() {
        mAccDatabase = new Firebase("https://crackling-heat-6364.firebaseio.com/");
        mAccDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                accMap = (Map<String, String>) ((Map<String, Object>) snapshot.getValue())
                        .get("userAccount");
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                throw firebaseError.toException();
            }
        });
    }

    /**
     * Return email associated with <code>userName</code> passed.
     *
     * @param userName userName of the email owner
     * @return {@link String} representation of email if the <code>userName</code> registered
     * @throws IllegalArgumentException if <code>userName</code> is not registered
     */
    public String getEmail(String userName) {
        if (!userIsRegistered(userName)) {
            throw new IllegalArgumentException("User is not registered");
        }

        return accMap.get(userName);
    }

    @Override
    public boolean userIsRegistered(String userName) {
        return accMap.containsKey(userName);
    }


    @Override
    public void checkAuthentication(final AuthenticationStateListener listener) {

        /* check if the user is authenticated with Firebase already */
        mAccDatabase.addAuthStateListener(new Firebase.AuthStateListener() {
            @Override
            public void onAuthStateChanged(AuthData authData) {
                if (authData != null) {
                    listener.onAuthenticated(new Account());
                }
            }
        });
    }

    @Override
    public void login(String userName, String password,
                      final AuthenticationStateListener listener) {
        String email = this.getEmail(userName);
        mAccDatabase.authWithPassword(email, password, new Firebase.AuthResultHandler() {

            @Override
            public void onAuthenticated(AuthData authData) {
                if (authData != null) {
                    listener.onAuthenticated(new Account());
                }
            }

            @Override
            public void onAuthenticationError(FirebaseError firebaseError) {
                listener.onError(new DatabaseError(firebaseError));
            }
        });
    }

    @Override
    public void register(String userName, String email, String password,
                         final RegisterStateListener listener) {
        mAccDatabase.createUser(email, password, new Firebase.ResultHandler() {
            @Override
            public void onSuccess() {
                mAccDatabase.child("userAccount").setValue(accMap); listener.onSuccess();
            }

            @Override
            public void onError(FirebaseError firebaseError) {
                listener.onError(new DatabaseError(firebaseError));
            }
        }); accMap.put(userName, email);
    }

    @Override
    public void logout() {
        mAccDatabase.unauth();
    }

}