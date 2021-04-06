package com.metroid2010.mnemonicpasswordgenerator;

import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import static org.junit.Assert.assertTrue;


public class PasswordGeneratorTest {

    private WordDictionary wd;

    @Before
    public void setUp() throws IOException {
        InputStream test_dictionary = new DictionaryLoaderFixture("test_dictionary").getStream();
        this.wd = new WordDictionary(test_dictionary, "test");
    }

    @Test
    public void PasswordGenerator_WhenGeneratePasswordOfFourWords_ReturnStringFourRandomWords() {
        PasswordGenerator pwg = new PasswordGenerator(this.wd, 4);
        String pwd_regex = "([a-zA-Z'0-9]+[\\s]){3}([a-zA-Z'0-9]+)";
        assertTrue(pwg.generate_password().toString().matches(pwd_regex));
    }

}