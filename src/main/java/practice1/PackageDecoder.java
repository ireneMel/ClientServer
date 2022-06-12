package practice1;

import java.nio.ByteBuffer;

public class PackageDecoder {
    private final byte[] wholePackage;
    private static final int HEADER_LENGTH = 16;

    public PackageDecoder(byte[] wholePackage) {
        this.wholePackage = wholePackage;
    }

    public Package getPackage() throws Exception {
        ByteBuffer buffer = ByteBuffer.wrap(wholePackage);

        byte bMagic = buffer.get();
        if(bMagic != Package.bMagic) throw new Exception("Wrong package structure");

        byte bSrc = buffer.get();
        long packetId = buffer.getLong();
        int wLen = buffer.getInt();
        short wCrc16_start = buffer.getShort();

        byte[] header = ByteBuffer.allocate(1 + 1 + 8 + 4)
                .put(bMagic)
                .put(bSrc)
                .putLong(packetId)
                .putInt(wLen).array();

        if (CRC16.getCrc16(header) != wCrc16_start) throw new Exception("Wrong header");

        byte[] wholeMessage = new byte[wLen];
        buffer.get(wholeMessage, 0, wLen);
        Message message = new MessageDecoder(wholeMessage, packetId).getMessage();

        short wCrc16_end = buffer.getShort();

        byte[] mes = ByteBuffer.allocate(wholePackage.length - 2)
                .put(bMagic)
                .put(bSrc)
                .putLong(packetId)
                .putInt(wLen)
                .putShort(wCrc16_start)
                .put(wholeMessage)
               .array();


        short c = CRC16.getCrc16(mes);

        if (CRC16.getCrc16(mes) != wCrc16_end) throw new Exception("Wrong package structure");
        return new Package(bSrc, packetId, wLen, message);
    }

}
