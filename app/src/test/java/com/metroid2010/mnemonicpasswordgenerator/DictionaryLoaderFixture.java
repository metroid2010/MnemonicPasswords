package com.metroid2010.mnemonicpasswordgenerator;

import java.io.IOException;
import java.io.InputStream;

public class DictionaryLoaderFixture {

    private String name;

    public DictionaryLoaderFixture(String name) {
        this.name = name;
    }

    public InputStream getStream() throws IOException {
        InputStream is = this.getClass().getClassLoader().getResourceAsStream(name);
        if (is == null) {
            System.exit(1);
        }
        return is;
    }
}
