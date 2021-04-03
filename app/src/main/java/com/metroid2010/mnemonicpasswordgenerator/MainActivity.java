package com.metroid2010.mnemonicpasswordgenerator;

import androidx.appcompat.app.AppCompatActivity;

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

    public MainActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        try {
            this.dictionaries = getBaseContext().getAssets().list("dictionaries");
        } catch (IOException e) {
            e.printStackTrace();
            show_toast("Error reading dictionaries directory");
        }
        try {
            WordDictionary word_dictionary = new WordDictionary(getBaseContext().getAssets().open(dictionaries_assets_path + "/" + dictionaries[0]), dictionaries[0]);
            this.pwg = new PasswordGenerator(word_dictionary, PASSWORD_LENGTH);
        } catch (IOException e) {
            e.printStackTrace();
            show_toast("Error opening dictionary " + dictionaries_assets_path + "/" + dictionaries[0]);
        }
        this.textview_password = (TextView) findViewById(R.id.textview_password_box);
    }

    public void oc_button_generate_password(View view) {
        try {
            this.textview_password.setText(this.pwg.generate_password().toString());
        } catch (NullPointerException e) {
            show_toast("Cannot generate password: dictionary not initialized");
            this.textview_password.setText("Error generating password");
        }
    }

    private void show_toast(String text) {
        Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT).show();
    }

}