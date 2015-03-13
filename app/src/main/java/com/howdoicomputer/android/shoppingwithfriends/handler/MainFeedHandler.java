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
import java.util.LinkedList;
import java.util.List;
import java.util.TreeSet;

import static com.howdoicomputer.android.shoppingwithfriends.handler.MatchingHandler.matchByPrice;

/**
 * Created by Yoel Ivan on 3/2/2015.
 */
public class MainFeedHandler {
    private MainFeedModel    db;
    private MainFeedView     view;
    private Collection<Item> mFeed;
    private List<Item>       dataSet;
    private Collection<Item> currentUserInterest;

    public MainFeedHandler(MainFeedView view) {
        this.view = view;
        db = Database.getInstace();
        mFeed = new TreeSet<>();
        dataSet = new ArrayList<>();
        currentUserInterest = new HashSet<>();
    }

    public void postItemOfInterest(String itemName, String posterUsername, String price) {
        double parsed_price = -1;
        try {
            parsed_price = Double.parseDouble(price);
        } catch (NumberFormatException e) {
            // error
        }
        if (parsed_price >= 0) {
            //            Location loc = AppActivity.getmLastLocation();
            //            List<Address> possibilities = null;
            //            try {
            //                possibilities = AppActivity.getGeoCoder().getFromLocation(loc
            // .getLatitude(),
            //                        loc.getLongitude(), 3);
            //            } catch (IOException idc) {
            //
            //            }
            //            Item newItem = new Item(itemName, posterUsername, parsed_price,
            // loc.getLatitude(),
            //                    loc.getLongitude(), loc.getAltitude(), true,
            // possibilities.get(0).toString());
            Item newItem = new Item(itemName, posterUsername, parsed_price, 0, 0, 0, true, null);
            db.pushItemPost(newItem);
            fetchFeed();
        }
    }

    public void fetchFeed() {
        final Date aWeekAgo = new Date(System.currentTimeMillis() - 604800000);
        final Date aMonthAgo = new Date(System.currentTimeMillis() - 2419200000L);
        final Collection<Item> interestFeed = new HashSet<>();
        final Collection<Item> reportedFeed = new HashSet<>();
        List<String> userNameList = new LinkedList<>(
                ((User) view.getAppStateListener().getLatestAccount()).getFriendlist()
                        .getFriendsUserName());
        userNameList.add(view.getAppStateListener().getLatestAccount().getUserName());
        db.fetchUserItemPosts(userNameList, new MainFeedModel.FeedListener() {
            @Override
            public void onListFetched(Collection<Item> c) {
                for (Item post : c) {
                    if (post.isInterest()) {
                        if (post.getPosterUserName().equals(
                                view.getAppStateListener().getLatestAccount().getUserName())) {
                            currentUserInterest.add(post);
                        }
                        if (post.getDate().compareTo(aWeekAgo) >= 0) {
                            interestFeed.add(post);
                        }
                    } else {
                        if (post.getDate().compareTo(aMonthAgo) >= 0) {
                            if (post.getPosterUserName().equals(
                                    view.getAppStateListener().getLatestAccount().getUserName())) {
                                mFeed.add(post);
                                dataSet.clear();
                                dataSet.addAll(mFeed);
                                view.refreshView();
                            } else {
                                reportedFeed.add(post);
                            }
                        }
                    }
                }

                for (Item interest : currentUserInterest) {
                    if (mFeed.addAll(matchByPrice(reportedFeed, interest))) {
                        dataSet.clear();
                        dataSet.addAll(mFeed);
                        view.refreshView();
                    }
                }

                if (mFeed.addAll(interestFeed)) {
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

    public void postItemOfReport(String itemName, String posterUsername, String price) {
        double parsed_price = -1;
        try {
            parsed_price = Double.parseDouble(price);
        } catch (NumberFormatException e) {
            // error
        }
        if (parsed_price >= 0) {
            //            Location loc = AppActivity.getmLastLocation();
            //            List<Address> possibilities = null;
            //            try {
            //                possibilities = AppActivity.getGeoCoder().getFromLocation(loc
            // .getLatitude(),
            //                        loc.getLongitude(), 3);
            //            } catch (IOException idc) {
            //
            //            }
            //            Item newItem = new Item(itemName, posterUsername, parsed_price,
            // loc.getLatitude(),
            //                    loc.getLongitude(), loc.getAltitude(), false,
            // possibilities.get(0).toString());
            Item newItem = new Item(itemName, posterUsername, parsed_price, 0, 0, 0, false, null);
            db.pushItemPost(newItem);
            fetchFeed();
        }
    }
}
