package com.howdoicomputer.android.shoppingwithfriends.model;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/**
 * Created by Ricardomacias on 2/7/2015.
 */
public class Admin extends Account {
    /**
     * Construct new {@link Admin} instance.
     */
    public Admin(String name, String usrName, String email) {
        super(name, usrName, email);
    }

}

