package homework.network.client_server_protocols.tcp;

import lombok.SneakyThrows;

import java.io.IOException;
import java.net.ServerSocket;

public class StoreServerTCP implements Runnable {
    private ServerSocket serverSocket;

    private StoreClientHandler clientHandler;

    public void start(int port) throws IOException {
        try {
            serverSocket = new ServerSocket(port);

            while (true) {
                clientHandler = new StoreClientHandler(serverSocket.accept());
                clientHandler.start();
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            serverSocket.close();
        }
    }

    @SneakyThrows
    @Override
    public void run() {
        StoreServerTCP server = new StoreServerTCP();
        server.start(6666);
    }
}