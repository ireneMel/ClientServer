package practice1.network.interfaces;

import practice1.packet.Package;

public interface Receiver {
    void receivePackage(byte[] packet);
}
