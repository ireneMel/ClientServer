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

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicInteger;

public class MultipleUDPTest {
    private static StoreServerUDP server;
    static ExecutorService service = Executors.newFixedThreadPool(10);

    @SneakyThrows
    @BeforeAll
    public static void before() {
        server = new StoreServerUDP(
                new Decryptor(service),
                new Processor(service),
                new Encryptor(service));
    }

    @Test
    public void multiThreadOKTest() throws Exception {
        service.execute(server);
        AtomicInteger cnt = new AtomicInteger(0);
        List<Future<String>> list = new ArrayList<>();
        ExecutorService service = Executors.newFixedThreadPool(3);
        for (int i = 0; i < 10; ++i)
            list.add(service.submit(()->{
                StoreClientUDP client = new StoreClientUDP(6667);
                Message message = new Message(Commands.PRODUCT_ADD_NAME, 0, "AAA".getBytes());
                int ans = cnt.getAndAdd(2);
                client.sendPackage(new Package((byte) 0, ans, message.getMessageBody().length + 8, message));
                try {
                    return client.readMessage(ans + 1);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }));

        for (Future<String> f : list){
            Assertions.assertEquals("Ok",f.get());
        }
    }

    @Test
    public void multiThreadTimeoutTest() throws Exception {
        AtomicInteger cnt = new AtomicInteger(0);
        List<Future<String>> list = new ArrayList<>();
        ExecutorService service = Executors.newFixedThreadPool(10);
        for (int i = 0; i < 10; ++i)
            list.add(service.submit(()->{
                StoreClientUDP client = new StoreClientUDP(6667);
                Message message = new Message(Commands.PRODUCT_ADD_NAME, 0, "AAA".getBytes());
                int ans = cnt.getAndAdd(2);
                client.sendPackage(new Package((byte) 0, ans, message.getMessageBody().length + 8, message));
                try {
                    return client.readMessage(ans + 1);
                } catch (TimeoutException e) {
                    return "Timeout";
                }
            }));

        for (Future<String> f : list){
            Assertions.assertEquals("Timeout",f.get());
        }
    }
}
