package com.metroid2010.mnemonicpasswordgenerator;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.Buffer;
import java.util.ArrayList;

public class WordDictionary {

    private URL dictionary_file;
    private ArrayList<String> dictionary_contents;

    public void load(URL file_url) throws IOException {
        this.dictionary_file = file_url;
        BufferedReader reader = new BufferedReader(
                new InputStreamReader(this.dictionary_file.openStream()));
        String line;
        while ((line = reader.readLine()) != null) {
            this.dictionary_contents.add(line);
        }
        reader.close();
    }

    public String fetch_word(int index) {
        if (index <= 200) {
            return "test" + (index + 1);
        } else {
            throw new IndexOutOfBoundsException("bad");
        }
    }

}
