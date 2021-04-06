package com.metroid2010.mnemonicpasswordgenerator;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Build;
import android.util.Log;

import java.util.Timer;
import java.util.TimerTask;

import static com.metroid2010.mnemonicpasswordgenerator.Utils.showToastAndLog;

public class ClipboardHelper {

    private Timer mTimer;
    private Context mContext;
    private final String clipboard_label = "MnemonicPasswordGenerator password";
    private final long DEFAULT_TIMEOUT = (BuildConfig.DEBUG ? (5 * 1000) : (30 * 1000));


    public ClipboardHelper(Context context) {
        this.mContext = context;
        mTimer = new Timer();
    }

    public void copyToClipboardWithTimeout(String text, String toast_text) {
        copyToClipboard(text, this.clipboard_label);
        showToastAndLog(this.mContext, this.mContext.getString(R.string.toast_clipboard_copy_success));
        mTimer.schedule(clearClipboardRun(text), DEFAULT_TIMEOUT);
    }

    private void copyToClipboard(String text, String label) {
        ClipData clip = ClipData.newPlainText(label, text);
        getClipboardFromContext().setPrimaryClip(clip);
    }

    private ClipboardManager getClipboardFromContext(){
        return (ClipboardManager) this.mContext.getSystemService(Context.CLIPBOARD_SERVICE);
    }

    private TimerTask clearClipboardRun(String text) {
        return new TimerTask() {
            @Override
            public void run() {
                if (getClipboardFromContext().getPrimaryClip().getItemAt(0).getText().equals(text)) {
                    clearClipboard();
                }
            }
        };
    }

    private void clearClipboard() {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.P) {
            getClipboardFromContext().clearPrimaryClip();
        } else {
            getClipboardFromContext().setPrimaryClip(ClipData.newPlainText(clipboard_label, ""));
        }
        showToastAndLog(this.mContext.getApplicationContext(), "Clipboard cleared");
    };

}