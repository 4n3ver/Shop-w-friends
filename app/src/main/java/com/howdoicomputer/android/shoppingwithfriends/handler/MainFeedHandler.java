package com.howdoicomputer.android.shoppingwithfriends.handler;

import com.howdoicomputer.android.shoppingwithfriends.model.database.Database;
import com.howdoicomputer.android.shoppingwithfriends.model.database.DatabaseError;
import com.howdoicomputer.android.shoppingwithfriends.model.databaseinterface.MainFeedModel;
import com.howdoicomputer.android.shoppingwithfriends.model.pojo.FriendList;
import com.howdoicomputer.android.shoppingwithfriends.model.pojo.Item;
import com.howdoicomputer.android.shoppingwithfriends.model.pojo.User;
import com.howdoicomputer.android.shoppingwithfriends.view.viewinterface.MainFeedView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.TreeSet;

import static com.howdoicomputer.android.shoppingwithfriends.handler.MatchingHandler.matchByPrice;

/**
 * Created by Yoel Ivan on 3/2/2015.
 */
public class MainFeedHandler {
    public static final int  A_WEEK_IN_MILLIS  = 604800000;
    public static final long A_MONTH_IN_MILLIS = 2419200000L;

    private static MainFeedModel db;

    private final List<Item>       dataSet;
    private final MainFeedView     view;
    private       Collection<Item> mFeed;
    private       Collection<Item> currentUserInterest;

    public MainFeedHandler(MainFeedView view) {
        this.view = view;
        db = Database.getInstance();
        mFeed = new TreeSet<>();
        dataSet = new ArrayList<>();
        currentUserInterest = new HashSet<>();
    }

    /* method that handle the adding of item of interest
     * @param itemName - name of the item
     * @param posterUsername - name of the user who post the interest
     * @param price - the threshold price desired by the user
     */
    public void postItemOfInterest(String itemName, String posterUsername, String price) {
        double parsed_price = -1;
        try {
            parsed_price = Double.parseDouble(price);
        } catch (NumberFormatException e) {
            // error
        }
        if (parsed_price >= 0) {
            Item newItem = new Item(itemName, posterUsername, parsed_price,
                    view.getAppStateListener().getLocation()[0],
                    view.getAppStateListener().getLocation()[1], true,
                    view.getAppStateListener().getAddress());
            db.pushItemPost(newItem);
            fetchFeed();
        }
    }

    /*method to fetch the main feed page from data base
     *
     */
    public void fetchFeed() {
        final Date aWeekAgo = new Date(System.currentTimeMillis() - A_WEEK_IN_MILLIS);
        final Date aMonthAgo = new Date(System.currentTimeMillis() - A_MONTH_IN_MILLIS);
        final Collection<Item> interestFeed = new HashSet<>();
        final Collection<Item> reportedFeed = new HashSet<>();
        FriendList userNameList = new FriendList(
                ((User) view.getAppStateListener().getLatestAccount()).getFriendlist());
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
                view.getUiUtil().showErrorDialog(databaseError.getMessage());
            }
        });
    }

    /* Method to start new map activity
     * @param latitude - latitude of the sale item
     * @param longitude - longitude of the sale item
     */
    public void startMapActivity(double latitude, double longitude) {
        view.getAppStateListener().randomMethod(latitude, longitude);
    }

    public List<Item> getDataSet() {
        return dataSet;
    }

    /* method that handle the reporting of item on sale
     * @param itemName - name of item
     * @param posterUsername - name of the user who report sale
     * @param price - price of the item on sale
     */
    public void postItemOfReport(String itemName, String posterUsername, String price) {
        double parsed_price = -1;
        try {
            parsed_price = Double.parseDouble(price);
        } catch (NumberFormatException e) {
            // error
        }
        if (parsed_price >= 0) {
            Item newItem = new Item(itemName, posterUsername, parsed_price,
                    view.getAppStateListener().getLocation()[0],
                    view.getAppStateListener().getLocation()[1], false,
                    view.getAppStateListener().getAddress());
            db.pushItemPost(newItem);
            fetchFeed();
        }
    }
}
