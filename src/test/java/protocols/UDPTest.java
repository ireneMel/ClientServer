package protocols;

import homework.homework1.message.Message;
import homework.homework1.packet.Package;
import homework.network.client_server_protocols.udp.StoreClientUDP;
import homework.network.client_server_protocols.udp.StoreServerUDP;
import homework.utils.Commands;
import homework.utils.GeneratorUtils;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static homework.utils.GeneratorUtils.createMessageBody;
import static homework.utils.GeneratorUtils.productTemplates;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class UDPTest {
    static StoreClientUDP client;

    @BeforeAll
    public static void setup() {
        new StoreServerUDP().run();
        client = new StoreClientUDP();
    }

    @Test
    public void whenCanSendAndReceivePacket_thenCorrect() {
        Message message = GeneratorUtils.template.getMessage();
        message = message.withCType(Commands.PRODUCT_ADD_NAME);
        message = message.withMessageBody(createMessageBody(productTemplates[0][0]));

        Package packet = GeneratorUtils.packMessageWithoutEncoding(0, message);

        client.sendPackage(packet);
        assertEquals("Ok", client.readMessage());
        client.sendPackage(packet);
        assertNotEquals("Ok", client.readMessage());
    }
//
//    @AfterAll
//    public static void tearDown() {
//        client.sendPackage("end");
//        client.close();
//    }
}
