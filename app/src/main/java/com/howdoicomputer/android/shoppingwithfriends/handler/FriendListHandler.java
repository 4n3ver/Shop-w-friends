package com.howdoicomputer.android.shoppingwithfriends.handler;

import com.howdoicomputer.android.shoppingwithfriends.model.database.Database;
import com.howdoicomputer.android.shoppingwithfriends.model.database.DatabaseError;
import com.howdoicomputer.android.shoppingwithfriends.model.databaseinterface.AccountStateListener;
import com.howdoicomputer.android.shoppingwithfriends.model.databaseinterface
        .FetchAccountResultListener;
import com.howdoicomputer.android.shoppingwithfriends.model.databaseinterface.FriendListModel;
import com.howdoicomputer.android.shoppingwithfriends.model.pojo.Account;
import com.howdoicomputer.android.shoppingwithfriends.model.pojo.User;
import com.howdoicomputer.android.shoppingwithfriends.view.viewinterface.FriendListView;

/**
 *
 */
public class FriendListHandler {
    private FriendListModel db;
    private FriendListView  view;

    public FriendListHandler(final FriendListView view) {
        db = Database.getInstace();
        this.view = view;
        db.fetchFriendAccountInfo(
                ((User) view.getAppStateListener().getLatestAccount()).getFriendlist(),
                new AccountStateListener() {
                    @Override
            public void onError(DatabaseError databaseError) {
                view.getUiUtil().showErrorDialog(databaseError.toString());
            }

            @Override
            public void onAccountChanged(Account acc) {
                ((User) view.getAppStateListener().getLatestAccount()).getFriendlist().add(
                        (User) acc);
                updateAll();
            }
        });
    }

    public void add(final String userName) {
        if (userName.length() == 0) {
            // ignore if no input
        } else {
            view.getUiUtil().showProgressDialog("Searching for " + userName, "Please wait");
            db.fetchAccountInfo(userName, new FetchAccountResultListener() {
                @Override
                public void onFound(Account account) {
                    view.getUiUtil().hideProgressDialog();
                    if (((User) view.getAppStateListener().getLatestAccount()).compareTo(account)
                            == 0) {
                        view.getUiUtil().showErrorDialog("You can't be friend with yourself");
                    } else if (account instanceof User) {
                        if (((User) view.getAppStateListener().getLatestAccount()).getFriendlist()
                                .add((User) account)) {
                            updateAll();
                        } else {
                            view.getUiUtil().showErrorDialog(
                                    account.getName() + " is already your friend");
                        }
                    } else {
                        view.getUiUtil().showErrorDialog("WOAH!!! WHAT HAPPENED?!!?");
                    }
                }

                @Override
                public void onNotFound() {
                    view.getUiUtil().hideProgressDialog();
                    view.getUiUtil().showErrorDialog(userName + " not found");
                }

                @Override
                public void onError(DatabaseError error) {
                    view.getUiUtil().hideProgressDialog();
                    view.getUiUtil().showErrorDialog(error.toString());
                }
            });
        }
    }

    public void remove(User other) {
        if (((User) view.getAppStateListener().getLatestAccount()).getFriendlist().isFriendWith(
                other)) {
            ((User) view.getAppStateListener().getLatestAccount()).getFriendlist().remove(other);
            updateAll();
        }
    }

    private void updateAll() {
        db.updateAccount(((User) view.getAppStateListener().getLatestAccount()));
        view.getAppStateListener().onAccountChanged(
                ((User) view.getAppStateListener().getLatestAccount()));
        view.refreshView();
    }
}
