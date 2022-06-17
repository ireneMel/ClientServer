package homework.network;

import homework.message.Message;
import homework.pckage.Package;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class Processor {
    private static final int THREADS = 5;
    private final Executor executor = Executors.newFixedThreadPool(THREADS);
    private final Encryptor encryptor;

    public Processor(Encryptor encryptor) {
        this.encryptor = encryptor;
    }

    public void process(Package pckage) {
        executor.execute(() -> {
            Message message = pckage.getMessage();
            Message response = new Message(message.getCType(), message.getUserId(), "Ok".getBytes());
            encryptor.encrypt(new Package(pckage.getBSrc(), pckage.getPacketId() + 1, response.getMessageBody().length + 8, response));
        });
    }
}
