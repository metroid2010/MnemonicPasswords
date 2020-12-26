package com.metroid2010.mnemonicpasswordgenerator;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Vector;

public class WordDictionary {

    private Vector<String> dictionary_contents;

    public WordDictionary(InputStream dictionary_file, String language) throws IOException{
        this.dictionary_contents = this.load(dictionary_file);
    }

    private Vector<String> load(InputStream is) throws IOException {
        Vector<String> contents = new Vector<>();
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        String line;
        while ((line = br.readLine()) != null){
            contents.add(line);
        };
        br.close();
        return contents;
    }

    public String fetch_word(int index) {
        return this.dictionary_contents.get(index);
    }

    public int get_length() { return this.dictionary_contents.size(); }

}
