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
public class User extends Account implements Serializable {
    private String usrName;
    private String email;
    private Friendlist friendlist;

    public User(String usrName, String email) {
        super(usrName, email);
        this.friendlist = new Friendlist();
    }

    public static void writeToFile (User account) throws IOException {
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream("User.bin"));
        objectOutputStream.writeObject(account);
    }

    public static void readFile() throws IOException, ClassNotFoundException {
        ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream(("User.bin")));
        User account = (User) objectInputStream.readObject();
    }

}
