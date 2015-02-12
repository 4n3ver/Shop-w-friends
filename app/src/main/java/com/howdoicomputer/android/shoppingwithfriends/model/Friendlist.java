package com.howdoicomputer.android.shoppingwithfriends.model;

import java.util.Collection;
import java.util.TreeSet;

/**
 * Created by Ricardomacias on 2/7/2015.
 */
public class Friendlist {
    Collection<User> friends = new TreeSet<>();
    public void addFriend(User user) {
        friends.add(user);
    }

    public Collection<User> getFriends() {
        return friends;
    }
}
