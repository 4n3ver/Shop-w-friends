package com.howdoicomputer.android.shoppingwithfriends.model.databaseinterface;

import com.howdoicomputer.android.shoppingwithfriends.model.pojo.Item;

/**
 * Created by Yoel Ivan on 3/2/2015.
 */
public interface MainFeedModel extends MainModel {

    public void pushItemPost(Item item);
}
