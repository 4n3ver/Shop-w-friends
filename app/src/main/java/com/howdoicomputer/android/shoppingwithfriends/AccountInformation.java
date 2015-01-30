package com.howdoicomputer.android.shoppingwithfriends;

/**
 * Created by Yoel Ivan on 1/30/2015.
 */
public class AccountInformation {
    private String uid;
    private String email;
    private String password;

    public AccountInformation(String uid, String email, String password) {
        this.uid = uid;
        this.email = email;
        this.password = password;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    String getEmail() {
        return email;
    }

    public void setEmail(String email) {

        this.email = email;
    }

    String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}
