package com.metroid2010.mnemonicpasswordgenerator;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    private final int PASSWORD_LENGTH = 4;
    private final String[] dictionaries = getBaseContext().getAssets().list("dictionaries");
    private PasswordGenerator pwg;
    private TextView textview_password;

    public MainActivity() throws IOException {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.textview_password = (TextView) findViewById(R.id.textview_password_box);
        this.pwg = load_dictionary_by_name(dictionaries[0]);
    }

    public void oc_button_generate_password(View view) {
        this.textview_password.setText(this.pwg.generate_password().toString());
    }

    private PasswordGenerator load_dictionary_by_name(String dictionary_name) {
        try {
            WordDictionary word_dictionary = new WordDictionary(getBaseContext().getAssets().open(dictionary_name), dictionary_name);
            PasswordGenerator pwg = new PasswordGenerator(word_dictionary, PASSWORD_LENGTH);
        } catch (IOException e) {
            e.printStackTrace();
            show_toast("Error opening dictionary " + dictionaries[0]);
        }

        return pwg;
    }

    private void show_toast(String text) {
        Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT).show();
    }

}