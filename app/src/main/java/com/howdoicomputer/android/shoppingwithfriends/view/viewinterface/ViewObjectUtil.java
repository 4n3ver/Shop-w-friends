package com.howdoicomputer.android.shoppingwithfriends.view.viewinterface;

/**
 * Created by Yoel Ivan on 2/28/2015.
 */
public interface ViewObjectUtil {
    /**
     * Show error dialog to user.
     *
     * @param message error message
     */
    public void showErrorDialog(String message);

    /**
     * Show progress dialog to user
     *
     * @param title   {@link String} of title to be shown to user
     * @param message {@link String} of message to be shown to user
     */
    public void showProgressDialog(String title, String message);

    /**
     * Hide progress dialog if any
     */
    public void hideProgressDialog();

    public String getString(int string_res_id);
}
