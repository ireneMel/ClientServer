package homework.network.interfaces_impl;

import homework.network.Decryptor;
import homework.network.interfaces.Receiver;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ReceiverImpl implements Receiver {

    private final ExecutorService executor = Executors.newFixedThreadPool(5);
    private Decryptor decryptor = new Decryptor();

    public void receivePackage(byte[] packet) {
        executor.execute(() -> {
            decryptor.decrypt(packet);
        });
    }

}
