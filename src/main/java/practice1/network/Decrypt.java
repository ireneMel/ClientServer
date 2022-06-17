package practice1.network;

import practice1.packet.Package;
import practice1.packet.PackageDecoder;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/*
Клас, що в багато потоків розбирає,
дешифрує та перетворює пакет в об`єкт типу Message.
Після чого передає повідомлення в Processor
 */
public class Decrypt {
    private final ExecutorService executor = Executors.newFixedThreadPool(5);
    private Processor processor;

    public void decrypt(byte[] packet) {
        executor.execute(() -> {
            try {
                Package decodedPackage = new PackageDecoder(packet).getPackage();
                processor.processMessage(decodedPackage.getMessage());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }
}
