package homework.network;

import homework.pckage.PackageDecoder;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class SenderImpl implements Sender {
    private static final int THREADS = 5;
    private final Executor executor = Executors.newFixedThreadPool(THREADS);

    public void send(byte[] pckage) {
        executor.execute(() -> {
            try {
                System.out.println(new PackageDecoder(pckage).getPackage().getPacketId());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }
}
