package it.itismeucci;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.ArrayList;

public class MyThread extends Thread {
    private Socket s;
    private Users u;
    
    public MyThread(Socket s, Users u) {
        this.s = s;
        this.u = u;
    }

    public void run() {
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(s.getInputStream()));
            DataOutputStream out = new DataOutputStream(s.getOutputStream());
            String result;
            String username;
            do {
                username = in.readLine();
                result = u.verify(username);
                out.writeBytes(result + "\n");    
            } while (result.equals("-"));
            String message;
            String receiver;
            do {
                message = in.readLine();
                receiver = in.readLine();
                switch (receiver) {
                    case "/!":
                        // Chiusura comunicazione
                        break;
                    case "*":
                        // Chat globale
                        break;
                    default:
                        // Chat singola
                        break;
                }
            } while (!receiver.equals("/!"));
        } catch (IOException e) {
            System.out.println("Errore!!");
        }
    }
}
