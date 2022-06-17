package practice1.packet;

import practice1.message.Message;
import practice1.message.MessageDecoder;
import practice1.utils.CRC16;

import java.nio.ByteBuffer;

public class PackageDecoder {
    private final byte[] wholePackage;
    private static final int HEADER_LENGTH = 14;
    private static final int CRC_LENGTH = 2;

    public PackageDecoder(byte[] wholePackage) {
        this.wholePackage = wholePackage;
    }

    public Package getPackage() throws Exception {
        ByteBuffer buffer = ByteBuffer.wrap(wholePackage);

        byte bMagic = buffer.get();
        if (bMagic != Package.bMagic) throw new Exception("Wrong magic byte");

        byte bSrc = buffer.get();
        long packetId = buffer.getLong();
        int wLen = buffer.getInt();
        short wCrc16_start = buffer.getShort();

        byte[] header = ByteBuffer.allocate(HEADER_LENGTH)
                .put(bMagic)
                .put(bSrc)
                .putLong(packetId)
                .putInt(wLen).array();

        if (CRC16.getCrc16(header) != wCrc16_start) throw new Exception("Wrong header CRC");

        int encodedMessageLength = wholePackage.length - HEADER_LENGTH - CRC_LENGTH - CRC_LENGTH;
        byte[] wholeMessage = new byte[encodedMessageLength];
        buffer.get(wholeMessage, 0, encodedMessageLength);
        Message message = new MessageDecoder(wholeMessage).getMessage();

        short wCrc16_end = buffer.getShort();

        byte[] mes = ByteBuffer.allocate(wholePackage.length - CRC_LENGTH)
                .put(bMagic)
                .put(bSrc)
                .putLong(packetId)
                .putInt(wLen)
                .putShort(wCrc16_start)
                .put(wholeMessage)
                .array();

        if (CRC16.getCrc16(mes) != wCrc16_end) throw new Exception("Wrong package CRC");
        return new Package(bSrc, packetId, wLen, message);
    }
}
