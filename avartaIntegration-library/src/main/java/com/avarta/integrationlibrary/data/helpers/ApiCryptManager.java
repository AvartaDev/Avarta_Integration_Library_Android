package com.avarta.integrationlibrary.data.helpers;

import android.util.Base64;
import android.util.Log;

import com.avarta.integrationlibrary.utils.CrashLogging;

import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class ApiCryptManager {

    public static String TAG = "HELLMEN";

    static {
        try {
            System.loadLibrary("skm");
            Log.e(TAG, "Loaded SKM library.");
        } catch (UnsatisfiedLinkError e) {
            e.printStackTrace();
            Log.e(TAG, "Error loading SKM library: " + e.getMessage());
        }
    }

    public static String getPassword() {
        return decrypt(Base64.decode(CryptConstants.get(CryptConstants.PWD), Base64.DEFAULT));
    }

    public static String getCipher() {
        return decrypt(Base64.decode(CryptConstants.get(CryptConstants.CIPHER), Base64.DEFAULT));
    }

    public static String getVector() {
        return decrypt(Base64.decode(CryptConstants.get(CryptConstants.VECTOR), Base64.DEFAULT));
    }

    public static String encryptItemData(String itemData) {
        return encryptItemData(itemData, "");
    }

    public static String encryptItemData(String itemData, String salt) {
        try {
            String in = salt + itemData + getCipher();

            byte[] encrypted = encrypt(in, getPassword().getBytes(CryptConstants.get(CryptConstants.CHARSET)),
                    getVector().getBytes(CryptConstants.get(CryptConstants.CHARSET)));

            return Base64.encodeToString(encrypted, Base64.NO_WRAP);
        } catch (Exception exception) {
            CrashLogging.LogCrashException(exception, "ApiCryptManager class -> encryptItemData method");
            exception.printStackTrace();
        }
        return null;
    }

    public static String encryptItemData(String itemData, String salt, byte[] secretkey) {
        try {
            String in = salt + itemData + getCipher();

            byte[] encrypted = encrypt(in, secretkey,
                    getVector().getBytes(CryptConstants.get(CryptConstants.CHARSET)));

            return Base64.encodeToString(encrypted, Base64.NO_WRAP);
        } catch (Exception exception) {
            CrashLogging.LogCrashException(exception, "encryptItemData method");
            exception.printStackTrace();
        }
        return null;
    }

    public static String decryptSalt(String salt) {
        try {
            byte[] message = Base64.decode(salt, Base64.DEFAULT);

            byte[] decrypted = decrypt(message, getPassword().getBytes(CryptConstants.get(CryptConstants.CHARSET)),
                    getVector().getBytes(CryptConstants.get(CryptConstants.CHARSET)));

            String result = new String(decrypted);
            return result.replace(getCipher(), "");
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return null;
    }

    public static String decryptSalt(String salt, byte[] secretkey) {
        try {
            byte[] message = Base64.decode(salt, Base64.DEFAULT);

            byte[] decrypted = decrypt(message, secretkey,
                    getVector().getBytes(CryptConstants.get(CryptConstants.CHARSET)));

            String result = new String(decrypted);
            return result.replace(getCipher(), "");
        } catch (Exception exception) {
            CrashLogging.LogCrashException(exception, "decrypt salt method");
            exception.printStackTrace();
        }
        return null;
    }
    // ******************

    private static byte[] encrypt(String message, byte[] keyBytes, byte[] iv) throws GeneralSecurityException {
        try {
            SecretKeySpec key = new SecretKeySpec(keyBytes, CryptConstants.get(CryptConstants.AES));
            final Cipher cipher = Cipher.getInstance(CryptConstants.get(CryptConstants.AES_MODE));
            IvParameterSpec ivParams = new IvParameterSpec(iv);
            cipher.init(Cipher.ENCRYPT_MODE, key, ivParams);//, ivSpec
            return cipher.doFinal(message.getBytes(CryptConstants.get(CryptConstants.CHARSET)));
        } catch (UnsupportedEncodingException e) {
            CrashLogging.LogCrashException(new GeneralSecurityException(e), "Encrypt from ApiCryptManager class");
            throw new GeneralSecurityException(e);
        }
    }

    private static byte[] decrypt(byte[] bytes, byte[] keyBytes, byte[] iv) throws GeneralSecurityException {
        try {
            SecretKeySpec key = new SecretKeySpec(keyBytes, CryptConstants.get(CryptConstants.AES));
            final Cipher cipher = Cipher.getInstance(CryptConstants.get(CryptConstants.AES_MODE));
            IvParameterSpec ivParams = new IvParameterSpec(iv);
            cipher.init(Cipher.DECRYPT_MODE, key, ivParams);//, ivSpec
            byte[] decryptedBytes = cipher.doFinal(bytes);
            return decryptedBytes;
        } catch (Exception e) {
            CrashLogging.LogCrashException(new GeneralSecurityException(e), "Decrypt from ApiCryptManager class");
            throw new GeneralSecurityException(e);
        }
    }

    public static native byte[] encrypt(byte[] _inString);

    public static native String decrypt(byte[] _inString);
}
