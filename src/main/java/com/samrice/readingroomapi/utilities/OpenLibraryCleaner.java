package com.samrice.readingroomapi.utilities;

import java.util.List;

public class OpenLibraryCleaner {

    public static String formatKey(String key) {
        return key.substring(key.indexOf("/", key.indexOf("/") + 1) + 1);
    }

    public static String formatIsbn(List<String> isbn) {
        return isbn != null && !isbn.isEmpty() ? isbn.get(0).replace("-", "").replace("—", "") : null;
    }
}
