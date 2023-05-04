package com.qwict.isbin.domein;

public class Formatter {
    public String formatISBNToString(String isbn) {
        // Remove spaces and dashes
        return isbn.replaceAll("[\\s\\-()]", "");
    }
}
