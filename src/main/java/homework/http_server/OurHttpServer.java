package homework.http_server;

import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.HashMap;

public class OurHttpServer {
    private static HashMap<String, String> userInformation = new HashMap<>();

    public static HashMap<String, String> getUserInformation() {
        return userInformation;
    }

    public void addUser(String username, String password) {
        userInformation.put(username, password);
    }

    public void create() throws IOException {
        addUser("user1", "123");

        HttpServer server = HttpServer.create();
        server.bind(new InetSocketAddress(8080), 0);

        server.createContext("/login", new LoginHandler());

        server.createContext("/api/good", new GoodHandler());

        server.setExecutor(null);
        server.start();
    }

    public static void main(String[] args) throws IOException {
        new OurHttpServer().create();
    }
}
