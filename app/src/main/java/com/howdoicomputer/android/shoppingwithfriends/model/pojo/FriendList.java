package com.howdoicomputer.android.shoppingwithfriends.model.pojo;

/**
 * Created by Ricardomacias on 2/7/2015.
 */

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

/**
 * {@link FriendList} is meant to be a list of friend's user name.
 *
 * @author Yoel Ivan
 * @version 2.0
 */
public class FriendList implements Iterable<String> {
    private ArrayList<String> list;

    /**
     * Construct new {@link FriendList} instance.
     */
    public FriendList() {
        list = new ArrayList<String>();
    }

    public FriendList(FriendList other) {
        this.list = new ArrayList<>(other.list);
    }

    public boolean add(String otherUserName) {
        int insertionIndex = indexOf(otherUserName);
        if (insertionIndex < 0) {
            list.add(-(insertionIndex + 1), otherUserName);
            return true;
        } else {
            list.set(insertionIndex, otherUserName);
            return false;
        }
    }

    public boolean isFriendWith(String otherUserName) {
        return indexOf(otherUserName) >= 0;
    }

    public boolean remove(String otherUserName) {
        int removalIndex = indexOf(otherUserName);
        if (removalIndex < 0) {
            return false;
        } else {
            list.remove(removalIndex);
            return true;
        }
    }

    /**
     * @param index the index in the list
     * @return the user in that index
     */
    public String get(int index) {
        return list.get(index);
    }

    /**
     * counts the number of friends
     *
     * @return the number of friends
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
    private int indexOf(String friend) {
        return Collections.binarySearch(list, friend);
    }

    @Override
    public Iterator<String> iterator() {
        return list.iterator();
    }
}
