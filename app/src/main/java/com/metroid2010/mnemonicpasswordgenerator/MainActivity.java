package com.metroid2010.mnemonicpasswordgenerator;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    private final int PASSWORD_LENGTH = 4;
    private final String dictionaries_assets_path = "dictionaries";
    private String[] dictionaries;
    private PasswordGenerator pwg;
    private TextView textview_password;
    private Password password;
    private ClipboardManager clipboard;

    public MainActivity() {
        this.clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        try {
            this.dictionaries = getBaseContext().getAssets().list(dictionaries_assets_path);
        } catch (IOException e) {
            e.printStackTrace();
            showToast(getString(R.string.toast_error_reading_dictionaries_dictionary));
        }
        try {
            WordDictionary word_dictionary = new WordDictionary(getBaseContext().getAssets().open(dictionaries_assets_path + "/" + dictionaries[0]), dictionaries[0]);
            this.pwg = new PasswordGenerator(word_dictionary, PASSWORD_LENGTH);
        } catch (IOException e) {
            e.printStackTrace();
            showToast(getString(R.string.toast_error_opening_dictionary) + dictionaries_assets_path + "/" + dictionaries[0]);
        }
        this.textview_password = (TextView) findViewById(R.id.textview_password_box);
    }

    public void ocButtonGeneratePassword(View view) {
        this.textview_password.setText(generatePassword());
    }

    private String generatePassword() {
        try {
            this.password = this.pwg.generate_password();
            return this.password.toString();
        } catch (NullPointerException e) {
            showToast(getString(R.string.toast_error_generate_password_dict_not_initialized));
            return getString(R.string.textview_error_generating_password);
        }
    }

    public void ocButtonCopyToClipboard(View view) {
        try {
            ClipData clip = ClipData.newPlainText("MnemonicPasswordGenerator password", this.password.toString());
            this.clipboard.setPrimaryClip(clip);
        } catch (NullPointerException e) {
            showToast("No password to copy to clipboard");
        }
    }

    private void showToast(String text) {
        Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT).show();
    }
}