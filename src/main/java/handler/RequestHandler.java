package handler;

import database.InMemoryDatabase;
import database.RDBJava;
import request.RedisRequest;

import java.io.ByteArrayInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.List;

public class RequestHandler implements Runnable {
    private final Socket clientSocket;
    private final InMemoryDatabase inMemoryDatabase;

    public RequestHandler(Socket clientSocket, InMemoryDatabase inMemoryDatabase) {
        this.clientSocket = clientSocket;
        this.inMemoryDatabase = inMemoryDatabase;
    }

    @Override
    public void run() {
        System.out.println("Thread::" + Thread.currentThread().threadId());
        while (true) {
            byte[] inputBytes = new byte[1024];
            try {
                int readBytes = clientSocket.getInputStream().read(inputBytes);
                if (readBytes == -1) continue;
//                System.out.println("Input data::" + new String(inputBytes).trim());
//                RedisRequest redisRequest = new RedisRequest(clientSocket.getInputStream());
                RedisRequest redisRequest = new RedisRequest(new ByteArrayInputStream(inputBytes), inMemoryDatabase);
                Object o = redisRequest.parseProtocol();
                System.out.println("protocol::" + o);

                DataOutputStream clientOutputStream = new DataOutputStream(clientSocket.getOutputStream());
//                String response = String.format("+%s\r\n", ((List) o).get(1));
                String response = redisRequest.parseCommand((List<Object>) o);
                System.out.println(response);
                clientOutputStream.write(response.getBytes());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

    }
}
