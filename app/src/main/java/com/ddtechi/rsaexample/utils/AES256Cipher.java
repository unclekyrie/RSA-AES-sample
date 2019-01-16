package com.ddtechi.rsaexample.utils;

import android.util.Base64;
import android.util.Log;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.InvalidAlgorithmParameterException;
import java.security.SecureRandom;
import java.security.spec.AlgorithmParameterSpec;

public class AES256Cipher {

    public static byte[] getRandomAesCryptKey() {
        try {
            MessageDigest sha256Hash = MessageDigest.getInstance("SHA-256");
            sha256Hash.update(Constants.AES256_KEY.getBytes(StandardCharsets.UTF_8));

            Log.i("key", Integer.toString(sha256Hash.digest().length));
            return sha256Hash.digest();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();

            return null;
        }
    }

    public static byte[] getRandomAesCryptIv() {
        byte[] randomBytes = new byte[16];
        new SecureRandom().nextBytes(randomBytes);

        return new IvParameterSpec(randomBytes).getIV();
    }

    public static String encrypt(byte[] aesCryptKey, byte[] aesCryptIv, String plainText)
            throws java.io.UnsupportedEncodingException,
            NoSuchAlgorithmException,
            NoSuchPaddingException,
            InvalidKeyException,
            InvalidAlgorithmParameterException,
            IllegalBlockSizeException,
            BadPaddingException {

        AlgorithmParameterSpec ivSpec = new IvParameterSpec(aesCryptIv);
        SecretKeySpec newKey = new SecretKeySpec(aesCryptKey, "AES");
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, newKey, ivSpec);

        return Base64.encodeToString(cipher.doFinal(plainText.getBytes(StandardCharsets.UTF_8)), Base64.DEFAULT);
    }

    public static String decrypt(byte[] aesCryptKey, byte[] aesCryptIv, String cipherText)
            throws java.io.UnsupportedEncodingException,
            NoSuchAlgorithmException,
            NoSuchPaddingException,
            InvalidKeyException,
            InvalidAlgorithmParameterException,
            IllegalBlockSizeException,
            BadPaddingException {

        AlgorithmParameterSpec ivSpec = new IvParameterSpec(aesCryptIv);
        SecretKeySpec newKey = new SecretKeySpec(aesCryptKey, "AES");
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, newKey, ivSpec);

        return new String(cipher.doFinal(Base64.decode(cipherText, Base64.DEFAULT)), "UTF-8");
    }
}
