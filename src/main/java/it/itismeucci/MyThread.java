package it.itismeucci;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.Set;

public class MyThread extends Thread {
    private Socket s;
    private Users u;
    private BufferedReader in;
    private DataOutputStream out;

    public MyThread(Socket s, Users u) {
        this.s = s;
        this.u = u;
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
                                u.users.remove(username, this);
                                Set<String> keys = u.users.keySet();
                                for (String key : keys) {
                                    u.users.get(key).sendMessage("server");
                                    u.users.get(key).sendMessage("#-");
                                    u.users.get(key).sendMessage(username);
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
                                    System.out.println(username);
                                    result = u.verify(username);
                                    sendMessage(result);
                                } while (result.equals("-"));
                                keys = u.users.keySet();
                                for (String key : keys) {
                                    u.users.get(key).sendMessage("server");
                                    u.users.get(key).sendMessage("#+");
                                    u.users.get(key).sendMessage(username);
                                }
                                u.users.put(username, this);
                                break;
                        }
                        break;
                    case "*": // comunicazione globale
                        Set<String> keys = u.users.keySet();
                        keys.remove(username);
                        for (String key : keys) {
                            u.users.get(key).sendMessage("*" + username);
                            u.users.get(key).sendMessage(message);
                        }
                        break;
                    default: // comunicazione privata tra due client
                        if (u.users.containsKey(receiver)) {
                            u.users.get(receiver).sendMessage(username);
                            u.users.get(receiver).sendMessage(message);
                        } else {
                            sendMessage("server");
                            sendMessage("#!");
                            sendMessage(receiver);
                        }
                }
            } while (!receiver.equals("/!"));
            s.close();
        } catch (IOException e) {
            System.out.println("Errore!!");
        }
    }
}
