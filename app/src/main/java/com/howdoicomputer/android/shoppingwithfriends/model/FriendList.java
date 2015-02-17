package com.howdoicomputer.android.shoppingwithfriends.model;

/**
 * Created by Ricardomacias on 2/7/2015.
 */

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * Originally planned to be a List of Friend (Interface)
 * Database serialize into JSON object but JSON object does not store info about actual data types,
 * so when database try to deserialize Friend object, it crash since interface cannot be
 * initialized.
 * so I have no idea how to make this as a List of Friend
 * <p/>
 * {@link FriendList} is meant to be a list of friend ordered by their name.
 *
 * @author Yoel Ivan
 * @version 0.5
 */
public class FriendList {
    private ArrayList<User> list;

    /**
     * Construct new {@link FriendList} instance.
     */
    public FriendList() {
        list = new ArrayList<>();
    }

    /**
     * Required public getter for database.
     *
     * @return list of friend
     */
    public ArrayList<User> getList() {
        return list;
    }

    /**
     * @param other
     * @return
     */
    public boolean addFriend(User other) {
        int insertionIndex = 0;
        boolean done = false;
        for (int i = 0; i < list.size() && !done; i++) {
            if (list.get(i).equals(other)) {
                return false;
            } else if (list.get(i).getName().compareTo(other.getName()) >= 0) {
                insertionIndex = i;
                done = true;
            }
        }
        list.add(insertionIndex, other);
        return true;
    }

    /**
     * Check whether given user name is current user friend.
     *
     * @param userName user name of other user
     * @return <code>true</code> if user name passed is one of current user friend,
     * <code>false</code> otherwise
     */
    public boolean isFriendWith(String userName) {
        return indexOf(userName) >= 0;
    }

    /**
     * Remove user with given user name from the friend list.
     *
     * @param userName user name of user that need to be removed from the list
     */
    public void removeFriend(String userName) {
        list.remove(indexOf(userName));
    }

    /**
     * Perform binary search on the friend list.
     *
     * @param userName user name which index is to be found
     * @return see {@link Collections}
     */
    private int indexOf(String userName) {
        return Collections.binarySearch(list, new User(null, userName, null),
                new Comparator<User>() {
                    @Override
                    public int compare(User lhs, User rhs) {
                        return lhs.getName().compareTo(rhs.getName());
                    }
                });
    }
}
