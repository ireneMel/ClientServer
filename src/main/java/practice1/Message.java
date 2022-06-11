package practice1;

import lombok.*;

import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.util.Random;

@Getter
@Setter
@NoArgsConstructor
@ToString
@EqualsAndHashCode
public class Message {
    private int cType; //код команди
    private int userId;
    private byte[] messageBody;

    private static SecretKey key;
    private static IvParameterSpec iv;

    public Message(int cType, int userId, byte[] messageBody) {
        this.cType = cType;
        this.messageBody = messageBody;
        this.userId = userId;
    }

    public static SecretKey generateKey(long seed) {
        byte[] tmp = new byte[16];
        new Random(seed).nextBytes(tmp);
        return new SecretKeySpec(tmp, "AES");
    }
}