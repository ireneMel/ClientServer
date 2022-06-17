package practice1.packet;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import practice1.message.Message;

@Getter
@EqualsAndHashCode
public class Package {
    public static final byte bMagic = 0x13;
    public static final int HEADER_LENGTH = 14;

    private final byte bSrc; //Унікальний номер клієнтського застосування
    private final long packetId; //Номер повідомлення
    private final int wLen;
    private final Message message;

    public Package(byte bSrc, long packetId, int wLen, Message message) {
        this.bSrc = bSrc;
        this.packetId = packetId;
        this.message = message;
        this.wLen = wLen;
    }
}