package com.franmontiel.persistentcookiejar;

import com.franmontiel.persistentcookiejar.persistence.HexStringCookieSerializer;

import org.junit.Before;
import org.junit.Test;

import okhttp3.Cookie;

import static org.junit.Assert.assertEquals;

public class HexStringCookieSerializerTest {

    private HexStringCookieSerializer serializer;

    @Before
    public void createSerializer() throws Exception {
        serializer = new HexStringCookieSerializer();
    }

    @Test
    public void cookieSerialization() throws Exception {
        Cookie cookie = TestCookieCreator.createPersistentCookie(false);

        String serializedCookie = serializer.serialize(cookie);
        Cookie deserializedCookie = serializer.deserialize(serializedCookie);

        assertEquals(cookie, deserializedCookie);
    }

    @Test
    public void hostOnlyDomainCookieSerialization() throws Exception {
        Cookie cookie = TestCookieCreator.createPersistentCookie(true);

        String serializedCookie = serializer.serialize(cookie);
        Cookie deserializedCookie = serializer.deserialize(serializedCookie);

        assertEquals(cookie, deserializedCookie);
    }

    @Test
    public void nonPersistentCookieSerialization() throws Exception {
        Cookie cookie = TestCookieCreator.createNonPersistentCookie();

        String serializedCookie = serializer.serialize(cookie);
        Cookie deserializedCookie = serializer.deserialize(serializedCookie);

        assertEquals(cookie, deserializedCookie);
    }
}