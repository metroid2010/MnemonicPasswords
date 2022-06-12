package com.metroid2010.mnemonicpasswords;

import android.app.Application;

import com.google.android.material.color.DynamicColors;

public class MnemonicPasswordsApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        DynamicColors.applyToActivitiesIfAvailable(this);
    }
}
