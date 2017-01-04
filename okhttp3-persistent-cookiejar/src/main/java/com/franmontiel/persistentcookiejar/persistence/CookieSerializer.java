package com.franmontiel.persistentcookiejar.persistence;

import okhttp3.Cookie;

/**
 * Cookie serializer which handles serializing/ deserializing a {@link okhttp3.Cookie}
 */
public interface CookieSerializer {
    Cookie deserialize(String serializedCookie);

    String serialize(Cookie cookie);
}
