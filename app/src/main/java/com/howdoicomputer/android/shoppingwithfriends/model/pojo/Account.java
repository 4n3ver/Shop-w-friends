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
    /**
     * Construct new {@link Account} instance.
     */
    public Account(String name, String userName, String email) {
        this.name = name;
        this.userName = userName;
        this.email = email;
    }

    /**
     * Getter that returns the username of the account.
     * @return userName
     */
    public String getUserName() {
        return userName;
    }

    /**
     * Getter that returns the email of the account
     * @return email
     */
    public String getEmail() {
        return email;
    }

    /**
     * Setter that modifies the email of the account
     * @param email: The new desired email
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Getter that returns the name of the Account Holder
     * @return name
     */
    public String getName() {
        return name;
    }

    @Override
    public int compareTo(Account another) {
        return userName.compareTo(another.getUserName());
    }

    @Override
    public boolean equals(Object other) {

        /* this was intended to compare Account based on their username */
        if (other instanceof Account) {
            return this.userName.equals(((Account) other).getUserName());
        } else {
            throw new IllegalArgumentException("Not instance of Account");
        }
    }
}
