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
     */
    public NavigationHandler() {
        db = Database.getInstance();
    }

    /**
     * Contact database to end current session on this client.
     */
    public static void logout() {
        db.logout();
        Database.destroyInstance();
    }
}
