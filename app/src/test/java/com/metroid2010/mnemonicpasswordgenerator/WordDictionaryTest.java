package com.metroid2010.mnemonicpasswordgenerator;

import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.net.URL;

import static org.junit.Assert.*;


public class WordDictionaryTest {

    private WordDictionary wd;

    private URL getResourceDictionary() throws IOException {
        return this.getClass().getClassLoader().getResource("test_dictionary");
    }

    @Before
    public void setUp() throws IOException {
        this.wd = new WordDictionary();
        URL test_dictionary = this.getResourceDictionary();
        this.wd.load(test_dictionary);
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

}