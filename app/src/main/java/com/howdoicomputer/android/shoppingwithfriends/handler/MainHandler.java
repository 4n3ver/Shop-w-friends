package com.howdoicomputer.android.shoppingwithfriends.handler;

import com.howdoicomputer.android.shoppingwithfriends.model.database.Database;
import com.howdoicomputer.android.shoppingwithfriends.model.databaseinterface.MainModel;
import com.howdoicomputer.android.shoppingwithfriends.view.viewinterface.MainView;

/**
 * {@link MainHandler} handles event from main app GUI and process related data from the model.
 *
 * @author Yoel Ivan
 * @version %I%, %G%
 */
public class MainHandler {
    private static MainModel db;
    private        MainView  view;

    /**
     * Construct {@link MainHandler} object.
     *
     * @param view reference to the implementing GUI
     */
    public MainHandler(MainView view) {
        db = Database.getInstace();
        this.view = view;
    }

    /**
     * Contact database to end current session on this client.
     */
    public void logout() {
        db.logout();
        view.onLoggedOut();

    }
}
