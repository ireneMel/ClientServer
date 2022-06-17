package practice1.message;

import practice1.utils.KeyGenerator;

import javax.crypto.SecretKey;

public class MessageKey {
    public static SecretKey key = KeyGenerator.generate();
}
