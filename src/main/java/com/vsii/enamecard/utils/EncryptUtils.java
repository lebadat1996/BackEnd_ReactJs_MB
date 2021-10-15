package com.vsii.enamecard.utils;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.nio.charset.Charset;
import java.util.Base64;
import java.util.Random;
import java.util.zip.CRC32;


public final class EncryptUtils {

    private EncryptUtils() {
    }

    private static PasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();

    public static String getBcryptPasswordEncoder(String pass) {
        return bCryptPasswordEncoder.encode(pass);
    }

    public static String crc32PasswordEncoder(String pass) {
        CRC32 crc32 = new CRC32();
        crc32.update(pass.getBytes());
        return Long.toHexString(crc32.getValue());
    }


    public static String decodeString(String data) {
        byte[] decodedBytes = Base64.getDecoder().decode(data);
        String dataDecode = new String(decodedBytes);
        return dataDecode;
    }

    public static String getAlphaNumericString(int n)
    {
        byte[] array = new byte[256];
        new Random().nextBytes(array);

        String randomString = new String(array, Charset.forName("UTF-8"));

        StringBuffer r = new StringBuffer();

        String AlphaNumericString
                = randomString
                .replaceAll("[^A-Za-z0-9]", "");

        for (int k = 0; k < AlphaNumericString.length(); k++) {

            if (Character.isLetter(AlphaNumericString.charAt(k))
                    && (n > 0)
                    || Character.isDigit(AlphaNumericString.charAt(k))
                    && (n > 0)) {

                r.append(AlphaNumericString.charAt(k));
                n--;
            }
        }

        // return the resultant string
        return r.toString();
    }
}
