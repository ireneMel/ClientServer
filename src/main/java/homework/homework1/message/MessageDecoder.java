package homework.homework1.message;

import javax.crypto.Cipher;
import java.nio.ByteBuffer;

public class MessageDecoder {
    private final byte[] wholeMessage;

    public MessageDecoder(byte[] wholeMessage) {
        this.wholeMessage = wholeMessage;
    }

    public Message getMessage() {
        ByteBuffer buffer = ByteBuffer.wrap(decode(wholeMessage));

        int cType = buffer.getInt();
        int userId = buffer.getInt();
        byte[] messageBody = new byte[buffer.array().length - 8];
        buffer.get(messageBody, 0, buffer.array().length - 8);
        return new Message(cType, userId, messageBody);
    }

    private byte[] decode(byte[] message) {
        try {
            Cipher c = Cipher.getInstance("AES");
            c.init(Cipher.DECRYPT_MODE, MessageKey.key);
            return c.doFinal(message);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
