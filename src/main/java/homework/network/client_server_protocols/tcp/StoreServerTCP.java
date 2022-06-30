package homework.network.client_server_protocols.tcp;

import homework.network.Decryptor;
import homework.network.Encryptor;
import homework.network.Processor;
import homework.network.client_server_protocols.udp.ReceiverUDP;
import homework.network.client_server_protocols.udp.SenderUDP;
import homework.network.interfaces.Receiver;
import lombok.SneakyThrows;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.ServerSocket;
import java.net.SocketException;

public class StoreServerTCP implements Runnable {
    private final Receiver receiver;
    public static int port = 6667;

    public StoreServerTCP(Decryptor decryptor, Processor processor, Encryptor encryptor) throws IOException {
        ServerSocket socket = new ServerSocket(port);
        receiver = new ReceiverTCP(decryptor,processor, encryptor,new SenderTCP(), socket);
    }

    @SneakyThrows
    public void run() {
        receiver.startReceive();
    }

    public void stop() {
        receiver.stopReceive();
    }
}