package protocols;

import homework.homework1.message.Message;
import homework.homework1.packet.Package;
import homework.network.Decryptor;
import homework.network.Encryptor;
import homework.network.Processor;
import homework.network.client_server_protocols.tcp.StoreClientTCP;
import homework.network.client_server_protocols.tcp.StoreServerTCP;
import homework.network.client_server_protocols.udp.StoreClientUDP;
import homework.utils.Commands;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

public class MultipleTCPTest {
    private static StoreServerTCP server;
    static ExecutorService service = Executors.newFixedThreadPool(10);

    @SneakyThrows
    @BeforeAll
    public static void before() {
        server = new StoreServerTCP(
                new Decryptor(service),
                new Processor(service),
                new Encryptor(service));
    }

    @Test
    public void multiThreadOKTest() throws Exception {
        service.execute(server);
        AtomicInteger cnt = new AtomicInteger(0);
        List<Future<?>> list = new ArrayList<>();
        ExecutorService service = Executors.newFixedThreadPool(30);
        BlockingQueue<String> queue = new LinkedBlockingQueue<>();
        for (int i = 0; i < 1000; ++i)
            list.add(service.submit(()->{
                try {
                    StoreClientTCP client = new StoreClientTCP();
                    client.startConnection("localhost",6667);
                    for (int j = 0; j < 10; ++j) {
                        Message message = new Message(Commands.PRODUCT_ADD_NAME, 0, "AAA".getBytes());
                        int ans = cnt.getAndAdd(1);
                        client.sendMessage(new Package((byte) 0, ans, message.getMessageBody().length + 8, message));
                        queue.put(client.readMessage());
                    }
                    client.stopConnection();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }));

        for (Future<?> f : list){
            f.get();
        }
        Assertions.assertEquals(1000*10,queue.size());
        for (String a : queue) Assertions.assertEquals("Ok",a);
    }

    @Test
    public void multiThreadUnavailableTest() throws Exception {
        service.execute(()->{
            try {
                Thread.sleep(2500);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            server.run();
        });
        AtomicInteger cnt = new AtomicInteger(0);
        List<Future<?>> list = new ArrayList<>();
        ExecutorService service = Executors.newFixedThreadPool(20);
        BlockingQueue<String> queue = new LinkedBlockingQueue<>();
        for (int i = 0; i < 1000; ++i)
            list.add(service.submit(()->{
                StoreClientTCP client = new StoreClientTCP();
                try {
                    client.startConnection("localhost",6667);
                    for (int j = 0; j < 10; ++j) {
                        Message message = new Message(Commands.PRODUCT_ADD_NAME, 0, "AAA".getBytes());
                        int ans = cnt.getAndAdd(1);
                        client.sendMessage(new Package((byte) 0, ans, message.getMessageBody().length + 8, message));
                        queue.put(client.readMessage());
                    }
                } catch (Exception e) {
                    throw new RuntimeException(e);
                } finally {
                    client.stopConnection();
                }
            }));

        for (Future<?> f : list){
            f.get();
        }
        Assertions.assertEquals(1000*10,queue.size());
        for (String a : queue) Assertions.assertTrue(a.equals("Ok"));
    }
}
