package com.metroid2010.mnemonicpasswordgenerator;

import org.junit.Before;
import org.junit.Test;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import static org.junit.Assert.*;


public class WordDictionaryTest {

    private WordDictionary wd;

    private InputStream getResourceDictionary() throws IOException {
        InputStream is = this.getClass().getClassLoader().getResourceAsStream("test_dictionary");
        if (is == null) {
            System.exit(1);
        }
        return is;
    }

    @Before
    public void setUp() throws IOException {
        InputStream test_dictionary = this.getResourceDictionary();
        this.wd = new WordDictionary(test_dictionary, "test");
    }

    @Test
    public void wordDictionary_whenRequestWordAtPosition_ReturnCorrectWord() {
        assertEquals(this.wd.fetch_word(199), "test200");
        assertEquals(this.wd.fetch_word(0), "test1");
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void wordDictionary_whenRequestWordAtPositionOutOfBounds_RaiseException() {
        this.wd.fetch_word(1000);
    }

    @Test
    public void wordDictionary_whenGetDictionaryLength_returnCorrectLength() {
        assertEquals(this.wd.get_length(), 200);
    }

}