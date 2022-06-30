package homework.network.client_server_protocols.tcp;

import homework.homework1.packet.Package;
import homework.network.Decryptor;
import homework.network.Encryptor;
import homework.network.Processor;
import homework.network.interfaces.Receiver;
import lombok.SneakyThrows;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

public class ReceiverTCP implements Receiver {
    private final Decryptor decryptor;
    private final Processor processor;
    private final Encryptor encryptor;
    private final SenderTCP sender;
    private final ServerSocket socket;

    final int size = 8;
    ExecutorService executorService = Executors.newFixedThreadPool(size);

    final byte[][] buffers = new byte[size][256];
    final BlockingQueue<Integer> free = new LinkedBlockingQueue<>();


    private boolean isRunning = false;

    public ReceiverTCP(Decryptor decryptor, Processor processor, Encryptor encryptor, SenderTCP sender, ServerSocket socket) {
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
                    processSocket(socket.accept());
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            });
        }
    }

    private void processSocket(Socket socket) throws Exception {
        DataInputStream in = new DataInputStream(socket.getInputStream());
        DataOutputStream out = new DataOutputStream(socket.getOutputStream());
        int id = free.take();
        while (true) {
            byte[] buffer = buffers[id];
            int size = in.readInt();
            if (size == -1) {
                in.close();
                out.close();
                socket.close();
                break;
            }

            sender.sendServerEnabled(size, out);

            int actualSize = in.read(buffer, 0, size);
            if (actualSize != size) {
                in.close();
                out.close();
                socket.close();
                break;
            }
            ByteBuffer wrap = ByteBuffer.allocate(size);
            wrap.put(buffer, 0, size);
            Package decryptedPackage = decryptor.decrypt(wrap.array()).get();
            Package responsePackage = processor.processMessage(decryptedPackage).get();
            byte[] encryptedResponse = encryptor.encrypt(responsePackage).get();
            sender.sendPackage(encryptedResponse, out).get();



            free.put(id);
        }
    }

    public void stopReceive() {
        isRunning = false;
        executorService.shutdownNow();
    }
}
