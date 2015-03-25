package com.howdoicomputer.android.shoppingwithfriends.model.databaseinterface;

import com.howdoicomputer.android.shoppingwithfriends.model.database.DatabaseError;
import com.howdoicomputer.android.shoppingwithfriends.model.pojo.FriendList;
import com.howdoicomputer.android.shoppingwithfriends.model.pojo.Item;

import java.util.Collection;

/**
 * Created by Yoel Ivan on 3/2/2015.
 */
public interface MainFeedModel extends MainModel {

    public void pushItemPost(Item item);

    public void fetchUserItemPosts(FriendList friendsUsernameList, FeedListener listener);

    public interface FeedListener {

        public void onListFetched(Collection<Item> c);

        void onError(DatabaseError databaseError);
    }
}
