package practice1.network.interfaces;

import java.net.InetAddress;

public interface Sender {
    void sendPackage(byte[] packet, InetAddress target);
}
