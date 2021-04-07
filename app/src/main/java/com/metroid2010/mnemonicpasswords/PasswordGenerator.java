package com.metroid2010.mnemonicpasswords;

import java.security.SecureRandom;
import java.util.Vector;

public class PasswordGenerator {

    private final int password_length;
    private final WordDictionary word_dictionary;

    public PasswordGenerator(WordDictionary word_dictionary, int password_length) {
        this.password_length = password_length;
        this.word_dictionary = word_dictionary;
    }

    public Password generate_password(){
        return new Password(this.build_random_word_vector(
                this.password_length,
                this.word_dictionary));
    }

    public String get_dictionary_name() {
        return this.word_dictionary.get_dictionary_name();
    }

    private int generate_random_number(int max) {
        SecureRandom rng = new SecureRandom();
        return rng.nextInt(max);
    }

    private Vector<String> build_random_word_vector(int length, WordDictionary wd) {
        Vector<String> word_vector = new Vector<>();
        for (int i=0; i<length; i++) {
            word_vector.add(wd.fetch_word(
                    this.generate_random_number(this.word_dictionary.get_length())));
        }
        return word_vector;
    }

}
