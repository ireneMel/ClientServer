package homework.network;

import homework.pckage.Package;
import homework.pckage.PackageEncoder;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class Encryptor {
    private static final int THREADS = 5;
    private final Executor executor = Executors.newFixedThreadPool(THREADS);
    private final Sender sender;

    public Encryptor(Sender sender) {
        this.sender = sender;
    }

    public void encrypt(Package pckage) {
        executor.execute(() -> {
            try {
                byte[] encodedPackage = new PackageEncoder(pckage).getBytes();
                sender.send(encodedPackage);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }
}
