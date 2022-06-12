package com.metroid2010.mnemonicpasswords;

import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.TimeoutException;

public class PasswordFilterTest {

    private WordDictionary wd;

    @Before
    public void setUp() throws IOException {
        InputStream test_dictionary = new DictionaryLoaderFixture("test_dictionary").getStream();
        this.wd = new WordDictionary(test_dictionary, "test");
    }

    @Test
    public void PasswordFilter_WhenGeneratePasswordWithNoApostropheFilterPerWord_ReturnValidPassword() throws TimeoutException {
        PasswordGenerator pwg = new PasswordGenerator(this.wd, 1);
        PasswordFilter filter = new PasswordFilter("([a-zA-Z0-9])+", "");
        pwg.add_filter(filter);
        String pwd_regex = "([a-z0-9A-Z]+)";
        for (int i = 0; i < 100; i++) { // try several times
            assertTrue(pwg.generate_password().toString().matches(pwd_regex));
        }
    }

    @Test
    public void PasswordFilter_WhenGeneratePasswordWithNoNumbersFilterWhole_ReturnValidPassword() throws TimeoutException {
        PasswordGenerator pwg = new PasswordGenerator(this.wd, 1);
        PasswordFilter filter = new PasswordFilter("([a-z'A-Z])+", "");
        pwg.add_filter(filter);
        String pwd_regex = "([a-z'A-Z]+)";
        for (int i = 0; i < 100; i++) { // try several times
            assertTrue(pwg.generate_password().toString().matches(pwd_regex));
        }
    }
}
