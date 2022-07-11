package homework.http_server;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;

public class LoginHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        StringBuilder builder = new StringBuilder();

        BufferedReader in = new BufferedReader(new InputStreamReader(exchange.getRequestBody()));

        if ("GET".equals(exchange.getRequestMethod())) {
            String query = exchange.getRequestURI().toString();
            String[] s = query.split("\\?")[1].split("=");

            String username = s[0];
            String password = s[1];

            byte[] bytes;

            if (password.equals(OurHttpServer.getUserInformation().get(username))) {
                builder.append("unique token");
                bytes = builder.toString().getBytes();
                exchange.sendResponseHeaders(200, bytes.length);

                OutputStream os = exchange.getResponseBody();
                os.write(bytes);
                os.close();
            } else {
                exchange.sendResponseHeaders(401, 0);
                OutputStream os = exchange.getResponseBody();
                os.close();
            }
        }

    }

}
