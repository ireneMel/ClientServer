package practice1;

import homework.homework1.packet.PackageDecoder;
import homework.network.Decryptor;
import homework.network.Encryptor;
import homework.network.Processor;
import homework.network.interfaces.Generator;
import homework.network.interfaces.Receiver;
import homework.network.interfaces.Sender;
import homework.network.interfaces_impl.GeneratorImpl;
import homework.network.interfaces_impl.ReceiverImpl;
import homework.network.interfaces_impl.SenderImpl;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class GeneratorTest {
    @SneakyThrows
    @Test
    public void test() {
        ExecutorService service = Executors.newFixedThreadPool(20);
        Sender sender = packet -> {
            try {
                byte[] body = new PackageDecoder(packet).getPackage().getMessage().getMessageBody();
                String ans = new String(body);
                Assertions.assertTrue(ans.equals("Ok") || ans.equals("Not Ok"));
            } catch (Exception e) {
                Assertions.fail("Exception!!!");
            }
        };
        Encryptor encryptor = new Encryptor(service, sender);
        Processor processor = new Processor(service, encryptor);
        Decryptor decryptor = new Decryptor(service, processor);
        Receiver receiver = new ReceiverImpl(decryptor);
        Generator generator = new GeneratorImpl(receiver);
        ExecutorService gen = Executors.newFixedThreadPool(10);
        for (int i = 0; i < 100; ++i)
            gen.submit(generator::generateMessage);
        service.awaitTermination(2, TimeUnit.SECONDS);
        service.shutdown();
    }
}
