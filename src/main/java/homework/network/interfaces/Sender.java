package homework.network.interfaces;

import java.net.InetAddress;

public interface Sender {
    void sendPackage(byte[] packet, InetAddress target, int port) throws Exception;
}
