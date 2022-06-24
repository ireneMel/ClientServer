package protocols;

import homework.homework1.message.Message;
import homework.homework1.packet.Package;
import homework.network.client_server_protocols.tcp.StoreClientTCP;
import homework.network.client_server_protocols.tcp.StoreServerTCP;
import homework.utils.Commands;
import homework.utils.GeneratorUtils;
import lombok.SneakyThrows;
import org.junit.jupiter.api.AfterAll;
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
//        serverTCP.start(6666);
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

    @SneakyThrows
    @AfterAll
    static void stop() {
//        serverThread.join();
//        serverTCP.stop();
    }
//    private static Thread serverThread;
//    private static StoreServerTCP serverTCP;
//    private StoreClientTCP client;
//
//    @BeforeAll
//    static void init() {
//        serverTCP = new StoreServerTCP();
//        serverTCP.start(6666);
////        serverThread = new Thread(serverTCP);
////        serverThread.start();
//    }
//
//    @Test
//    public void testResponse() throws IOException {
//
//        client = new StoreClientTCP();
//        client.startConnection(6666);
//        String reply = client.sendData(packet);
//
//        assertEquals("Ok", reply);
//    }
//
//    @AfterAll
//    static void stop() throws InterruptedException {
////        serverThread.join();
////        serverTCP.stopServer();
//
//    }
}
