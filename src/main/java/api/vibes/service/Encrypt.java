/*
 * 
 */
package api.vibes.service;

import java.util.Base64;
import java.nio.charset.Charset;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import java.lang.Exception;
import org.springframework.stereotype.Service;

/**
 * ;
 */
public class Encrypt {

    private final String ALGORITHM = "Blowfish";

    public String encrypt(String password, String key) {
        try{
            byte[] KeyData = key.getBytes();
            SecretKeySpec KS = new SecretKeySpec(KeyData, ALGORITHM);
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, KS);
            String encryptedtext = Base64.getEncoder().encodeToString(cipher.doFinal(password.getBytes("UTF-8")));
            return encryptedtext;
        } catch (Exception e) {}
        return null;
    }

    public String decrypt(String encryptedtext, String key) {
        try{
            byte[] KeyData = key.getBytes();
            SecretKeySpec KS = new SecretKeySpec(KeyData, ALGORITHM);
            byte[] ecryptedtexttobytes = Base64.getDecoder().decode(encryptedtext);
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, KS);
            byte[] decrypted = cipher.doFinal(ecryptedtexttobytes);
            String decryptedString = new String(decrypted, Charset.forName("UTF-8"));
            return decryptedString;
        } catch (Exception e) {}
        return null;
    }

    /**
     * online:
     * http://sladex.org/blowfish.js/
     * https://www.lddgo.net/en/encrypt/blowfish
     */
    public void teste() {
        final String secretKey = "secrete28796468673187614";

        String originalString = "javaguides";

        Encrypt aesEncryptionDecryption = new Encrypt();
        String encryptedString = aesEncryptionDecryption.encrypt(originalString, secretKey);
        String decryptedString = aesEncryptionDecryption.decrypt(encryptedString, secretKey);

        System.out.println("Original: " + originalString);
        System.out.println("Encript: " + encryptedString);
        System.out.println("Decript: " + decryptedString);
    }

}
