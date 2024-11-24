package it.itismeucci;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.Set;

public class MyThread extends Thread {
    private Socket s;
    private BufferedReader in;
    private DataOutputStream out;

    public MyThread(Socket s) {
        this.s = s;
        try {
            this.in = new BufferedReader(new InputStreamReader(s.getInputStream()));
            this.out = new DataOutputStream(s.getOutputStream());
        } catch (IOException e) {
            System.out.println("Errore!!");
        }
    }

    public void sendMessage(String msg) throws IOException {
        this.out.writeBytes(msg + "\n");
    }

    public void run() {
        try {
            String username = "";
            String message;
            String receiver;
            do {
                receiver = in.readLine();
                System.out.println(receiver);
                message = in.readLine(); // messaggio da cui capire il tipo di azione da eseguire
                System.out.println(message);
                switch (receiver) {
                    case "server":
                        switch (message) {
                            case "/!": // disconnessione client
                                Users.users.remove(username, this);
                                Set<String> keys = Users.users.keySet();
                                for (String key : keys) {
                                    Users.users.get(key).sendMessage("server");
                                    Users.users.get(key).sendMessage("#-");
                                    Users.users.get(key).sendMessage(username);
                                }
                                break;
                            case "/+": // registrazione client
                                String result = "";
                                do {
                                    if (result.equals("-")) {
                                        receiver = in.readLine();
                                        message = in.readLine();
                                    }
                                    username = in.readLine();
                                    result = Users.verify(username);
                                    sendMessage(result);
                                } while (result.equals("-"));
                                keys = Users.users.keySet();
                                for (String key : keys) {
                                    Users.users.get(key).sendMessage("server");
                                    Users.users.get(key).sendMessage("#+");
                                    Users.users.get(key).sendMessage(username);
                                }
                                Users.users.put(username, this);
                                break;
                        }
                        break;
                    case "*": // comunicazione globale
                        Set<String> keys = Users.users.keySet();
                        for (String key : keys) {
                            if (!key.equals(username)) {
                                Users.users.get(key).sendMessage("*" + username);
                                Users.users.get(key).sendMessage(message);
                            }
                        }
                        break;
                    default: // comunicazione privata tra due client
                        if (Users.users.containsKey(receiver)) {
                            Users.users.get(receiver).sendMessage(username);
                            Users.users.get(receiver).sendMessage(message);
                        } else {
                            sendMessage("server");
                            sendMessage("#!");
                            sendMessage(receiver);
                        }
                }
            } while (!message.equals("/!"));
            s.close();
        } catch (IOException e) {
            System.out.println("Errore!!");
        }
    }
}
