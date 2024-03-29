package com.metroid2010.mnemonicpasswords;

import java.security.SecureRandom;
import java.util.Vector;
import java.util.concurrent.TimeoutException;

public class PasswordGenerator {

    private final int password_length;
    private final WordDictionary word_dictionary;
    private final Vector<PasswordFilter> filters;
    private final int GENERATE_MAX_TRIES = 1000;

    public PasswordGenerator(WordDictionary word_dictionary, int password_length) {
        this.password_length = password_length;
        this.word_dictionary = word_dictionary;
        this.filters = new Vector<>();
    }

    public Password generate_password() throws TimeoutException {
        Password p;
        int i = 0;
        do {
            p = new Password(this.build_random_word_vector(this.password_length, this.word_dictionary));
            if (i > GENERATE_MAX_TRIES) {
                throw new TimeoutException();
            }
            i++;
        } while (!this.pass_filters(p));
        return p;
    }

    public void add_filter(final PasswordFilter password_filter) {
        this.filters.add(password_filter);
    }

    public void remove_filter(final PasswordFilter password_filter) {
        this.filters.remove(password_filter);
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

    private boolean pass_filters(final Password password) {
        for (PasswordFilter filter: this.filters) {
            if (!filter.is_valid(password)) {
                return false;
            }
        }
        return true;
    }

}
