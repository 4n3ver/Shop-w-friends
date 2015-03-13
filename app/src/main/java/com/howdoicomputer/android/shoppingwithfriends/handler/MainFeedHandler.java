package com.howdoicomputer.android.shoppingwithfriends.handler;

import com.howdoicomputer.android.shoppingwithfriends.model.database.Database;
import com.howdoicomputer.android.shoppingwithfriends.model.database.DatabaseError;
import com.howdoicomputer.android.shoppingwithfriends.model.databaseinterface.MainFeedModel;
import com.howdoicomputer.android.shoppingwithfriends.model.pojo.Item;
import com.howdoicomputer.android.shoppingwithfriends.model.pojo.User;
import com.howdoicomputer.android.shoppingwithfriends.view.viewinterface.MainFeedView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.TreeSet;

/**
 * Created by Yoel Ivan on 3/2/2015.
 */
public class MainFeedHandler {
    private MainFeedModel   db;
    private MainFeedView    view;
    private Collection<Item> mFeed;
    private List<Item>       dataSet;

    public MainFeedHandler(MainFeedView view) {
        this.view = view;
        db = Database.getInstace();
        mFeed = new TreeSet<>();
        dataSet = new ArrayList<>();
    }

    public void postItemOfInterest(String itemName, String posterUsername, String price) {
        double parsed_price = -1;
        try {
            parsed_price = Double.parseDouble(price);
        } catch (NumberFormatException e) {
            // error
        }
        if (parsed_price >= 0) {
            Item newItem = new Item(itemName, posterUsername, parsed_price, null, true);
            db.pushItemPost(newItem);
            fetchFeed();
        }
    }

    public void postReportedItem(String itemName, String posterUsername, String price) {
        double parsed_price = -1;
        try {
            parsed_price = Double.parseDouble(price);
        } catch (NumberFormatException e) {
            // error
        }
        if (parsed_price >= 0) {
            Item newItem = new Item(itemName, posterUsername, parsed_price, null, false);
            db.pushItemPost(newItem);
            fetchFeed();
        }
    }

    public void fetchFeed() {
        final Date aWeekAgo = new Date(System.currentTimeMillis() - 604800000);
        final Collection<Item> feed = new HashSet<>();
        List<String> friendUserName = ((User) view.getAppStateListener().getLatestAccount())
                .getFriendlist().getFriendsUserName();
        friendUserName.add(view.getAppStateListener().getLatestAccount().getUserName());
        db.fetchFriendsItemOfInterest(friendUserName, new MainFeedModel.FeedListener() {
            @Override
            public void onListFetched(Collection<Item> c) {
                for (Item post : c) {
                    if (post.getDate().compareTo(aWeekAgo) >= 0) {
                        feed.add(post);
                    }
                }

                if (mFeed.addAll(feed)) {
                    dataSet.clear();
                    dataSet.addAll(mFeed);
                    view.refreshView();
                }
            }

            @Override
            public void onError(DatabaseError databaseError) {

            }
        });
    }

    public List<Item> getDataSet() {
        return dataSet;
    }

    public void postItemOfReport(String s, String userName, String s1) {}
}
