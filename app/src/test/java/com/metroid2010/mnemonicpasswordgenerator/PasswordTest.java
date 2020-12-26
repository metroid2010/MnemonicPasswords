package com.metroid2010.mnemonicpasswordgenerator;

import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Vector;

import static org.junit.Assert.assertEquals;


public class PasswordTest {

    @Test
    public void password_whenCreatePassWithWhitespace_returnTrimmedPass() {
        Vector<String> al = new Vector<>(Arrays.asList("test1", "test2 ", "  test3 "));
        Password pwd = new Password(al);
        assertEquals(pwd.toString(), "test1 test2 test3");
    }

}