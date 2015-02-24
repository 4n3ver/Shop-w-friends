package com.howdoicomputer.android.shoppingwithfriends.handler;

import com.howdoicomputer.android.shoppingwithfriends.model.database.Database;
import com.howdoicomputer.android.shoppingwithfriends.model.database.DatabaseError;
import com.howdoicomputer.android.shoppingwithfriends.model.databaseinterface.AccountStateListener;
import com.howdoicomputer.android.shoppingwithfriends.model.databaseinterface.FriendListModel;
import com.howdoicomputer.android.shoppingwithfriends.model.pojo.Account;
import com.howdoicomputer.android.shoppingwithfriends.model.pojo.User;
import com.howdoicomputer.android.shoppingwithfriends.view.viewinterface.FriendListView;

/**
 * Created by Yoel Ivan on 2/15/2015.
 */
public class FriendListHandler {
    private FriendListModel db;
    private FriendListView  view;
    private User            currentUser;

    public FriendListHandler(FriendListView view, User currentUser) {
        db = Database.getInstace();
        this.view = view;
        this.currentUser = currentUser;
    }

    public void add(String userName) {
        if (userName.length() == 0) {
            //
        } else {

            db.fetchAccountInfo(userName, new AccountStateListener() {
                @Override
                public void onFound(Account account) {
                    if (currentUser.compareTo(account) == 0) {
                        // Are you a friend with yourself?
                    } else if (account instanceof User) {
                        if (currentUser.getFriendlist().addFriend((User) account)) {
                            db.updateAccount(currentUser);
                            view.getAppStateListener().onAccountChanged(currentUser);
                            view.refreshView();
                        } else {
                            // might already on the list
                        }

                    } else {
                        // idk, bad thing happened?
                    }
                }

                @Override
                public void onNotFound() {

                }

                @Override
                public void onError(DatabaseError error) {

                }
            });
        }
    }

    public void remove(String userName) {
        if (currentUser.getFriendlist().isFriendWith(userName)) {
            currentUser.getFriendlist().removeFriend(userName);
            db.updateAccount(currentUser);
            view.getAppStateListener().onAccountChanged(currentUser);
            view.refreshView();
        }
    }
}
