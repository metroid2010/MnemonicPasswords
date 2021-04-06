package com.metroid2010.mnemonicpasswordgenerator;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Vector;

public class WordDictionary {

    private final Vector<String> dictionary_contents;
    private final String dictionary_name;

    public WordDictionary(InputStream dictionary_file, String dictionary_name) throws IOException{
        this.dictionary_contents = this.load_from_is(dictionary_file);
        this.dictionary_name = dictionary_name;
    }

    public String get_dictionary_name() { return this.dictionary_name; }

    private Vector<String> load_from_is(InputStream is) throws IOException {
        Vector<String> contents = new Vector<>();
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        String line;
        while ((line = br.readLine()) != null){
            contents.add(line);
        }
        br.close();
        return contents;
    }

    public String fetch_word(int index) {
        return this.dictionary_contents.get(index);
    }

    public int get_length() { return this.dictionary_contents.size(); }

}
