package com.howdoicomputer.android.shoppingwithfriends.model.pojo;

/**
 * @author Ricardo Macias
 * @author Yoel Ivan
 * @version %I%, %G%
 */

public class Account implements Comparable<Account> {

    private String name;
    private String userName;
    private String email;

    public Account(String name, String userName, String email) {
        this.name = name;
        this.userName = userName;
        this.email = email;
        //confirmEmail();
    }

    public String getUserName() {
        return userName;
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
        return this.userName.compareTo(another.userName);
    }

    @Override
    public boolean equals(Object other) {

        /* this was intended to compare Account based on their username */
        if (other instanceof Account) {
            return this.userName.equals(((Account) other).userName);
        }
        throw new IllegalArgumentException("Not instance of Account");
    }
}
