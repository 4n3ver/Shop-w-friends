package com.howdoicomputer.android.shoppingwithfriends.model;

/**
 * Created by Ricardomacias on 2/7/2015.
 */
public class User extends Account {
    private String usrName;
    private String email;
    private String password;
    private Friendlist friendlist;

    public User(String usrName, String email, String password) {
        super(usrName, email, password);
        this.friendlist = new Friendlist();
    }

}
