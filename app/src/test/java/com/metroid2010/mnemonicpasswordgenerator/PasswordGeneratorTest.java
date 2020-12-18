package com.metroid2010.mnemonicpasswordgenerator;

import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Vector;
import java.util.regex.Pattern;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;


public class PasswordGeneratorTest {

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
    public void passwordGenerator_whenGeneratePasswordOfFourWords_returnStringFourRandomWords() {
        PasswordGenerator pwg = new PasswordGenerator(this.wd);
        String pwd_regex = "([a-zA-Z\\'0-9]+[\\s]){3}(([a-zA-Z\\'0-9]+))";
        assertTrue(pwg.generate_password().toString().matches(pwd_regex));
    }

}