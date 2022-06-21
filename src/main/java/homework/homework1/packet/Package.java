package homework.homework1.packet;

import homework.homework1.message.Message;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.With;

@Getter
@EqualsAndHashCode
@With
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