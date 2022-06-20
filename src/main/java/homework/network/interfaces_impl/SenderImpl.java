package homework.network.interfaces_impl;

import homework.homework1.packet.PackageDecoder;
import homework.network.interfaces.Sender;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SenderImpl implements Sender {
    private ExecutorService executorService = Executors.newFixedThreadPool(5);

    public void sendPackage(byte[] packet) { //, InetAddress target
        executorService.execute(() -> {
            try {
                System.out.println(new PackageDecoder(packet).getPackage().getMessage());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }
}
