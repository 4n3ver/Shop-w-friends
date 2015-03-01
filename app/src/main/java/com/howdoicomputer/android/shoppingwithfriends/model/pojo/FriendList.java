package com.howdoicomputer.android.shoppingwithfriends.model.pojo;

/**
 * Created by Ricardomacias on 2/7/2015.
 */

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;

/**
 * {@link FriendList} is meant to be a list of friend's user name.
 *
 * @author Yoel Ivan
 * @version 2.0
 */
public class FriendList implements Iterable<User> {
    private ArrayList<User> list;

    /**
     * Construct new {@link FriendList} instance.
     */
    public FriendList() {
        list = new ArrayList<User>();
    }


    public boolean add(User other) {
        int insertionIndex = indexOf(other);
        if (insertionIndex < 0) {
            list.add(-(insertionIndex + 1), other);
            return true;
        } else {
            list.set(insertionIndex, other);
            return false;
        }
    }

    public boolean isFriendWith(User other) {
        return indexOf(other) >= 0 ? true : false;
    }

    public boolean remove(User other) {
        int removalIndex = indexOf(other);
        if (removalIndex < 0) {
            return false;
        } else {
            list.remove(removalIndex);
            return true;
        }
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
     * @param friend <code>User</code> object which index is to be found
     * @return see {@link Collections}
     */
    private int indexOf(User friend) {
        return Collections.binarySearch(list, friend, new Comparator<User>() {
            @Override
            public int compare(User lhs, User rhs) {
                int dName = lhs.getName().compareTo(rhs.getName());
                if (dName == 0) {
                    return lhs.getUserName().compareTo(rhs.getUserName());
                } else {
                    return dName;
                }
            }
        });
    }

    @Override
    public Iterator<User> iterator() {
        return list.iterator();
    }
}
