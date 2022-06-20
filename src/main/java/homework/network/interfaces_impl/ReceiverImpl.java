package homework.network.interfaces_impl;

import homework.network.Decryptor;
import homework.network.interfaces.Receiver;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ReceiverImpl implements Receiver {

    private final ExecutorService executor = Executors.newFixedThreadPool(5);
    private final Decryptor decryptor;

    public ReceiverImpl(Decryptor decryptor) {
        this.decryptor = decryptor;
    }

    public void receivePackage(byte[] packet) {
        executor.execute(() -> {
            decryptor.decrypt(packet);
        });
    }

}
