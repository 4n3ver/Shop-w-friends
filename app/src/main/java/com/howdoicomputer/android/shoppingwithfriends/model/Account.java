package com.howdoicomputer.android.shoppingwithfriends.model;

import java.io.*;

/**
 * Created by Yoel Ivan on 2/6/2015. In Progress I guess?
 *
 * @author Yoel Ivan
 * @version %I%, %G%
 */
public class Account implements Serializable {
    private String usrName;
    private String email;
    private String password;

    public Account(String usrName, String email, String password) {
        this.usrName = usrName;
        this.email = email;
        this.password = password;
        confirmEmail();
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setEmail(String email) {
        this.email = email;
        confirmEmail();
    }

    //This expression checks if the account is registered with a valid email
    private void confirmEmail() {
        if (!email.matches("^([A-Za-z][\\w\\-\\.]*)@([A-Za-z][\\w\\-\\.]*)\\.(com|org|net|edu)$")) {
            throw new IllegalArgumentException();
        }
    }

    public static void writeToFile (Account account) throws IOException {
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream("Account.bin"));
            objectOutputStream.writeObject(account);
    }

    public static void readFile() throws IOException, ClassNotFoundException {
        ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream(("Account.bin")));
        Account account = (Account) objectInputStream.readObject();
    }
}
