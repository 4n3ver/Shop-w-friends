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
public class Admin extends Account implements Serializable {
    private String usrName;
    private String email;

    public Admin(String usrName, String email) {
        super(usrName, email);
    }

    public static void writeToFile(Admin account) throws IOException {
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(
                new FileOutputStream("Admin.bin")); objectOutputStream.writeObject(account);
    }

    public static void readFile() throws IOException, ClassNotFoundException {
        ObjectInputStream objectInputStream = new ObjectInputStream(
                new FileInputStream(("Admin.bin")));
        Admin account = (Admin) objectInputStream.readObject();
    }
}

