package homework.network;

import homework.pckage.Package;
import homework.pckage.PackageDecoder;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class Decryptor {
    private static final int THREADS = 5;
    private final Executor executor = Executors.newFixedThreadPool(THREADS);
    private final Processor processor;

    public Decryptor(Processor processor) {
        this.processor = processor;
    }

    public void decrypt(byte[] pckage) {
        executor.execute(() -> {
            try {
                Package decodedPackage = (new PackageDecoder(pckage).getPackage());
                processor.process(decodedPackage);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }
}
