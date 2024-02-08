package com.samrice.readingroomapi.utilities;

public class OpenLibraryCleaner {

    public static String formatKey(String key) {
        return key.substring(key.indexOf("/", key.indexOf("/") + 1) + 1);
    }
}
