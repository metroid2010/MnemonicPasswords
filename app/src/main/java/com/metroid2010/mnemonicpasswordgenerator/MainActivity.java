package com.metroid2010.mnemonicpasswordgenerator;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;

import static com.metroid2010.mnemonicpasswordgenerator.Utils.showToastAndLog;

public class MainActivity extends AppCompatActivity {

    private final int PASSWORD_LENGTH = 4;
    private final String dictionaries_assets_path = "dictionaries";
    private String[] dictionaries;
    private PasswordGenerator pwg;
    private TextView textview_password;
    private Password password;
    private ClipboardManager clipboard;
    private ClipboardHelper mClipboardHelper;

    public MainActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        try {
            this.dictionaries = getBaseContext().getAssets().list(dictionaries_assets_path);
        } catch (IOException e) {
            e.printStackTrace();
            showToastAndLog(getApplicationContext(), getString(R.string.toast_error_reading_dictionaries_dictionary));
        }
        try {
            WordDictionary word_dictionary = new WordDictionary(getBaseContext().getAssets().open(dictionaries_assets_path + "/" + dictionaries[0]), dictionaries[0]);
            this.pwg = new PasswordGenerator(word_dictionary, PASSWORD_LENGTH);
        } catch (IOException e) {
            e.printStackTrace();
            showToastAndLog(getApplicationContext(), getString(R.string.toast_error_opening_dictionary) + dictionaries_assets_path + "/" + dictionaries[0]);
        }
        this.textview_password = (TextView) findViewById(R.id.textview_password_box);
        this.mClipboardHelper = new ClipboardHelper(getApplicationContext());
    }

    public void ocButtonGeneratePassword(View view) {
        this.textview_password.setText(generatePassword());
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

    public void ocButtonCopyToClipboard(View view) {
        if (this.password != null) {
            mClipboardHelper.copyToClipboardWithTimeout(this.password.toString(), getString(R.string.toast_clipboard_copy_success));
        } else {
            showToastAndLog(getApplicationContext(), "No password to copy");
        }
    }
}