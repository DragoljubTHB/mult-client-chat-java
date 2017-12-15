package saqs.eis;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class Client {

    public static void main(String[] args) {
        try {
            Socket client = new Socket("localhost", 2222);
            System.out.println("Client verbunden");

            new Thread(read(client)).start();
            write(client);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void write(Socket server) {
        try {
            System.out.println("writer started" + server.getPort());
            Scanner s = new Scanner(System.in);
            PrintWriter out = new PrintWriter(server.getOutputStream(), true);

            while (true) {
                out.println(s.nextLine());
            }
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    private static Runnable read(Socket client) throws IOException {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                BufferedReader in = null;
                try {
                    in = new BufferedReader(new InputStreamReader(client.getInputStream()));
                    String line = null;
                    System.out.println("while in.readLine()" + client.getLocalPort());
                    while ((line = in.readLine()) != null) {

                        System.out.println(line);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };
        return runnable;
    }

}


