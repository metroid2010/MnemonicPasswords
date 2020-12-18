package com.metroid2010.mnemonicpasswordgenerator;

import java.util.Iterator;
import java.util.Vector;

public class Password {

    private Vector<String> content;

    public Password(Vector<String> content) {
        this.content = content;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        Iterator it = this.content.iterator();
        while (it.hasNext()) {
            sb.append(it.next().toString().trim() + " ");
        }
        return sb.toString().trim();
    }

}
