package homework.network.interfaces_impl;

import homework.network.Decrypt;
import homework.network.interfaces.Receiver;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ReceiverImpl implements Receiver {

    private final ExecutorService executor = Executors.newFixedThreadPool(5);
    private Decrypt decrypt = new Decrypt();

    public void receivePackage(byte[] packet) {
        executor.execute(() -> {
            decrypt.decrypt(packet);
        });
    }

}
