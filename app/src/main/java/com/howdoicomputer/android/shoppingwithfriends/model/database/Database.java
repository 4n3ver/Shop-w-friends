package com.howdoicomputer.android.shoppingwithfriends.model.database;

import com.firebase.client.AuthData;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.google.gson.Gson;
import com.howdoicomputer.android.shoppingwithfriends.model.databaseinterface.AccountStateListener;
import com.howdoicomputer.android.shoppingwithfriends.model.databaseinterface
        .FetchAccountResultListener;
import com.howdoicomputer.android.shoppingwithfriends.model.databaseinterface.FriendListModel;
import com.howdoicomputer.android.shoppingwithfriends.model.databaseinterface.LoginModel;
import com.howdoicomputer.android.shoppingwithfriends.model.databaseinterface.MainFeedModel;
import com.howdoicomputer.android.shoppingwithfriends.model.databaseinterface.MainModel;
import com.howdoicomputer.android.shoppingwithfriends.model.pojo.Account;
import com.howdoicomputer.android.shoppingwithfriends.model.pojo.FriendList;
import com.howdoicomputer.android.shoppingwithfriends.model.pojo.Item;
import com.howdoicomputer.android.shoppingwithfriends.model.pojo.User;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

/**
 * {@link Database} where all cool data hang...
 *
 * @author Yoel Ivan
 * @version %I%, %G%
 */
public class Database implements LoginModel, MainModel, FriendListModel, MainFeedModel {

    /* singleton instance */
    private static Database singletonInstance;

    private Firebase mAccDatabase;
    private Gson mGson;

    private Database() {
        mGson = new Gson();
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

                            fetchAccountInfo(userName, new FetchAccountResultListener() {
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
                } else {
                    listener.onNotAuthenticated();
                }
            }
        });
    }

    @Override
    public void login(final String userName, final String password,
            final AuthenticationStateListener listener) {

        fetchAccountInfo(userName, new FetchAccountResultListener() {
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
        fetchAccountInfo(userName, new FetchAccountResultListener() {
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
                        mAccDatabase.child("userAccount").child(userName).setValue(mGson.toJson(
                                new User(name, userName, email), User.class));
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

    @Override
    public void fetchAccountInfo(final String userName, final FetchAccountResultListener listener) {
        mAccDatabase.child("userAccount").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (snapshot.hasChild(userName)) {
                    listener.onFound(mGson.fromJson(snapshot.child(userName).getValue(String.class),
                            User.class));
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

    @Override
    public void updateAccount(Account account) {
        mAccDatabase.child("userAccount").child(account.getUserName()).setValue(mGson.toJson(
                account, User.class));
    }

    @Override
    public void fetchFriendAccountInfo(final FriendList friendList,
            final AccountStateListener listener) {
        new Thread(new Runnable() {
            public void run() {
                for (User friend : friendList) {
                    mAccDatabase.child("userAccount").child(friend.getUserName())
                            .addValueEventListener(new ValueEventListener() {

                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    listener.onAccountChanged(mGson.fromJson(dataSnapshot.getValue(
                                            String.class), User.class));
                                }

                                @Override
                                public void onCancelled(FirebaseError error) {
                                    listener.onError(new DatabaseError(error));
                                }
                            });
                }
            }
        }).start();
    }

    @Override
    public void pushItemPost(Item item) {
        String itemType = "";
        if (item.isInterest()) {
            itemType = "userInterestItem";
        } else {
            itemType = "userReportedItem";
        }
        mAccDatabase.child(itemType).child(item.getPosterUserName()).push().setValue(mGson.toJson(
                item));
    }

    @Override
    public void fetchFriendsItemOfInterest(final List<String> friendsUsernameList,
            final FeedListener listener) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (String friendUsername : friendsUsernameList) {
                    mAccDatabase.child("userInterestItem").child(friendUsername)
                            .addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    Map<String, String> snap = (Map<String, String>) dataSnapshot
                                            .getValue();
                                    if (snap != null) {
                                        Collection<String> m = snap.values();
                                        Collection<Item> set = new HashSet<Item>(m.size());
                                        for (String item : m) {
                                            set.add(mGson.fromJson(item, Item.class));
                                        }
                                        listener.onListFetched(set);
                                    }
                                }

                                @Override
                                public void onCancelled(FirebaseError error) {
                                    listener.onError(new DatabaseError(error));
                                }
                            });
                }
            }
        }).start();
    }
}