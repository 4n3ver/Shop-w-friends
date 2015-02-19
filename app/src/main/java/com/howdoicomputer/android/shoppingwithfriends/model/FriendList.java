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
        list = new ArrayList<User>();
    }

    /**
     * @param other
     * @return
     */
    public boolean addFriend(User other) {
        int insertionIndex = indexOf(other.getName());
        if (insertionIndex < 0) {
            list.add(-(insertionIndex + 1), other);
            return true;
        } else {
            return false;
        }
    }

    /**
     * Check whether given user name is current user friend.
     *
     * @param userName user name of other user
     * @return <code>true</code> if user name passed is one of current user friend,
     * <code>false</code> otherwise
     */
    public boolean isFriendWith(String userName) {
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getUserName().equals(userName)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Remove user with given user name from the friend list.
     *
     * @param userName user name of user that need to be removed from the list
     * @return <code>true</code> if the list is changed as the result of the operation,
     * <code>false</code> otherwise.
     */
    public boolean removeFriend(String userName) {
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getUserName().equals(userName)) {
                list.remove(i);
                return true;
            }
        }
        return false;
    }

    /**
     * @param index
     * @return
     */
    public User get(int index) {
        return list.get(index);
    }

    /**
     * @return
     */
    public int friendCount() {
        return list.size();
    }

    /**
     * Perform binary search on the friend list.
     *
     * @param name name of user which index is to be found
     * @return see {@link Collections}
     */
    private int indexOf(String name) {
        return Collections.binarySearch(list, new User(name, null, null), new Comparator<User>() {
                    @Override
                    public int compare(User lhs, User rhs) {
                        return lhs.getName().compareTo(rhs.getName());
                    }
                });
    }
}
