package it.itismeucci;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

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
                        u.remove(username);
                        break;
                    case "*":
                        // Chat global
                        break;
                    default:
                        // Chat singola
                        break;
                }
            } while (!receiver.equals("/!"));
            s.close();
        } catch (IOException e) {
            System.out.println("Errore!!");
        }
    }
}
