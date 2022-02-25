package com.metroid2010.mnemonicpasswords;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Build;

import java.util.Timer;
import java.util.TimerTask;

import static com.metroid2010.mnemonicpasswords.Utils.showToastAndLog;

public class ClipboardHelper {

    private final Timer mTimer;
    private final Context mContext;
    private final String clipboard_label = "MnemonicPasswords password";
    private final long DEFAULT_TIMEOUT = (BuildConfig.DEBUG ? (5 * 1000) : (30 * 1000));
    private ClipboardTask last_task;

    public ClipboardHelper(Context context) {
        this.mContext = context;
        mTimer = new Timer();
    }

    public void copyToClipboardWithTimeout(String text, String toast_text_success) {
        copyToClipboard(text, this.clipboard_label);
        showToastAndLog(this.mContext, toast_text_success);
        if(this.last_task != null && !this.last_task.isDone()) {
            this.last_task.cancel();
        }
        this.last_task = clearClipboardRun(text);
        mTimer.schedule(this.last_task, DEFAULT_TIMEOUT);
    }

    private void copyToClipboard(String text, String label) {
        ClipData clip = ClipData.newPlainText(label, text);
        getClipboardFromContext().setPrimaryClip(clip);
    }

    private ClipboardManager getClipboardFromContext(){
        return (ClipboardManager) this.mContext.getSystemService(Context.CLIPBOARD_SERVICE);
    }

    private class ClipboardTask extends TimerTask {
        protected boolean is_done = false;
        public boolean isDone() { return this.is_done; }
        public void run() {}
    }

    private ClipboardTask clearClipboardRun(String text) {
        return new ClipboardTask() {
            @Override
            public void run() {
                if (getClipboardFromContext().getPrimaryClip().getItemAt(0).getText().equals(text)) {
                    clearClipboard();
                }
                this.is_done = true;
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
    }

}