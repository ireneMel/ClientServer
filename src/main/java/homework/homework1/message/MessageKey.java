package homework.homework1.message;

import homework.utils.KeyGenerator;

import javax.crypto.SecretKey;

public class MessageKey {
    public static SecretKey key = KeyGenerator.generate();
}
