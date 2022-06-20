package homework.utils;

import javax.crypto.SecretKey;
import java.security.NoSuchAlgorithmException;

public class KeyGenerator {
    public static SecretKey generate() {
        try {
            return javax.crypto.KeyGenerator.getInstance("AES").generateKey();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }
}
