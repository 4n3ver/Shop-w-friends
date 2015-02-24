package com.howdoicomputer.android.shoppingwithfriends.model;

import android.location.Location;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Ricardomacias on 2/7/2015.
 * Work in Progress
 */
public class Item implements Comparable {
    private int likes;
    private double price;
    private Location location;
    private String time;
    private Date date;

    /**
     * Contructor for the item class, automatically gives time and date
     * it was created.
     * @param price: price of the item
     * @param loc: location of the item
     */
    public Item(double price, Location loc) {
        this.likes = 0;
        this.price = price;
        location = loc;
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        date = new Date();
        time = dateFormat.format(date);
    }

    /**
     * Getter for the current location of the item.
     * @return location
     */
    public Location getLocation() {
        return location;
    }

    /**
     * Getter for the price of the item
     * @return price
     */
    public double getPrice() {
        return price;
    }

    /**
     * Modifies price to the new value.
     */
    public void setPrice(double price) {
        this.price = price;
    }

    /**
     * Getter for the likes of the item.
     * @return likes, the amount of current likes the product has
     */
    public int getLikes() {
        return likes;
    }

    /**
     * Increments likes of the item.
     */
    public void incrementLikes() {
        likes++;
    }

    /**
     * getter for the date this item was created
     * @return date, the date this item was created
     */
    public Date getDate() {
        return date;
    }

    /**
     * getter for the date & time this item was created,
     * in string format.
     * @return time
     */
    public String getTime() {
        return time;
    }
    @Override
    public int compareTo(Object another) {
        return date.compareTo(((Item)another).getDate());
    }
}
