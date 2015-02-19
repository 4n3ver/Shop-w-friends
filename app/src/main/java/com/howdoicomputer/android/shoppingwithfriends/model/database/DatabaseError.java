package com.howdoicomputer.android.shoppingwithfriends.model.database;

import com.firebase.client.FirebaseError;

/**
 * Created by Yoel Ivan on 2/7/2015.
 */

public class DatabaseError extends FirebaseError {
    public static final int USERNAME_TAKEN     = 0x26262626;
    public static final int USERNAME_NOT_EXIST = 0x62626262;

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
