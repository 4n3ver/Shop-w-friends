package com.howdoicomputer.android.shoppingwithfriends.model;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/**
 * Created by Yoel Ivan on 2/6/2015. In Progress I guess?
 *
 * @author Yoel Ivan
 * @version %I%, %G%
 */
public class Account implements Serializable {
    private String usrName;
    private String email;

    public Account(String usrName, String email) {
        this.usrName = usrName; this.email = email;
        //confirmEmail();
    }

    public static void writeToFile(Account account) throws IOException {
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(
                new FileOutputStream("Account.bin")); objectOutputStream.writeObject(account);
    }

    public static void readFile() throws IOException, ClassNotFoundException {
        ObjectInputStream objectInputStream = new ObjectInputStream(
                new FileInputStream(("Account.bin")));
        Account account = (Account) objectInputStream.readObject();
    }

    public void setEmail(String email) {
        this.email = email; confirmEmail();
    }

    //This expression checks if the account is registered with a valid email
    private void confirmEmail() {
        if (!email.matches("^([A-Za-z][\\w\\-\\.]*)@([A-Za-z][\\w\\-\\.]*)\\.(com|org|net|edu)$")) {
            throw new IllegalArgumentException();
        }
    }
}
