package com.qwict.isbin.domein;

import org.json.simple.JSONObject;

import java.io.IOException;

public class DomeinController {
    private Formatter formatter;

    public DomeinController() {
        this.formatter = new Formatter();
    }

    public String formatISBNToString(String isbn) {
        return formatter.formatISBNToString(isbn);
    }

    public JSONObject get(String urlString) throws IOException {
        return RemoteAPI.get(urlString);
    }
}
