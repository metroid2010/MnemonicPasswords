package com.metroid2010.mnemonicpasswords;

import java.io.InputStream;

public class DictionaryLoaderFixture {

    private final String name;

    public DictionaryLoaderFixture(String name) {
        this.name = name;
    }

    public InputStream getStream() {
        InputStream is = this.getClass().getClassLoader().getResourceAsStream(name);
        if (is == null) {
            System.exit(1);
        }
        return is;
    }
}
