package org.sucareto.androidhidkeyboard;

import android.util.Log;

import com.topjohnwu.superuser.io.SuFile;
import com.topjohnwu.superuser.io.SuFileInputStream;
import com.topjohnwu.superuser.io.SuFileOutputStream;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;

public class HidController {
    private static final byte[] EMPTY_KEYBOARD_REPORT = new byte[]{0, 0, 0, 0, 0, 0, 0, 0};
    private static final byte[] EMPTY_MOUSE_REPORT = new byte[]{0, 0, 0, 0};

    public byte[] kCode = Arrays.copyOf(EMPTY_KEYBOARD_REPORT, EMPTY_KEYBOARD_REPORT.length);
    public byte[] mCode = Arrays.copyOf(EMPTY_MOUSE_REPORT, EMPTY_MOUSE_REPORT.length);
    private OutputStream kDev = null;
    private OutputStream mDev = null;
    private InputStream kDevIn = null;
    private InputStream mDevIn = null;

    public void UnInit() {
        releaseAll();
        closeQuietly(kDev, "kDev");
        closeQuietly(mDev, "mDev");
        closeQuietly(kDevIn, "kDevIn");
        closeQuietly(mDevIn, "mDevIn");
        kDev = null;
        mDev = null;
        kDevIn = null;
        mDevIn = null;
    }

    public boolean kInit() {
        kCode = Arrays.copyOf(EMPTY_KEYBOARD_REPORT, EMPTY_KEYBOARD_REPORT.length);
        SuFile keyboard = SuFile.open("/dev/hidg0");
        if (!keyboard.exists()) {
            return true;
        }
        try {
            kDev = SuFileOutputStream.open(keyboard);
            kDevIn = SuFileInputStream.open(keyboard);
            kDev.write(kCode);
            return false;
        } catch (Exception e) {
            Log.e("kInit", String.valueOf(e));
            return true;
        }
    }

    public boolean mInit() {
        mCode = Arrays.copyOf(EMPTY_MOUSE_REPORT, EMPTY_MOUSE_REPORT.length);
        SuFile mouse = SuFile.open("/dev/hidg1");
        if (!mouse.exists()) {
            return true;
        }
        try {
            mDev = SuFileOutputStream.open(mouse);
            mDevIn = SuFileInputStream.open(mouse);
            mDev.write(mCode);
            return false;
        } catch (Exception e) {
            Log.e("mInit", String.valueOf(e));
            return true;
        }
    }

    public void kSend() {
        if (kDev == null) return;
        try {
            kDev.write(kCode);
        } catch (Exception e) {
            Log.e("kSend", String.valueOf(e));
        }
    }

    public void kPress(byte code) {
        if (code == 0 || containsKey(code)) return;
        for (byte i = 2; i < 8; i++) {
            if (kCode[i] == 0) {
                kCode[i] = code;
                break;
            }
        }
        kSend();
    }

    public void kRelease(byte code) {
        for (byte i = 2; i < 8; i++) {
            if (kCode[i] == code) {
                kCode[i] = 0;
                break;
            }
        }
        kSend();
    }

    public void kPress_c(byte code) {
        kCode[0] |= code;
        kSend();

    }

    public void kRelease_c(byte code) {
        kCode[0] &= (byte) ~code;
        kSend();
    }

    public void mSend() {
        if (mDev == null) return;
        try {
            mDev.write(mCode);
        } catch (Exception e) {
            Log.e("mSend", String.valueOf(e));
        }
        mCode[1] = 0;
        mCode[2] = 0;
        mCode[3] = 0;
    }

    public void mPress(byte code) {
        mCode[0] |= code;
        mSend();

    }

    public void mRelease(byte code) {
        mCode[0] &= (byte) ~code;
        mSend();
    }

    public void releaseAll() {
        kCode = Arrays.copyOf(EMPTY_KEYBOARD_REPORT, EMPTY_KEYBOARD_REPORT.length);
        mCode = Arrays.copyOf(EMPTY_MOUSE_REPORT, EMPTY_MOUSE_REPORT.length);
        kSend();
        mSend();
    }

    public int readKeyboard(byte[] buffer) throws IOException {
        return kDevIn == null ? -1 : kDevIn.read(buffer);
    }

    public int readMouse(byte[] buffer) throws IOException {
        return mDevIn == null ? -1 : mDevIn.read(buffer);
    }

    private boolean containsKey(byte code) {
        for (byte i = 2; i < 8; i++) {
            if (kCode[i] == code) {
                return true;
            }
        }
        return false;
    }

    private void closeQuietly(AutoCloseable closeable, String name) {
        if (closeable == null) return;
        try {
            closeable.close();
        } catch (Exception e) {
            Log.e("UnInit", "Failed to close " + name, e);
        }
    }
}
