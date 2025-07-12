import database.InMemoryDatabase;
import database.config.ConfigStorage;
import handler.RequestHandler;
import handler.ThreadExecutor;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Main {
    public static void main(String[] args) {
        // You can use print statements as follows for debugging, they'll be visible when running tests.
        System.out.println("Logs from your program will appear here!");

        InMemoryDatabase inMemoryDatabase = new InMemoryDatabase();

        for (int i = 0; i < args.length; i++) {
            if (args[i].startsWith("--")) {
                inMemoryDatabase
                        .getConfigDatabase()
                        .put(
                                args[i].substring(2),
                                new ConfigStorage(args[i].substring(2), args[i + 1])
                        );
            }
            i++;
        }

        ServerSocket serverSocket = null;
        Socket clientSocket = null;
        int port = 6379;
        try {
            serverSocket = new ServerSocket(port);

            serverSocket.setReuseAddress(true);
            // Wait for connection from client.

            ThreadExecutor threadExecutor = new ThreadExecutor();

            while (true) {
                clientSocket = serverSocket.accept();
                threadExecutor.execute(new RequestHandler(clientSocket, inMemoryDatabase));
//                new Thread(new RequestHandler(clientSocket)).start();
            }


        } catch (IOException e) {
            System.out.println("IOException: " + e.getMessage());
        } finally {
            try {
                if (clientSocket != null) {
                    clientSocket.close();
                }
            } catch (IOException e) {
                System.out.println("IOException: " + e.getMessage());
            }
        }
    }
}
