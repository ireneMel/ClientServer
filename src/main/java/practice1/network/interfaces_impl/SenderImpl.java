package practice1.network.interfaces_impl;

import java.net.InetAddress;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SenderImpl {
    private ExecutorService executorService = Executors.newFixedThreadPool(5);

    SenderImpl(ExecutorService executorService) {
        this.executorService = executorService;
    }

    public void sendPackage(byte[] packet, InetAddress target) {

    }
}
