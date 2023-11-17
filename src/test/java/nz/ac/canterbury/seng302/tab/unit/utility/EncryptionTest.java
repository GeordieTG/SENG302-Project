package nz.ac.canterbury.seng302.tab.unit.utility;

import java.lang.reflect.Field;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import nz.ac.canterbury.seng302.tab.utility.Encryption;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class EncryptionTest {
    private final Encryption encryption = new Encryption();
    private final String message = "Test Message";

    @BeforeAll
    void setEncryptionKey() throws IllegalAccessException, NoSuchFieldException {
        String testSecretKey = "j7R9P2sF5tQ1wE8Z";

        // Use Java Reflection to access and modify private field
        Field secretKeyField = Encryption.class.getDeclaredField("secretKeyString");
        secretKeyField.setAccessible(true);
        secretKeyField.set(encryption, testSecretKey);
    }

    @Test
    void encryptTest() {
        String encryptedText = encryption.encrypt(message);
        Assertions.assertNotEquals(message, encryptedText);
    }

    @Test
    void decryptTest() {
        String encryptedText = encryption.encrypt(message);
        String decryptedText = encryption.decrypt(encryptedText);
        Assertions.assertEquals(message, decryptedText);
    }
}
