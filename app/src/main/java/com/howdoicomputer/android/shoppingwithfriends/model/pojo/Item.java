package com.howdoicomputer.android.shoppingwithfriends.model.pojo;


import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Ricardomacias on 2/7/2015.
 * Work in Progress
 *
 * @author Ricardo Macias
 * @author Yoel Ivan
 */
public class Item implements Comparable<Item> {
    private String  posterUserName;
    private boolean isInterest;
    private String  itemName;

    //instance variables
    private int    likes;
    private double price;
    private String time;
    private Date   date;
    private double latitude;
    private double longitude;
    private String address;

    /**
     * Contructor for the item class, automatically gives time and date
     * it was created.
     *
     * @param price: price of the item
     */
    public Item(String itemName, String opUserName, double price, double latitude, double longitude,
            boolean isInterest, String address) {
        this.itemName = itemName;
        this.posterUserName = opUserName;
        this.likes = 0;
        this.price = price;
        this.latitude = latitude;
        this.longitude = longitude;
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        date = new Date();
        time = dateFormat.format(date);
        this.isInterest = isInterest;
        this.address = address;
    }

    /**
     * Getter for the current latitude of the item.
     *
     * @return latitude
     */
    public double getLatitude() {
        return latitude;
    }

    /**
     * Getter for the current longitude of the item.
     *
     * @return longitude
     */
    public double getLongitude() {
        return longitude;
    }

    /**
     * Getter for the price of the item
     *
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
     *
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
     *
     * @return date, the date this item was created
     */
    public Date getDate() {
        return date;
    }

    /**
     * getter for the date & time this item was created,
     * in string format.
     *
     * @return time
     */
    public String getTime() {
        return time;
    }

    public String getPosterUserName() {
        return posterUserName;
    }

    public boolean isInterest() {
        return isInterest;
    }

    public String getItemName() {
        return itemName;
    }

    @Override
    public int compareTo(Item another) {
        return another.getDate().compareTo(this.getDate());
    }

    public String getAddress() {
        return address;
    }
}
