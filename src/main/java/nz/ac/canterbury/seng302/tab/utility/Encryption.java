package nz.ac.canterbury.seng302.tab.utility;

import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Helper class for encrypting and decrypting the Garmin API access token
 */
@Component
public class Encryption {

    @Value("${encryption.secret.key}")
    String secretKeyString;
    Logger logger = LoggerFactory.getLogger(Encryption.class);

    /**
     * Encrypt using AES in ECB mode
     *
     * @param plainText secret api token
     * @return encrypted secret key to be put in database
     * @throws NoSuchPaddingException    If the specified padding scheme is not available.
     * @throws NoSuchAlgorithmException  If the specified encryption algorithm is not available.
     * @throws InvalidKeyException       If the provided key is invalid for encryption.
     * @throws IllegalBlockSizeException If the block size is invalid for the encryption.
     * @throws BadPaddingException       If the padding is incorrect or corrupted.
     */
    public String encrypt(String plainText) {
        // Convert the secret key string to bytes
        byte[] secretKeyBytes = secretKeyString.getBytes(StandardCharsets.UTF_8);
        SecretKey secretKey = new SecretKeySpec(secretKeyBytes, "AES");
        try {
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            byte[] encryptedBytes = cipher.doFinal(plainText.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(encryptedBytes);
        } catch (NoSuchPaddingException | IllegalBlockSizeException
                 | NoSuchAlgorithmException | BadPaddingException | InvalidKeyException e) {
            logger.error(e.toString());
        }
        return null;
    }

    /**
     * Decrypt using AES in ECB mode
     *
     * @param encryptedText encrypted secret api token
     * @return decrypted secret key to be used in calls to the Garmin API
     * @throws NoSuchPaddingException    If the specified padding scheme is not available.
     * @throws NoSuchAlgorithmException  If the specified encryption algorithm is not available.
     * @throws InvalidKeyException       If the provided key is invalid for encryption.
     * @throws IllegalBlockSizeException If the block size is invalid for the encryption.
     * @throws BadPaddingException       If the padding is incorrect or corrupted.
     */
    public String decrypt(String encryptedText) {
        // Convert the secret key string to bytes
        byte[] secretKeyBytes = secretKeyString.getBytes(StandardCharsets.UTF_8);
        SecretKey secretKey = new SecretKeySpec(secretKeyBytes, "AES");
        try {
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, secretKey);
            byte[] encryptedBytes = Base64.getDecoder().decode(encryptedText);
            byte[] decryptedBytes = cipher.doFinal(encryptedBytes);
            return new String(decryptedBytes, StandardCharsets.UTF_8);
        } catch (NoSuchPaddingException | IllegalBlockSizeException
                 | NoSuchAlgorithmException | BadPaddingException | InvalidKeyException e) {
            logger.error(e.toString());
        }
        return null;
    }
}
