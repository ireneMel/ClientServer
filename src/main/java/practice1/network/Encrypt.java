package practice1.network;

import practice1.network.interfaces.Sender;
import practice1.network.interfaces_impl.SenderImpl;
import practice1.packet.Package;
import practice1.packet.PackageEncoder;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/*
Клас, що в багато потоків шифрує відповідь
та відправляє класу, що відповідає за передачу
інформації мережею
 */
public class Encrypt {
    private final ExecutorService executor = Executors.newFixedThreadPool(5);
    private final SenderImpl sender = new SenderImpl();

    public void encrypt(Package packet) {
        executor.execute(() -> {
            byte[] encodedPackage = new PackageEncoder(packet).getBytes();
            sender.sendPackage(encodedPackage);
//            Tester tester = new Tester();
//            tester.receiveEncoded(encodedPackage);
        });
    }
}
