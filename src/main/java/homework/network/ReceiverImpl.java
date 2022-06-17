package homework.network;

import homework.pckage.Package;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class ReceiverImpl implements Receiver {
    private static final int THREADS = 5;
    private final Executor executor = Executors.newFixedThreadPool(THREADS);
    private final Decryptor decryptor;

    public ReceiverImpl(Decryptor decryptor) {
        this.decryptor = decryptor;
    }

    @Override
    public void receivePackage(byte[] pckage) {
        executor.execute(() -> {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            decryptor.decrypt(pckage);
        });
    }
}
