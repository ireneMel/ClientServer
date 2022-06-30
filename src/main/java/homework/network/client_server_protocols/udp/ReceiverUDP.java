package homework.network.client_server_protocols.udp;

import homework.homework1.packet.Package;
import homework.network.Decryptor;
import homework.network.Encryptor;
import homework.network.Processor;
import homework.network.interfaces.Receiver;
import lombok.SneakyThrows;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.nio.ByteBuffer;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

public class ReceiverUDP implements Receiver {
    private final Decryptor decryptor;
    private final Processor processor;
    private final Encryptor encryptor;
    private final SenderUDP sender;
    private final DatagramSocket socket;

    final int size = 8;
    ExecutorService executorService = Executors.newFixedThreadPool(size);

    final byte[][] buffers = new byte[size][256];
    final BlockingQueue<Integer> free = new LinkedBlockingQueue<>();


    private boolean isRunning = false;

    public ReceiverUDP(Decryptor decryptor, Processor processor, Encryptor encryptor, SenderUDP sender, DatagramSocket socket) {
        this.decryptor = decryptor;
        this.processor = processor;
        this.encryptor = encryptor;
        this.sender = sender;
        this.socket = socket;
    }

    @SneakyThrows
    @Override
    public void startReceive() {
        isRunning = true;
        for (int i = 0; i < size; ++i) free.put(i);
        while (isRunning) {
            executorService.execute(() -> {
                try {
                    int id = free.take();
                    DatagramPacket packet = new DatagramPacket(buffers[id], buffers[id].length);
                    try {
                        socket.receive(packet);

                        ByteBuffer buffer = ByteBuffer.allocate(packet.getLength());
                        buffer.put(packet.getData(), 0, packet.getLength());
                        Package decryptedPackage = decryptor.decrypt(buffer.array()).get();
                        Package responsePackage = processor.processMessage(decryptedPackage).get();
                        byte[] encryptedResponse = encryptor.encrypt(responsePackage).get();
                        sender.sendPackage(encryptedResponse, packet.getAddress(), packet.getPort());
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                    free.put(id);

                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            });
        }
    }

    public void stopReceive() {
        isRunning = false;
        executorService.shutdownNow();
    }
}
