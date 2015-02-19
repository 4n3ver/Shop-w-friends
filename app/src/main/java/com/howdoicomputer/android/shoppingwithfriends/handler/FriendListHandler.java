package com.howdoicomputer.android.shoppingwithfriends.handler;

import com.howdoicomputer.android.shoppingwithfriends.model.Account;
import com.howdoicomputer.android.shoppingwithfriends.model.User;
import com.howdoicomputer.android.shoppingwithfriends.model.database.AccountStateListener;
import com.howdoicomputer.android.shoppingwithfriends.model.database.Database;
import com.howdoicomputer.android.shoppingwithfriends.model.database.DatabaseError;
import com.howdoicomputer.android.shoppingwithfriends.model.database.FriendListModel;
import com.howdoicomputer.android.shoppingwithfriends.view.MainView;

/**
 * Created by Yoel Ivan on 2/15/2015.
 */
public class FriendListHandler {
    private FriendListModel db;
    private MainView        view;
    private User            currentUser;

    public FriendListHandler(MainView view, User currentUser) {
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
                            view.onAccountChanged(currentUser);
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
            view.onAccountChanged(currentUser);
        }
    }
}
