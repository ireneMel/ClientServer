package homework.message;

import javax.crypto.Cipher;
import java.nio.ByteBuffer;

public class MessageEncoder {
    private final Message message;
    private final long seed;

    public MessageEncoder(Message message, long seed) {
        this.message = message;
        this.seed = seed;
    }

    public byte[] getBytes() {
        ByteBuffer buffer = ByteBuffer.allocate(message.getMessageBody().length + 8)
                .putInt(message.getCType())
                .putInt(message.getUserId())
                .put(message.getMessageBody());
        return encode(buffer.array());
    }

    private byte[] encode(byte[] message) {
        try {
            Cipher c = Cipher.getInstance("AES");
            c.init(Cipher.ENCRYPT_MODE, Message.generateKey(seed));
            return c.doFinal(message);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
