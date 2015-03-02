package com.howdoicomputer.android.shoppingwithfriends.handler;

import com.howdoicomputer.android.shoppingwithfriends.model.database.Database;
import com.howdoicomputer.android.shoppingwithfriends.model.databaseinterface.MainModel;

/**
 * {@link NavigationHandler} handles event from main app GUI and process related data from the
 * model.
 *
 * @author Yoel Ivan
 * @version %I%, %G%
 */
public class NavigationHandler {
    private static MainModel db;

    /**
     * Construct {@link NavigationHandler} object.
     *
     * @param view reference to the implementing GUI
     */
    public NavigationHandler() {
        db = Database.getInstace();
    }

    /**
     * Contact database to end current session on this client.
     */
    public void logout() {
        db.logout();
        Database.destroyInstance();
    }
}
