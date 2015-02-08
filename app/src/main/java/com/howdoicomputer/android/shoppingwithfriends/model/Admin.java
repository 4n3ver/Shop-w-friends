package com.howdoicomputer.android.shoppingwithfriends.model;

/**
 * Created by Ricardomacias on 2/7/2015.
 */
public class Admin extends Account {
    private String usrName;
    private String email;
    private String password;

    public Admin(String usrName, String email, String password) {
        super(usrName, email, password);
    }
}

