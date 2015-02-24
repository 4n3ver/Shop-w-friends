package com.howdoicomputer.android.shoppingwithfriends.model.pojo;

/**
 * Created by Ricardomacias on 2/7/2015.
 */
public class Rating {
    //instance variables
    private int StarCount;
    private int rated;

    /**
     * Constructor for Rating object, initialized an empty
     * rating and amount of rates.
     */
    public Rating() {
        this.StarCount = 0;
        this.rated = 0;
    }

    /**
     * Dynamically calculates rating according to how many
     * other users have rated the profile, and what their
     * rating was.
     *
     * @param starCount:The amount of stars the user is being rated at
     *                      one point in time.
     */
    public void rate(int starCount) {
        rated++;
        this.StarCount = (starCount + this.StarCount) / rated;
    }

    /**
     * getter for the starCount
     *
     * @return StarCount, amount of stars the user has.
     */
    public int getStarCount() {
        return StarCount;
    }
}
