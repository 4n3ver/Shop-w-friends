package com.howdoicomputer.android.shoppingwithfriends.model;

/**
 * Created by Ricardomacias on 2/7/2015.
 */
public class User extends Account implements Friend {
    private FriendList friendlist;
    private int        salesReported;
    private int        rating;


    /**
     * @param name
     * @param usrName
     * @param email
     */
    public User(String name, String usrName, String email) {
        super(name, usrName, email);
        this.friendlist = new FriendList();
        this.salesReported = 0;
        this.rating = 0;
    }

    @Override
    public int getSalesReported() {
        return salesReported;
    }

    @Override
    public int getRating() {
        return rating;
    }

    public FriendList getFriendlist() {
        return friendlist;
    }
}
