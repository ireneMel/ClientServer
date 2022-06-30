package homework.network.client_server_protocols.udp;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SenderUDP {
    private ExecutorService executorService = Executors.newFixedThreadPool(5);
    private DatagramSocket socket;

    public SenderUDP(DatagramSocket socket) {
        this.socket = socket;
    }

    public void sendPackage(byte[] packet, InetAddress target, int port) {
        executorService.execute(()->{
            DatagramPacket datagramPacket = new DatagramPacket(packet, packet.length);
            datagramPacket.setAddress(target);
            datagramPacket.setPort(port);
            try {
                socket.send(datagramPacket);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }
}
