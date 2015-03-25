package com.howdoicomputer.android.shoppingwithfriends.handler;

import com.howdoicomputer.android.shoppingwithfriends.R;
import com.howdoicomputer.android.shoppingwithfriends.model.database.Database;
import com.howdoicomputer.android.shoppingwithfriends.model.database.DatabaseError;
import com.howdoicomputer.android.shoppingwithfriends.model.databaseinterface
        .FetchAccountResultListener;
import com.howdoicomputer.android.shoppingwithfriends.model.databaseinterface.FriendListModel;
import com.howdoicomputer.android.shoppingwithfriends.model.pojo.Account;
import com.howdoicomputer.android.shoppingwithfriends.model.pojo.User;
import com.howdoicomputer.android.shoppingwithfriends.view.viewinterface.FriendListView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

/**
 * FriendListHandler class
 *
 * @author Yoel Ivan
 */
public class FriendListHandler {
    private static FriendListModel  db;
    private static Comparator<User> comparatorByName;

    private final FriendListView view;
    private final List<User>     dataSet;

    public FriendListHandler(final FriendListView view) {
        db = Database.getInstance();
        this.view = view;
        dataSet = new ArrayList<>();
        comparatorByName = new Comparator<User>() {
            @Override
            public int compare(User lhs, User rhs) {
                int dName = lhs.getName().compareTo(rhs.getName());
                if (dName == 0) {
                    return lhs.getUserName().compareTo(rhs.getUserName());
                } else {
                    return dName;
                }
            }
        };
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
                                .add(account.getUserName())) {
                            dataSet.add(-(Collections.binarySearch(dataSet, (User) account,
                                    comparatorByName) + 1), (User) account);
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
                    view.getUiUtil().showErrorDialog(error.getMessage());
                }
            });
        }
    }

    public void remove(User other) {
        if (dataSet.remove(Collections.binarySearch(dataSet, other, comparatorByName)) != null) {
            ((User) view.getAppStateListener().getLatestAccount()).getFriendlist().remove(
                    other.getUserName());
            updateAll();
        }
    }

    private void updateAll() {
        db.updateAccount(view.getAppStateListener().getLatestAccount());
        view.refreshView();
    }

    public void fetchFriendList() {
        final Set<User> buffer = new TreeSet<>(comparatorByName);
        for (final String friendUserName : ((User) view.getAppStateListener().getLatestAccount())
                .getFriendlist()) {
            db.fetchAccountInfo(friendUserName, new FetchAccountResultListener() {
                @Override
                public void onFound(Account account) {
                    if (buffer.add((User) account)) {
                        dataSet.clear();
                        dataSet.addAll(buffer);
                        view.refreshView();
                    }
                }

                @Override
                public void onNotFound() {
                    ((User) view.getAppStateListener().getLatestAccount()).getFriendlist().remove(
                            friendUserName);
                    db.updateAccount(view.getAppStateListener().getLatestAccount());
                }

                @Override
                public void onError(DatabaseError error) {
                    view.getUiUtil().showErrorDialog(error.getMessage());
                }
            });
        }
    }

    public List<User> getDataSet() {
        return dataSet;
    }
}