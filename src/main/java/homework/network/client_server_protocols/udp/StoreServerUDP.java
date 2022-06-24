package homework.network.client_server_protocols.udp;

import homework.homework1.packet.Package;
import homework.homework1.packet.PackageDecoder;
import lombok.SneakyThrows;

import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class StoreServerUDP implements Runnable {
    private DatagramSocket socket;
    private boolean running;
    private byte[] buffer = new byte[256];
    public static int port = 6667;

//    @SneakyThrows
//    public StoreServerUDP() {
//        try {
//            socket = new DatagramSocket(port);
//            new StoreServerUDP()
//        } catch (IOException e) {
//
//        } finally {
//
//        }
//    }

    @SneakyThrows
    public void run() {
        running = true;

        while (running) {
            DatagramPacket packet
                    = new DatagramPacket(buffer, buffer.length);
            socket.receive(packet);
            Package received = new PackageDecoder(packet.getData()).getPackage();
            packet = new DatagramPacket(buffer, buffer.length);
            socket.send(packet);

//            InetAddress address = packet.getAddress();
//            int port = packet.getPort();
//            packet = new DatagramPacket(buffer, buffer.length, address, port);
//            String received
//                    = new String(packet.getData(), 0, packet.getLength());
//
//            if (received.equals("end")) {
//                running = false;
//                continue;
//            }
//            socket.send(packet);
        }
        socket.close();
    }
}
