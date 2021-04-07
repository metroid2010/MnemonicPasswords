package com.metroid2010.mnemonicpasswords;

import org.junit.Test;

import java.util.Arrays;
import java.util.Vector;

import static org.junit.Assert.assertEquals;


public class PasswordTest {

    @Test
    public void Password_WhenCreatePassWithWhitespace_ReturnTrimmedPass() {
        Vector<String> al = new Vector<>(Arrays.asList("test1", "test2 ", "  test3 "));
        Password pwd = new Password(al);
        assertEquals(pwd.toString(), "test1 test2 test3");
    }

}