package dev.boxadactle.macrocraft.fs;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;

/**
 * This class is used to encrypt and decrypt macro files
 * using AES encryption with a 128-bit key derived from the string "macrocraft".
 */
public class Crypto {
    private static final String KEY = "macrocraft";
    private static final String ALGORITHM = "AES";

    private static SecretKeySpec getKeySpec() {
        byte[] keyBytes = new byte[16]; // AES key size
        byte[] b = KEY.getBytes(StandardCharsets.UTF_8);
        int len = b.length;
        if (len > keyBytes.length) len = keyBytes.length;
        System.arraycopy(b, 0, keyBytes, 0, len);
        return new SecretKeySpec(keyBytes, ALGORITHM);
    }

    public static byte[] encrypt(String data) throws Exception {
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, getKeySpec());
        return cipher.doFinal(data.getBytes(StandardCharsets.UTF_8));
    }

    public static String decrypt(byte[] encryptedData) throws Exception {
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, getKeySpec());
        byte[] original = cipher.doFinal(encryptedData);
        return new String(original);
    }
}

