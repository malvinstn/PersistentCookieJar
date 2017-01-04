package com.franmontiel.persistentcookiejar.persistence;

import android.util.Log;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import okhttp3.Cookie;

/**
 * Implementation of {@link CookieSerializer} that wraps the {@link Cookie} with
 * {@link SerializableCookie} in order to use it with {@link ObjectInputStream} and {@link
 * ObjectOutputStream} for deserializing/ serializing the {@link Cookie} from/ to its Hex String
 * representation.
 */
public class HexStringCookieSerializer implements CookieSerializer {
    private static final String TAG = HexStringCookieSerializer.class.getSimpleName();

    @Override
    public Cookie deserialize(String serializedCookie) {
        byte[] bytes = hexStringToByteArray(serializedCookie);
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(
                bytes);

        Cookie cookie = null;
        ObjectInputStream objectInputStream = null;
        try {
            objectInputStream = new ObjectInputStream(byteArrayInputStream);
            cookie = ((SerializableCookie) objectInputStream.readObject()).cookie;
        } catch (IOException e) {
            Log.d(TAG, "IOException in decodeCookie", e);
        } catch (ClassNotFoundException e) {
            Log.d(TAG, "ClassNotFoundException in decodeCookie", e);
        } finally {
            if (objectInputStream != null) {
                try {
                    objectInputStream.close();
                } catch (IOException e) {
                    Log.d(TAG, "Stream not closed in decodeCookie", e);
                }
            }
        }
        return cookie;
    }

    @Override
    public String serialize(Cookie cookie) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = null;

        try {
            objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
            objectOutputStream.writeObject(new SerializableCookie(cookie));
        } catch (IOException e) {
            Log.d(TAG, "IOException in encodeCookie", e);
            return null;
        } finally {
            if (objectOutputStream != null) {
                try {
                    // Closing a ByteArrayOutputStream has no effect, it can be used later (and is used in the return statement)
                    objectOutputStream.close();
                } catch (IOException e) {
                    Log.d(TAG, "Stream not closed in encodeCookie", e);
                }
            }
        }

        return byteArrayToHexString(byteArrayOutputStream.toByteArray());
    }

    /**
     * Using some super basic byte array &lt;-&gt; hex conversions so we don't
     * have to rely on any large Base64 libraries. Can be overridden if you
     * like!
     *
     * @param bytes byte array to be converted
     * @return string containing hex values
     */
    private static String byteArrayToHexString(byte[] bytes) {
        StringBuilder sb = new StringBuilder(bytes.length * 2);
        for (byte element : bytes) {
            int v = element & 0xff;
            if (v < 16) {
                sb.append('0');
            }
            sb.append(Integer.toHexString(v));
        }
        return sb.toString();
    }

    /**
     * Converts hex values from strings to byte array
     *
     * @param hexString string of hex-encoded values
     * @return decoded byte array
     */
    private static byte[] hexStringToByteArray(String hexString) {
        int len = hexString.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(hexString.charAt(i), 16) << 4) + Character
                    .digit(hexString.charAt(i + 1), 16));
        }
        return data;
    }

}