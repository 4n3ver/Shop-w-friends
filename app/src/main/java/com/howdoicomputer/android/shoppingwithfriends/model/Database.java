package com.howdoicomputer.android.shoppingwithfriends.model;

import com.firebase.client.AuthData;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.google.gson.Gson;

/**
 * {@link Database} where all cool data hang...
 *
 * @author Yoel Ivan
 * @version %I%, %G%
 */
public class Database implements LoginModel, MainModel, FriendListModel {
    private static Database singletonInstance;

    private Firebase mAccDatabase;

    private Database() {
        mAccDatabase = new Firebase("https://crackling-heat-6364.firebaseio.com/");
    }

    /**
     * Request an instance of {@link Database} object.
     *
     * @return instance of this object
     */
    public static Database getInstace() {
        if (singletonInstance == null) {
            singletonInstance = new Database();
        }
        return singletonInstance;
    }

    /**
     * Call this method to let garbage collector destroy this instance.
     */
    public static void destroyInstance() {
        singletonInstance = null;

    }

    @Override
    public void checkAuthentication(final AuthenticationStateListener listener) {

        /* check if the user is authenticated with Firebase already */
        mAccDatabase.addAuthStateListener(new Firebase.AuthStateListener() {
            @Override
            public void onAuthStateChanged(final AuthData authData) {
                if (authData != null) {
                    mAccDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot snapshot) {
                            String userName = snapshot.child("userEmail").child(
                                    ((String) authData.getProviderData().get("email")).replaceAll(
                                            "\\.", ",")).getValue(String.class);

                            fetchAccountInfo(userName, new AccountStateListener() {
                                @Override
                                public void onFound(Account account) {
                                    listener.onAuthenticated(account);
                                }

                                @Override
                                public void onNotFound() {
                                    listener.onError(new DatabaseError(Integer.MAX_VALUE,
                                            "WTF...!!"));
                                }

                                @Override
                                public void onError(DatabaseError error) {
                                    listener.onError(error);
                                }
                            });
                        }

                        @Override
                        public void onCancelled(FirebaseError firebaseError) {
                            listener.onError(new DatabaseError(firebaseError));
                        }
                    });
                }
            }
        });
    }

    @Override
    public void login(final String userName, final String password,
            final AuthenticationStateListener listener) {

        fetchAccountInfo(userName, new AccountStateListener() {
            @Override
            public void onFound(final Account account) {
                mAccDatabase.authWithPassword(account.getEmail(), password,
                        new Firebase.AuthResultHandler() {

                            @Override
                            public void onAuthenticated(AuthData authData) {
                                if (authData != null) {
                                    listener.onAuthenticated(account);
                                }
                            }

                            @Override
                            public void onAuthenticationError(FirebaseError firebaseError) {
                                listener.onError(new DatabaseError(firebaseError));
                            }
                        });
            }

            @Override
            public void onNotFound() {
                listener.onError(new DatabaseError(DatabaseError.USERNAME_NOT_EXIST,
                        "Username does not exist"));
            }

            @Override
            public void onError(DatabaseError error) {
                listener.onError(error);
            }
        });

    }

    @Override
    public void register(final String name, final String userName, final String email,
            final String password, final RegisterStateListener listener) {
        fetchAccountInfo(userName, new AccountStateListener() {
            @Override
            public void onFound(Account account) {
                listener.onError(new DatabaseError(DatabaseError.USERNAME_TAKEN,
                        "Username has been taken"));
            }

            @Override
            public void onNotFound() {
                mAccDatabase.createUser(email, password, new Firebase.ResultHandler() {
                    @Override
                    public void onSuccess() {
                        mAccDatabase.child("userAccount").child(userName).setValue(
                                new Gson().toJson(new User(name, userName, email), User.class));
                        mAccDatabase.child("userEmail").child(email.replaceAll("\\.", ","))
                                .setValue(userName);
                        listener.onSuccess();
                    }

                    @Override
                    public void onError(FirebaseError firebaseError) {
                        listener.onError(new DatabaseError(firebaseError));
                    }
                });
            }

            @Override
            public void onError(DatabaseError error) {
                listener.onError(error);
            }
        });
    }

    @Override
    public void logout() {
        mAccDatabase.unauth();
    }

    /**
     * Send {@link Account} for <code>userName</code> and send it to <code>listener</code>.
     *
     * @param userName user name of the {@link Account} to be fetched
     * @param listener target listener where the {@link Account} need to be send
     */
    public void fetchAccountInfo(final String userName, final AccountStateListener listener) {
        mAccDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (snapshot.child("userAccount").hasChild(userName)) {
                    listener.onFound(new Gson().fromJson(snapshot.child("userAccount").child(
                            userName).getValue(String.class), User.class));
                } else {
                    listener.onNotFound();
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                listener.onError(new DatabaseError(firebaseError));
            }
        });
    }

    public void updateAccount(Account account) {
        mAccDatabase.child("userAccount").child(account.getUserName()).setValue(new Gson().toJson(
                account, User.class));
    }

}