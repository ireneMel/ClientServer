package protocols;

import homework.homework1.message.Message;
import homework.homework1.packet.Package;
import homework.network.Decryptor;
import homework.network.Encryptor;
import homework.network.Processor;
import homework.network.client_server_protocols.udp.StoreClientUDP;
import homework.network.client_server_protocols.udp.StoreServerUDP;
import homework.utils.Commands;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SingleUDPTest {
    private static StoreServerUDP server;
    static ExecutorService service = Executors.newFixedThreadPool(10);

    @SneakyThrows
    @BeforeAll
    public static void before() {
        server = new StoreServerUDP(
                new Decryptor(service),
                new Processor(service),
                new Encryptor(service));
        service.execute(server);
    }

    @Test
    public void test() throws Exception {
        StoreClientUDP client = new StoreClientUDP(6667);
        Message message = new Message(Commands.PRODUCT_ADD_NAME, 0, "AAA".getBytes());
        client.sendPackage(new Package((byte) 0, 0, message.getMessageBody().length + 8, message));

        Assertions.assertEquals("Ok",client.readMessage(1));
    }
}
