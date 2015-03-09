package com.howdoicomputer.android.shoppingwithfriends.handler;

import com.howdoicomputer.android.shoppingwithfriends.model.pojo.Item;

import java.util.Collection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.HashSet;
import java.util.Date;

/**
 * MatchingHandler class
 * This class provides methods to filter a collection
 * Created by lovissa winyoto on 3/9/15.
 */
public class MatchingHandler {

    /**
     * This method compares items in a collection to an item and see
     * if there is any item(s) in the collection that matches the compared item
     *
     * @param collection a collection with items to be compared
     * @param compare    an item to be compared
     * @return a collection containing the item(s) that matches the compared item
     */
    public static Collection<Item> match(Collection<Item> collection, Item compare) {
        Collection<Item> toReturn = new HashSet<>();
        Pattern pattern = Pattern.compile(compare.getItemName().toLowerCase());
        for (Item each : collection) {
            Matcher matcher = pattern.matcher(each.getItemName().toLowerCase());
            if (matcher.find()) {
                toReturn.add(each);
            }
        }
        return toReturn;
    }

    public static Collection<Item> matchByPrice(Collection<Item> collection, Item compare) {
        Collection<Item> toReturn = MatchingHandler.match(collection, compare);
        for (Item each : toReturn) {
            if (each.getPrice() > compare.getPrice()) {
                toReturn.remove(each);
            }
        }
        return toReturn;
    }

    public static Collection<Item> matchByDate(Collection<Item> collection, Date compare) {
        for (Item each : collection) {
            if (each.getDate().compareTo(compare) < 0) {
                collection.remove(each);
            }
        }
        return collection;
    }

    //TODO: cut "and" "the" (30 words)
    //TODO: compare a list to a list
}
