package com.metroid2010.mnemonicpasswordgenerator;

import java.util.Vector;

public class Password {

    private final Vector<String> content;

    public Password(Vector<String> content) {
        this.content = content;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (String word : content) {
            sb.append(word.trim()).append(" ");
        }
        return sb.toString().trim();
    }

}
