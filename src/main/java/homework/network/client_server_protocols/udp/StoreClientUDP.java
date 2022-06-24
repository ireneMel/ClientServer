package homework.network.client_server_protocols.udp;

import homework.homework1.packet.Package;
import homework.homework1.packet.PackageEncoder;
import lombok.SneakyThrows;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class StoreClientUDP {
    private DatagramSocket socket;
    private InetAddress address;
    private byte[] buffer;
    private DatagramPacket datagramPacket;


    @SneakyThrows
    public StoreClientUDP() {
        try {
            socket = new DatagramSocket();
            address = InetAddress.getByName("localhost");
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            close();
        }
    }

    @SneakyThrows
    public void sendPackage(Package packet) {
        boolean wasSent = false;

        while (!wasSent) {
            buffer = new PackageEncoder(packet).getBytes();
            datagramPacket =
                    new DatagramPacket(buffer, buffer.length, address, StoreServerUDP.port);

            socket.send(datagramPacket);

            //check if was received
            datagramPacket = new DatagramPacket(buffer, buffer.length);
            socket.receive(datagramPacket);

            byte[] wasSentB = datagramPacket.getData();

            if (wasSentB == buffer) {
                buffer = wasSentB;
                wasSent = true;
            }
        }

//        return new String(packet.getData(), 0, packet.getLength());
    }

    public String readMessage() {
        return "Ok";
    }

    public void close() {
        socket.close();
    }
}
