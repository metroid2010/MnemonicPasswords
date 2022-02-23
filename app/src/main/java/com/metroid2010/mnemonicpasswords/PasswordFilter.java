package com.metroid2010.mnemonicpasswords;

public class PasswordFilter {

    private final String regex_per_word;
    private final String regex_whole;

    public PasswordFilter(final String regex_per_word, final String regex_whole) {
        this.regex_per_word = regex_per_word;
        this.regex_whole = regex_whole;
    }

    public boolean is_valid(final Password password) {

        if(!this.regex_whole.isEmpty() && !password.toString().matches(this.regex_whole)) {
            return false;
        }

        if(!this.regex_per_word.isEmpty()) {
            for (String word : password.toString().split("\\s+")) {
                if(!word.matches(this.regex_per_word)) {
                    return false;
                }
            }
        }

        return true;
    }

}
