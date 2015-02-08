package com.howdoicomputer.android.shoppingwithfriends.model;

import com.firebase.client.FirebaseError;

/**
 * Created by Yoel Ivan on 2/7/2015.
 */

public class DatabaseError extends FirebaseError {

    public DatabaseError(int code, String message) {
        super(code, message);
    }

    public DatabaseError(int code, String message, String details) {
        super(code, message, details);
    }

    public DatabaseError(FirebaseError error) {
        super(error.getCode(), error.getMessage(), error.getDetails());
    }
}
