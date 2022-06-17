package homework.message;

import homework.utils.KeyGenerator;

import javax.crypto.SecretKey;

class MessageKey {
    static SecretKey key = KeyGenerator.generate();
}
