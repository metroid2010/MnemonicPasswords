package com.metroid2010.mnemonicpasswords;

import org.junit.Before;
import org.junit.Test;
import java.io.IOException;
import java.io.InputStream;

import static org.junit.Assert.*;


public class WordDictionaryTest {

    private WordDictionary wd;


    @Before
    public void setUp() throws IOException {
        InputStream test_dictionary = new DictionaryLoaderFixture("test_dictionary").getStream();
        this.wd = new WordDictionary(test_dictionary, "test");
    }

    @Test
    public void WordDictionary_WhenRequestWordAtPosition_ReturnCorrectWord() {
        assertEquals(this.wd.fetch_word(199), "test200");
        assertEquals(this.wd.fetch_word(0), "test1");
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void WordDictionary_WhenRequestWordAtPositionOutOfBounds_RaiseException() {
        this.wd.fetch_word(1000);
    }

    @Test
    public void WordDictionary_WhenGetDictionaryLength_ReturnCorrectLength() {
        assertEquals(this.wd.get_length(), 260);
    }

}