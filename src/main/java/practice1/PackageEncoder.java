package practice1;

import java.nio.ByteBuffer;

public class PackageEncoder {
    private final Package pack;

    public PackageEncoder(Package pack) {
        this.pack = pack;
    }

    public byte[] getBytes() {
        byte[] encodedWholeMessage = new MessageEncoder(pack.getMessage(), pack.getPacketId()).getBytes();
        ByteBuffer byteBuffer = ByteBuffer.allocate(Package.HEADER_LENGTH)
                .put(Package.bMagic)
                .put(pack.getBSrc())
                .putLong(pack.getPacketId())
                .putInt(pack.getWLen());

        ByteBuffer buffer2 = ByteBuffer.allocate(Package.HEADER_LENGTH + 2 + encodedWholeMessage.length)
                .put(byteBuffer.array())
                .putShort(CRC16.getCrc16(byteBuffer.array()))
                .put(encodedWholeMessage);

        ByteBuffer buffer3 = ByteBuffer.allocate(Package.HEADER_LENGTH + encodedWholeMessage.length + 2 + 2)
                .put(buffer2.array())
                .putShort(CRC16.getCrc16(buffer2.array()));

        return buffer3.array();
    }
}
