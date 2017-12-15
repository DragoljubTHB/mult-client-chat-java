package saqs.eis;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Server {
    public Scanner s;
    static List<Socket> clientList;

    public Server() {
        clientList = new ArrayList<>();
        s = new Scanner(System.in);
    }

    public Runnable clientListener(Socket client, Server server) {
        Runnable runnable = new Runnable() {

            @Override
            public void run() {
                String message;
                System.out.println("Listener started");

                BufferedReader in;
                try {

                    in = new BufferedReader(new InputStreamReader(client.getInputStream()));

                    while ((message = in.readLine()) != null) {
                        System.out.println("publishing: " + message);
                        server.publish(message);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };
        return runnable;
    }

    private synchronized void publish(String message) {
        //senden an allen clienten
        synchronized (clientList) {
            System.out.println("synch clientList");
            for (Socket client : clientList) {
                System.out.println("for client  ");
                PrintWriter out = null;
                try {
                    System.out.println("sending: " + message + " to client " + client.getPort());
                    out = new PrintWriter(client.getOutputStream(), true);
                    out.println(message);
                    System.out.println("Sended");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    public static void main(String[] args) {
        Server server = new Server();
        server.start();

    }

    public void start() {
        try {

            Socket client;
            ServerSocket server = new ServerSocket(2222);

            System.out.println("Server laeuuuuuuuft auf Port: " + server.getLocalPort());


            while (true) {
                client = server.accept();
                synchronized (clientList) {
                    clientList.add(client);
                }
                new Thread(clientListener(client, this)).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
