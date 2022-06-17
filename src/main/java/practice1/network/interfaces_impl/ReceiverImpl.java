package practice1.network.interfaces_impl;

import practice1.network.Decrypt;
import practice1.network.interfaces.Receiver;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ReceiverImpl implements Receiver {

    private final ExecutorService executor = Executors.newFixedThreadPool(5);
    private Decrypt decrypt;

    public void receivePackage(byte[] packet) {
        executor.execute(() -> decrypt.decrypt(packet));
    }

}
