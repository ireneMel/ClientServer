package homework;

import homework.message.Message;
import homework.network.*;
import homework.pckage.Package;
import homework.pckage.PackageDecoder;
import homework.pckage.PackageEncoder;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;


public class NetworkTest {


    @Test
    public void message_test() throws InterruptedException {

        class FakeSender implements Sender{
            @Override
            public void send(byte[] pckage)  {
                byte[] message;
                try {
                    message = new PackageDecoder(pckage).getPackage().getMessage().getMessageBody();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }

                assertArrayEquals(message, "Ok".getBytes());
            }
        }

        Encryptor encryptor = new Encryptor(new FakeSender());
        Processor processor = new Processor(encryptor);
        Decryptor decryptor = new Decryptor(processor);
        Receiver r = new ReceiverImpl(decryptor);
        byte[] test = new byte[]{1, 2, 3, 4, 5};
        Message message = new Message(1, 1, test);

        Package aPackage = new Package((byte) 1, 1, 13, message);

        r.receivePackage(new PackageEncoder(aPackage).getBytes());
        r.receivePackage(new PackageEncoder(aPackage).getBytes());
        r.receivePackage(new PackageEncoder(aPackage).getBytes());
        r.receivePackage(new PackageEncoder(aPackage).getBytes());
        Thread.sleep(1200);
    }
}
