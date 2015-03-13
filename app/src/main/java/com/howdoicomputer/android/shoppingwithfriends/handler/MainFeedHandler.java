package com.howdoicomputer.android.shoppingwithfriends.handler;

import android.location.Address;
import android.location.Location;
import com.howdoicomputer.android.shoppingwithfriends.model.database.Database;
import com.howdoicomputer.android.shoppingwithfriends.model.database.DatabaseError;
import com.howdoicomputer.android.shoppingwithfriends.model.databaseinterface.MainFeedModel;
import com.howdoicomputer.android.shoppingwithfriends.model.pojo.Item;
import com.howdoicomputer.android.shoppingwithfriends.model.pojo.User;
import com.howdoicomputer.android.shoppingwithfriends.view.act.AppActivity;
import com.howdoicomputer.android.shoppingwithfriends.view.viewinterface.MainFeedView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

/**
 * Created by Yoel Ivan on 3/2/2015.
 */
public class MainFeedHandler {
    private MainFeedModel   db;
    private MainFeedView    view;
    private Set<Item>       mFeed;
    private ArrayList<Item> dataSet;

    public MainFeedHandler(MainFeedView view) {
        this.view = view;
        db = Database.getInstace();
        mFeed = new TreeSet<>();
        dataSet = new ArrayList<>();
    }

    public void postItemOfInterest(String itemName, String posterUsername, double price) throws IOException {
        Location loc = AppActivity.getmLastLocation();
        List<Address> possibilities = AppActivity.getGeoCoder().getFromLocation(loc.getLatitude(), loc.getLongitude(), 3);
        Item newItem = new Item(itemName, posterUsername, price, loc.getLatitude(), loc.getLongitude(), loc.getAltitude()
        , true, possibilities.get(0).toString());
        db.pushItemPost(newItem);
        fetchFeed();
    }

    public void fetchFeed() {
        final Date aWeekAgo = new Date(System.currentTimeMillis() - 604800000);
        final Set<Item> feed = new TreeSet<>();
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

    public ArrayList<Item> getDataSet() {
        return dataSet;
    }
}
