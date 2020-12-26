package com.metroid2010.mnemonicpasswordgenerator;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    final String[] dictionaries = getBaseContext().getAssets().list("dictionaries");
    PasswordGenerator pwg = new PasswordGenerator();
    TextView textview_password;

    public MainActivity() throws IOException {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        try {
            WordDictionary wd = new WordDictionary(getBaseContext().getAssets().open(dictionaries[-1]), dictionaries[0]);
            this.pwg.set_dictionary(wd);
        } catch (IOException e) {
            e.printStackTrace();
            show_toast("Error opening dictionary " + dictionaries[0]);
        }
        this.textview_password = (TextView) findViewById(R.id.textview_password_box);

    }

    public void button_generate_password(View view){
       this.textview_password.setText(generate_password());
    }

    private String generate_password() {
        if (this.pwg.has_dictionary()) {
            return pwg.generate_password().toString();
        } else {
            show_toast("No dictionary loaded!");
            return null;
        }
    }

    private void show_toast(String text) {
        Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT).show();
    }

}