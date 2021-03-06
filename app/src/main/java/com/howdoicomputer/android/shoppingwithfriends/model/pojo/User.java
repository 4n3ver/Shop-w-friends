package com.howdoicomputer.android.shoppingwithfriends.model.pojo;

/**
 * Created by Ricardo Macias on 2/7/2015.
 */
public class User extends Account {
    private FriendList friendlist;
    private int        salesReported;
    private int        rating;


    /**
     * Constructor for User object, initialized with desired
     * traits.
     *
     * @param name:       The name of the user
     * @param usrName:The username of the user
     * @param email:      The email of the user
     */
    public User(String name, String usrName, String email) {
        super(name, usrName, email);
        this.friendlist = new FriendList();
        this.salesReported = 0;
        this.rating = 0;
    }

    public int getSalesReported() {
        return salesReported;
    }

    public int getRating() {
        return rating;
    }

    /**
     * Getter for the friendlist of the user
     *
     * @return friendlist
     */
    public FriendList getFriendlist() {
        return friendlist;
    }
}
