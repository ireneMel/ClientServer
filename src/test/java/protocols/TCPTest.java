package protocols;

import homework.homework1.message.Message;
import homework.homework1.packet.Package;
import homework.network.client_server_protocols.tcp.StoreClientTCP;
import homework.network.client_server_protocols.tcp.StoreServerTCP;
import homework.utils.Commands;
import homework.utils.GeneratorUtils;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static homework.utils.GeneratorUtils.createMessageBody;
import static homework.utils.GeneratorUtils.productTemplates;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class TCPTest {
    private static Thread serverThread;
    private static StoreServerTCP serverTCP;

    @SneakyThrows
    @BeforeAll
    static void init() {
        serverTCP = new StoreServerTCP();
        serverThread = new Thread(serverTCP);
        serverThread.start();
    }

    @Test
    public void oneClient() throws IOException {
        Message message = GeneratorUtils.template.getMessage();
        message = message.withCType(Commands.PRODUCT_ADD_NAME);
        message = message.withMessageBody(createMessageBody(productTemplates[0][0]));

        Package packet = GeneratorUtils.packMessageWithoutEncoding(0, message);

        StoreClientTCP client = new StoreClientTCP();
        client.startConnection("127.0.0.1", 6666);
        client.sendMessage(packet);

        String response = client.readMessage();

        assertEquals("Ok", response);
        client.stopConnection();
    }

    @Test
    public void multipleClients() throws IOException {
        Message message = GeneratorUtils.template.getMessage();
        message = message.withCType(Commands.PRODUCT_ADD_NAME);
        message = message.withMessageBody(createMessageBody(productTemplates[0][0]));

        Package packet = GeneratorUtils.packMessageWithoutEncoding(0, message);

        for (int i = 0; i < 25; i++) {
            StoreClientTCP client = new StoreClientTCP();
            client.startConnection("127.0.0.1", 6666);
            client.sendMessage(packet);

            assertEquals("Ok", client.readMessage());
            client.stopConnection();
        }
    }

    @Test
    public void multipleClientsDifferentRequests() throws IOException {
        Message message = GeneratorUtils.template.getMessage();
        message = message.withCType(Commands.PRODUCT_ADD_NAME);
        message = message.withMessageBody(createMessageBody(productTemplates[0][0]));

        Package packet = GeneratorUtils.packMessageWithoutEncoding(0, message);
        StoreClientTCP client;

        for (int i = 0; i < 10; i++) {

            message.withCType(Commands.PRODUCT_ADD_NAME);
            message = message.withMessageBody(createMessageBody(productTemplates[0][0]));
            packet = GeneratorUtils.packMessageWithoutEncoding(0, message);

            client = new StoreClientTCP();
            client.startConnection("127.0.0.1", 6666);
            client.sendMessage(packet);

            assertEquals("Ok", client.readMessage());
//            client.stopConnection();


            for (int j = 0; j < 4; j++) {
                message.withCType(Commands.PRODUCT_INCREASE);
                message = message.withMessageBody(createMessageBody(productTemplates[0][0], 10));
                packet = GeneratorUtils.packMessageWithoutEncoding(0, message);

                client = new StoreClientTCP();
                client.startConnection("127.0.0.1", 6666);
                client.sendMessage(packet);

                assertEquals("Ok", client.readMessage());
//                client.stopConnection();
            }

            for (int j = 0; j < 3; j++) {
                message.withCType(Commands.PRODUCT_DECREASE);
                message = message.withMessageBody(createMessageBody(productTemplates[0][0], 10));
                packet = GeneratorUtils.packMessageWithoutEncoding(0, message);

                client = new StoreClientTCP();
                client.startConnection("127.0.0.1", 6666);
                client.sendMessage(packet);

                assertEquals("Ok", client.readMessage());
//                client.stopConnection();
            }

        }
    }
}
