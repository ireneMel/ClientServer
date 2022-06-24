package homework.network.client_server_protocols.tcp;

import lombok.SneakyThrows;

import java.io.IOException;
import java.net.ServerSocket;

public class StoreServerTCP implements Runnable {
    private ServerSocket serverSocket;

    public void start(int port) throws IOException {
        serverSocket = new ServerSocket(port);
        for (int i = 0; i < 2; i++) {
            new StoreClientHandler(serverSocket.accept()).start();
        }
    }

    @SneakyThrows
    @Override
    public void run() {
        StoreServerTCP server = new StoreServerTCP();
        server.start(6666);
    }

    public void stop() throws IOException {
        serverSocket.close();
    }
}