package homework.network;

import homework.homework1.message.Message;
import homework.homework1.packet.Package;
import homework.homework1.packet.PackageDecoder;
import lombok.AllArgsConstructor;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/*
Клас, що в багато потоків розбирає,
дешифрує та перетворює пакет в об`єкт типу Message.
Після чого передає повідомлення в Processor
 */
@AllArgsConstructor
public class Decryptor {
    private final ExecutorService executor;
    private final Processor processor;

    public void decrypt(byte[] packet) {
        executor.execute(() -> {
            try {
                Package decodedPackage = new PackageDecoder(packet).getPackage();
                processor.processMessage(decodedPackage);
                //build new message using reply as body
                //put this message to encoder
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }
}
