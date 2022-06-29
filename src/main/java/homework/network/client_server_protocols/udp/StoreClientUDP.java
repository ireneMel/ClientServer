package homework.network.client_server_protocols.udp;

import homework.homework1.packet.Package;
import homework.homework1.packet.PackageDecoder;
import homework.homework1.packet.PackageEncoder;
import lombok.SneakyThrows;

import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;
import java.util.Observable;
import java.util.concurrent.*;

public class StoreClientUDP {
    private ExecutorService service = Executors.newSingleThreadExecutor();
    private DatagramSocket socket;
    private InetAddress address;
    private final int port;

    @SneakyThrows
    public StoreClientUDP(int port) {
        this.port = port;
        try {
            socket = new DatagramSocket();
            address = InetAddress.getByName("localhost");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @SneakyThrows
    public void sendPackage(Package packet) {
        byte[] buffer = new PackageEncoder(packet).getBytes();
        DatagramPacket datagramPacket = new DatagramPacket(buffer, buffer.length);
        datagramPacket.setAddress(address);
        datagramPacket.setPort(port);
        socket.send(datagramPacket);
        sendPackages.put(packet.getPacketId(),packet);
    }

    private final Map<Long, Package> receivedPackages = new HashMap<>();
    private final Map<Long, Package> sendPackages = new HashMap<>();
    private final byte[] readBuffer = new byte[256];

    private void readMessage() throws Exception {
        DatagramPacket packet = new DatagramPacket(readBuffer, readBuffer.length);
        socket.receive(packet);
        ByteBuffer buffer = ByteBuffer.allocate(packet.getLength());
        buffer.put(packet.getData(), 0, packet.getLength());
        Package received = new PackageDecoder(buffer.array()).getPackage();
        receivedPackages.put(received.getPacketId(), received);
    }

    public String readMessage(long packetId) throws TimeoutException, ExecutionException, InterruptedException {
        for (int i = 0; i < 3; ++i){
            Future<?> f = service.submit(() -> {
                while (!receivedPackages.containsKey(packetId)) {
                    try {
                        readMessage();
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
            });
            try {
                f.get(1, TimeUnit.SECONDS);
                break;
            } catch (TimeoutException ignored) {
                sendPackage(sendPackages.get(packetId-1));
            }
        }
        Package packet = receivedPackages.get(packetId);
        if (packet == null) throw new TimeoutException("Package lost");
        sendPackages.remove(packetId);
        receivedPackages.remove(packetId);
        return new String(packet.getMessage().getMessageBody());

    }

    public void close() {
        socket.close();
    }
}
