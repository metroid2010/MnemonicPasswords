package com.metroid2010.mnemonicpasswordgenerator;

import java.security.SecureRandom;
import java.util.Vector;

public class PasswordGenerator {

    private int password_length;
    private WordDictionary word_dictionary;

    public PasswordGenerator(WordDictionary wd) {
        this.password_length = 4;
        this.word_dictionary = wd;
    }

    public Password generate_password(){
        return new Password(this.build_random_word_vector(
                this.password_length,
                this.word_dictionary));
    }

    private int generate_random_number() {
        SecureRandom rng = new SecureRandom();
        return rng.nextInt();
    }

    private Vector<String> build_random_word_vector(int length, WordDictionary wd) {
        Vector<String> word_vector = new Vector<>();
        for (int i=0; i<length; i++) {
            word_vector.add(wd.fetch_word(this.generate_random_number()));
        }
        return word_vector;
    }

}
