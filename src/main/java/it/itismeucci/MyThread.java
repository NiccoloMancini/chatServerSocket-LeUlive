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

    public void sendMessage(String msg) throws IOException{
        this.out.writeBytes(msg + "\n");
    }

    public void run() {
        try {
            String username = "";
            String message;
            String receiver;
            do {
                receiver = in.readLine();
                message = in.readLine();
                switch (receiver) {
                    case "server":
                        switch (message) {
                            case "/!":
                                u.users.remove(username, this);
                                Set<String> keys = u.users.keySet();
                                for (String key : keys) {
                                    u.users.get(key).sendMessage("server");
                                    u.users.get(key).sendMessage("#-");
                                    u.users.get(key).sendMessage(username);
                                }           
                                break;
                            case "/+":
                                String result;
                                do {
                                    username = in.readLine();
                                    result = u.verify(username);
                                    sendMessage(result);
                                } while (result.equals("-"));
                                keys = u.users.keySet();
                                for (String key : keys) {
                                    u.users.get(key).sendMessage("server");
                                    u.users.get(key).sendMessage("#+");
                                    u.users.get(key).sendMessage(username);
                                }        
                                u.users.put(username,this); 
                                break;
                        }
                        break;
                    case "*":    
                        Set<String> keys = u.users.keySet();
                        keys.remove(username);
                        for (String key : keys) {
                            u.users.get(key).sendMessage("*" + username);
                            u.users.get(key).sendMessage(message);
                        }               
                        break;
                    default:
                        if (u.users.containsKey(receiver)) {
                            u.users.get(receiver).sendMessage(username);
                            u.users.get(receiver).sendMessage(message);
                        } else {
                            sendMessage("#!");
                        }
                }
            } while (!receiver.equals("/!"));
            s.close();
        } catch (IOException e) {
            System.out.println("Errore!!");
        }
    }
}
