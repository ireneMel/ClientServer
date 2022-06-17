package practice1.network.interfaces_impl;

import practice1.packet.PackageDecoder;

import java.net.InetAddress;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SenderImpl {
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
