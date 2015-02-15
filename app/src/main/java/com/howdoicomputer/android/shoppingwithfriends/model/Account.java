package com.howdoicomputer.android.shoppingwithfriends.model;

/**
 * @author Ricardo Macias
 * @version %I%, %G%
 */
public class Account implements Comparable<Account> {

    private String name;
    private String usrName;
    private String email;

    public Account() {}

    public Account(String name, String usrName, String email) {
        this.name = name;
        this.usrName = usrName;
        this.email = email;
        confirmEmail();
    }

    public String getUsrName() {
        return usrName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
        confirmEmail();
    }

    public String getName() {
        return name;
    }

    //This expression checks if the account is registered with a valid email
    private void confirmEmail() {
        if (!email.matches("^([A-Za-z][\\w\\-\\.]*)@([A-Za-z][\\w\\-\\.]*)\\.(com|org|net|edu)$")) {
            throw new IllegalArgumentException();
        }
    }

    @Override
    public int compareTo(Account another) {
        return this.usrName.compareTo(another.usrName);
    }
}
