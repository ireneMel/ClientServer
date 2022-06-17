package practice1.network;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/*
Клас, що в багато потоків шифрує відповідь
та відправляє класу, що відповідає за передачу
інформації мережею
 */
public class Encrypt {
    private final ExecutorService executor = Executors.newFixedThreadPool(5);

    public void encrypt(Package packet) {

    }
}
