package it.itismeucci;

import java.util.HashMap;

public class Users {
    static HashMap<String, MyThread> users = new HashMap<>();

    synchronized static public String verify(String username) {
        if (users.containsKey(username) || (username.toLowerCase().equals("server")) || username.startsWith("*")) {
            return "-";
        } else {
            return "+";
        }
    }

    synchronized static public void remove(String username) {
        users.remove(username);
    }
}
