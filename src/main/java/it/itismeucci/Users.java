package it.itismeucci;

import java.util.ArrayList;

public class Users {
    ArrayList<String> users;

    public Users() {
        this.users = new ArrayList<String>();
    }

    synchronized public String verify(String username) {
        if (this.users.contains(username)) {
            return "-";
        } else {
            this.users.add(username);
            return "+";
        }
    }

    synchronized public void remove(String username) {
        this.users.remove(username);
    }
}
