package homework.network;

import homework.network.interfaces.Sender;
import homework.network.interfaces_impl.SenderImpl;
import homework.homework1.packet.Package;
import homework.homework1.packet.PackageEncoder;
import lombok.AllArgsConstructor;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/*
Клас, що в багато потоків шифрує відповідь
та відправляє класу, що відповідає за передачу
інформації мережею
 */
@AllArgsConstructor
public class Encryptor {
    private final ExecutorService executor;
    private final Sender sender;

    public void encrypt(Package packet) {
        executor.execute(() -> {
            byte[] encodedPackage = new PackageEncoder(packet).getBytes();
            sender.sendPackage(encodedPackage);
//            Tester tester = new Tester();
//            tester.receiveEncoded(encodedPackage);
        });
    }
}
