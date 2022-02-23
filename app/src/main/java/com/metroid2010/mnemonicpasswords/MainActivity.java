package com.metroid2010.mnemonicpasswords;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;

import static com.metroid2010.mnemonicpasswords.Utils.showToastAndLog;

public class MainActivity extends AppCompatActivity {

    private final int DEFAULT_PASSWORD_LENGTH = 4;
    private final String dictionaries_assets_path = "dictionaries";
    private String[] dictionaries;
    private PasswordGenerator pwg;
    private TextView textview_password;
    private Password password;
    private ClipboardHelper mClipboardHelper;

    // Default filters
    private final PasswordFilter filter_proper_name = new PasswordFilter("([a-z'])+", "");
    private final PasswordFilter filter_apostrophe = new PasswordFilter("([a-zA-Z])+", "");

    public MainActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.dictionaries = readDictionariesInAssets(this.dictionaries_assets_path);
        this.pwg = new PasswordGenerator(getDictionaryFromAsset(this.dictionaries[0]), this.DEFAULT_PASSWORD_LENGTH);
        this.pwg.add_filter(filter_apostrophe);
        this.pwg.add_filter(filter_proper_name);
        this.textview_password = findViewById(R.id.textview_password_box);
        this.mClipboardHelper = new ClipboardHelper(getApplicationContext());
    }

    public void ocButtonGeneratePassword(View view) {
        this.textview_password.setText(generatePassword());
    }

    public void ocButtonCopyToClipboard(View view) {
        if (this.password != null) {
            mClipboardHelper.copyToClipboardWithTimeout(this.password.toString(), getString(R.string.toast_clipboard_copy_success), getString(R.string.toast_error_copy_to_clipboard_empty_password));
        } else {
            showToastAndLog(getApplicationContext(), getString(R.string.toast_no_password));
        }
    }

    private String generatePassword() {
        try {
            this.password = this.pwg.generate_password();
            return this.password.toString();
        } catch (NullPointerException e) {
            showToastAndLog(getApplicationContext(), getString(R.string.toast_error_generate_password_dict_not_initialized));
            return getString(R.string.textview_error_generating_password);
        }
    }

    private String[] readDictionariesInAssets(final String dictionaries_assets_path) {
        String[] dictionary_list = null;
        try {
            dictionary_list = getBaseContext().getAssets().list(dictionaries_assets_path);
        } catch (IOException e) {
            e.printStackTrace();
            showToastAndLog(getApplicationContext(), getString(R.string.toast_error_reading_dictionaries_dictionary));
        }
        return dictionary_list;
    }

    private WordDictionary getDictionaryFromAsset(final String dictionary) {
        final String dictionary_full_path = dictionaries_assets_path + "/" + dictionary;
        WordDictionary word_dictionary = null;
        try {
            word_dictionary = new WordDictionary(getBaseContext().getAssets().open(dictionary_full_path), dictionary);
        } catch (IOException e) {
            e.printStackTrace();
            showToastAndLog(getApplicationContext(), getString(R.string.toast_error_opening_dictionary) + dictionary_full_path);
        }
        return word_dictionary;
    }

}