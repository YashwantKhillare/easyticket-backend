package com.org.easyticket.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.security.*;
import java.security.spec.AlgorithmParameterSpec;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.InvalidParameterSpecException;
import java.util.Base64;

public class CRSAESEncryptor {

    private static final String password = "V99UA8A7S7F2KD0DK20DJ29DJ29DJ29G";
    private static Cipher cipher;
    private static final String ALGORITHM = "AES";
    private static byte[] keyValue = null;
    private static byte[] iV = null;
    private static final String TYPE = "AES/CBC/PKCS5Padding";
    private static final String STRIV = "INTENSE-EBPPADMN";
    private static final Logger log = LoggerFactory.getLogger(CRSAESEncryptor.class);

    private CRSAESEncryptor() {
        throw new IllegalStateException("This is a utility class and cannot be instantiated");
    }

    public static String encrypt(String word) throws InvalidParameterSpecException, InvalidKeyException,
            IllegalBlockSizeException, UnsupportedEncodingException, BadPaddingException, InvalidKeySpecException,
            NoSuchAlgorithmException, NoSuchPaddingException {

        byte[] ivBytes;

        SecureRandom random = new SecureRandom();
        byte[] bytes = new byte[20];
        random.nextBytes(bytes);
        byte[] saltBytes = bytes;

        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
        PBEKeySpec spec = new PBEKeySpec(password.toCharArray(), saltBytes, 65556, 256);
        SecretKey secretKey = factory.generateSecret(spec);
        SecretKeySpec secret = new SecretKeySpec(secretKey.getEncoded(), ALGORITHM);
        Cipher cipher = Cipher.getInstance(TYPE);

        cipher.init(Cipher.ENCRYPT_MODE, secret);
        AlgorithmParameters params = cipher.getParameters();
        ivBytes = params.getParameterSpec(IvParameterSpec.class).getIV();
        byte[] encryptedTextBytes = cipher.doFinal(word.getBytes("UTF-8"));
        byte[] buffer = new byte[saltBytes.length + ivBytes.length + encryptedTextBytes.length];

        System.arraycopy(saltBytes, 0, buffer, 0, saltBytes.length);
        System.arraycopy(ivBytes, 0, buffer, saltBytes.length, ivBytes.length);
        System.arraycopy(encryptedTextBytes, 0, buffer, saltBytes.length + ivBytes.length, encryptedTextBytes.length);

        return Base64.getEncoder().encodeToString(buffer);
    }

    public static String decrypt(String encryptedText) throws NoSuchAlgorithmException, NoSuchPaddingException,
            InvalidKeySpecException, InvalidKeyException, InvalidAlgorithmParameterException {

        Cipher cipher = Cipher.getInstance(TYPE);
        ByteBuffer buffer = ByteBuffer.wrap(Base64.getDecoder().decode(encryptedText));

        byte[] saltBytes = new byte[20];
        buffer.get(saltBytes, 0, saltBytes.length);
        byte[] ivBytes1 = new byte[cipher.getBlockSize()];
        buffer.get(ivBytes1, 0, ivBytes1.length);
        byte[] encryptedTextBytes = new byte[buffer.capacity() - saltBytes.length - ivBytes1.length];

        buffer.get(encryptedTextBytes);

        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
        PBEKeySpec spec = new PBEKeySpec(password.toCharArray(), saltBytes, 65556, 256);
        SecretKey secretKey = factory.generateSecret(spec);
        SecretKeySpec secret = new SecretKeySpec(secretKey.getEncoded(), ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, secret, new IvParameterSpec(ivBytes1));

        byte[] decryptedTextBytes = null;
        try {
            decryptedTextBytes = cipher.doFinal(encryptedTextBytes);
        } catch (IllegalBlockSizeException | BadPaddingException e) {
            log.error("Exception Occured", e);
        }

        return new String(decryptedTextBytes);
    }

    public static String encrypt(String plainText, String strKey) throws NoSuchAlgorithmException,
            NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException {

        String encryptedText = "";
        try {
            if ((plainText != null) && (plainText.trim().length() != 0) && (strKey != null)
                    && (strKey.trim().length() == 16)) {

                keyValue = Base64.getEncoder().encode(strKey.getBytes());
                iV = Base64.getEncoder().encode(STRIV.getBytes());
                SecretKey key = new SecretKeySpec(Base64.getDecoder().decode(new String(keyValue)), ALGORITHM);
                AlgorithmParameterSpec iv = new IvParameterSpec(Base64.getDecoder().decode(new String(iV)));
                cipher = Cipher.getInstance(TYPE);
                cipher.init(Cipher.ENCRYPT_MODE, key, iv);
                encryptedText = Base64.getEncoder().encodeToString(cipher.doFinal(plainText.getBytes("UTF-8")));
            }
        } catch (Exception ex) {
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            ex.printStackTrace(pw);
            String strErrMsg = sw.toString();
            log.error("Exception during encryption: {}", strErrMsg);
        }

        return encryptedText;
    }

    public static String decrypt(String encryptedText, String strKey) throws NoSuchAlgorithmException,
            NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException {

        String decryptedText = "";
        try {
            if ((encryptedText != null) && (encryptedText.trim().length() != 0) && (strKey != null)
                    && (strKey.trim().length() == 16)) {

                keyValue = Base64.getEncoder().encode(strKey.getBytes());
                iV = Base64.getEncoder().encode(STRIV.getBytes());
                SecretKey key = new SecretKeySpec(Base64.getDecoder().decode(new String(keyValue)), ALGORITHM);
                AlgorithmParameterSpec iv = new IvParameterSpec(Base64.getDecoder().decode(new String(iV)));
                cipher = Cipher.getInstance(TYPE);
                cipher.init(Cipher.DECRYPT_MODE, key, iv);
                byte[] encryptedTextByte = Base64.getDecoder().decode(encryptedText);
                byte[] decryptedByte = cipher.doFinal(encryptedTextByte);
                decryptedText = new String(decryptedByte);
            }
        } catch (Exception ex) {
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            ex.printStackTrace(pw);
            String strErrMsg = sw.toString();
            log.error("Exception during decryption: {}", strErrMsg);
        }

        return decryptedText;
    }


}