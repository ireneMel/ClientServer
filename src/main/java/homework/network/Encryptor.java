package homework.network;

import homework.homework1.packet.Package;
import homework.homework1.packet.PackageEncoder;
import lombok.AllArgsConstructor;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

/*
Клас, що в багато потоків шифрує відповідь
та відправляє класу, що відповідає за передачу
інформації мережею
 */
@AllArgsConstructor
public class Encryptor {
    private final ExecutorService executor;
    public Future<byte[]> encrypt(Package packet) {
        return executor.submit(new PackageEncoder(packet)::getBytes);
    }
}
