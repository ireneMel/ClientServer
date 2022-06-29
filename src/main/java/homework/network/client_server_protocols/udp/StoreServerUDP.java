package homework.network.client_server_protocols.udp;

import homework.network.Decryptor;
import homework.network.Encryptor;
import homework.network.Processor;
import homework.network.interfaces.Receiver;
import homework.network.interfaces_impl.ReceiverImpl;
import homework.network.interfaces_impl.SenderImpl;
import lombok.SneakyThrows;

import java.net.DatagramSocket;
import java.net.SocketException;

public class StoreServerUDP implements Runnable {
    private final Receiver receiver;
    private final DatagramSocket socket;
    public static int port = 6667;

    public StoreServerUDP(Decryptor decryptor, Processor processor, Encryptor encryptor) throws SocketException {
        socket = new DatagramSocket(port);
        receiver = new ReceiverImpl(decryptor, processor, encryptor, new SenderImpl(socket), socket);
    }

    @SneakyThrows
    public void run() {
        receiver.startReceive();
    }

    public void stop() {
        receiver.stopReceive();
    }
}
