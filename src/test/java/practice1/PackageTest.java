package practice1;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

//тести мають бути без модифікаторів доступу
class PackageTest {

    @Test
    void shouldHandlePackage() throws Exception {
        byte[] test = new byte[]{1, 2, 3, 4, 5};
        Message message = new Message(1, 1, test);

        Package aPackage = new Package((byte) 1, 1, 13, message);

        PackageEncoder packageEncoder = new PackageEncoder(aPackage);
        byte[] pac = packageEncoder.getBytes();

        PackageDecoder packageDecoder = new PackageDecoder(pac);

        assertEquals(packageDecoder.getPackage(), aPackage);
    }

    @Test
    void shouldHandleInvalidCrc_header() {
        byte[] test = new byte[]{1, 2, 3, 4, 5};
        Message message = new Message(1, 1, test);

        Package aPackage = new Package((byte) 1, 1, 13, message);
        PackageEncoder packageEncoder = new PackageEncoder(aPackage);
        byte[] pac = packageEncoder.getBytes();

        pac[1] = 0x1;
        pac[10] = 0x1;

        PackageDecoder packageDecoder = new PackageDecoder(pac);
        assertThrows(Exception.class, packageDecoder::getPackage);
    }
}