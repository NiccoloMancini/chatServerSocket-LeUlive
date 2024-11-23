package it.itismeucci;

import java.util.HashMap;

public class Users {
    HashMap<String, MyThread> users;

    public Users() {
        this.users = new HashMap<String, MyThread>();
    }

    synchronized public String verify(String username) {
        if (this.users.containsKey(username) || (username.toLowerCase().equals("server")) || username.startsWith("*")) {
            return "-";
        } else {
            return "+";
        }
    }

    synchronized public void remove(String username) {
        this.users.remove(username);
    }
}
