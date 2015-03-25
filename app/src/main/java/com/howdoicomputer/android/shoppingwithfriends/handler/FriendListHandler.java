package com.howdoicomputer.android.shoppingwithfriends.handler;

import com.howdoicomputer.android.shoppingwithfriends.R;
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
 * FriendListHandler class
 *
 * @author Yoel Ivan
 */
public class FriendListHandler {
    private final FriendListModel db;
    private final FriendListView  view;

    public FriendListHandler(final FriendListView view) {
        db = Database.getInstance();
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
            view.getUiUtil().showProgressDialog(view.getUiUtil().getString(R.string.searching_for)
                    + userName, view.getUiUtil().getString(R.string.please_wait));
            db.fetchAccountInfo(userName, new FetchAccountResultListener() {
                @Override
                public void onFound(Account account) {
                    view.getUiUtil().hideProgressDialog();
                    if (view.getAppStateListener().getLatestAccount().compareTo(account) == 0) {
                        view.getUiUtil().showErrorDialog(view.getUiUtil().getString(
                                R.string.you_cannot_add_yourself_as_a_friend));
                    } else if (account instanceof User) {
                        if (((User) view.getAppStateListener().getLatestAccount()).getFriendlist()
                                .add((User) account)) {
                            updateAll();
                        } else {
                            view.getUiUtil().showErrorDialog(
                                    account.getName() + view.getUiUtil().getString(
                                            R.string.isAlreadyYourFriend));
                        }
                    } else {
                        view.getUiUtil().showErrorDialog(view.getUiUtil().getString(R.string.WTF));
                    }
                }

                @Override
                public void onNotFound() {
                    view.getUiUtil().hideProgressDialog();
                    view.getUiUtil().showErrorDialog(userName + view.getUiUtil().getString(
                            R.string.notFound));
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
        db.updateAccount(view.getAppStateListener().getLatestAccount());
        view.getAppStateListener().onAccountChanged(view.getAppStateListener().getLatestAccount());
        view.refreshView();
    }
}