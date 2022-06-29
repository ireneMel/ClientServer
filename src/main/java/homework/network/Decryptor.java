package homework.network;

import homework.homework1.packet.Package;
import homework.homework1.packet.PackageDecoder;
import lombok.AllArgsConstructor;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

/*
Клас, що в багато потоків розбирає,
дешифрує та перетворює пакет в об`єкт типу Message.
Після чого передає повідомлення в Processor
 */
@AllArgsConstructor
public class Decryptor {
    private final ExecutorService executor;
    public Future<Package> decrypt(byte[] packet) {
        return executor.submit(() -> new PackageDecoder(packet).getPackage());
    }
}
