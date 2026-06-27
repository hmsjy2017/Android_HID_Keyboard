package org.sucareto.androidhidkeyboard;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.HapticFeedbackConstants;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.sucareto.androidhidkeyboard.databinding.ActivityKeyboardBinding;


public class Keyboard extends AppCompatActivity {
    HidController hid = new HidController();
    private boolean FnEnable = false;
    private final int[] fnKeyIds = {
            R.id.Btn12, R.id.Btn13, R.id.Btn65, R.id.Btn69, R.id.Btn70, R.id.Btn71
    };
    private final int[] normalFnTexts = {
            R.string.KeyText12, R.string.KeyText13, R.string.KeyText84,
            R.string.KeyText85, R.string.KeyText86, R.string.KeyText87
    };
    private final int[] normalFnCodes = {
            R.string.KeyCode12, R.string.KeyCode13, R.string.KeyCode84,
            R.string.KeyCode85, R.string.KeyCode86, R.string.KeyCode87
    };
    private final int[] enabledFnTexts = {
            R.string.KeyText78, R.string.KeyText81, R.string.KeyText80,
            R.string.KeyText79, R.string.KeyText83, R.string.KeyText82
    };
    private final int[] enabledFnCodes = {
            R.string.KeyCode78, R.string.KeyCode81, R.string.KeyCode80,
            R.string.KeyCode79, R.string.KeyCode83, R.string.KeyCode82
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View root_view = ActivityKeyboardBinding.inflate(getLayoutInflater()).getRoot();
        setContentView(root_view);

        findViewById(R.id.BtnSpace).setOnTouchListener(new SpaceOnTouch());
        findViewById(R.id.BtnEnter).setOnTouchListener(new EnterOnTouch());
        findViewById(R.id.BtnFn).setOnTouchListener(new FnKeyOnTouch());

        String packageName = getPackageName();

        KeyOnTouch keyOnTouch = new KeyOnTouch();
        for (int i = 1; i < 72; i++) {
            int resId = getResources().getIdentifier("Btn" + i, "id", packageName);
            if (resId != 0) {
                findViewById(resId).setOnTouchListener(keyOnTouch);
            }
        }
        CtrlKeyOnTouch ctrlKeyOnTouch = new CtrlKeyOnTouch();
        for (int i = 1; i < 6; i++) {
            int resId = getResources().getIdentifier("CtrlBtn" + i, "id", packageName);
            if (resId != 0) {
                findViewById(resId).setOnTouchListener(ctrlKeyOnTouch);
            }
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        hid.UnInit();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (hid.kInit()) {
            Toast.makeText(this, R.string.msg_e_hid, Toast.LENGTH_LONG).show();
        }
    }

    private class SpaceOnTouch implements View.OnTouchListener {
        @SuppressLint("ClickableViewAccessibility")
        @Override
        public boolean onTouch(View v, MotionEvent e) {
            switch (e.getAction()) {
                case MotionEvent.ACTION_DOWN -> {
                    v.performHapticFeedback(HapticFeedbackConstants.KEYBOARD_PRESS);
                    if (FnEnable) {
                        startActivity(new Intent(Keyboard.this, Mouse.class));
                        break;
                    }
                    hid.kPress((byte) Integer.parseInt(v.getTag().toString(), 16));
                }
                case MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                    hid.kRelease((byte) Integer.parseInt(v.getTag().toString(), 16));
                }
            }
            return true;
        }
    }

    private class EnterOnTouch implements View.OnTouchListener {
        @SuppressLint("ClickableViewAccessibility")
        @Override
        public boolean onTouch(View v, MotionEvent e) {
            switch (e.getAction()) {
                case MotionEvent.ACTION_DOWN -> {
                    v.performHapticFeedback(HapticFeedbackConstants.KEYBOARD_PRESS);
                    if (FnEnable) {
                        hid.kCode = new byte[]{0x05, 0, 0x4c, 0, 0, 0, 0, 0};
                        hid.kSend();
                    } else {
                        hid.kPress((byte) Integer.parseInt(v.getTag().toString(), 16));
                    }
                }
                case MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                    if (FnEnable) {
                        hid.kCode = new byte[]{0, 0, 0, 0, 0, 0, 0, 0};
                        hid.kSend();
                    } else {
                        hid.kRelease((byte) Integer.parseInt(v.getTag().toString(), 16));
                    }
                }
            }
            return true;
        }
    }

    private class CtrlKeyOnTouch implements View.OnTouchListener {
        @SuppressLint("ClickableViewAccessibility")
        @Override
        public boolean onTouch(View v, MotionEvent e) {
            switch (e.getAction()) {
                case MotionEvent.ACTION_DOWN -> {
                    v.performHapticFeedback(HapticFeedbackConstants.KEYBOARD_PRESS);
                    hid.kPress_c((byte) Integer.parseInt(v.getTag().toString(), 16));
                }
                case MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                    hid.kRelease_c((byte) Integer.parseInt(v.getTag().toString(), 16));
                }
            }
            return true;
        }
    }

    private class KeyOnTouch implements View.OnTouchListener {
        @SuppressLint("ClickableViewAccessibility")
        @Override
        public boolean onTouch(View v, MotionEvent e) {
            switch (e.getAction()) {
                case MotionEvent.ACTION_DOWN -> {
                    v.performHapticFeedback(HapticFeedbackConstants.KEYBOARD_PRESS);
                    hid.kPress((byte) Integer.parseInt(v.getTag().toString(), 16));
                }
                case MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                    hid.kRelease((byte) Integer.parseInt(v.getTag().toString(), 16));
                }
            }
            return true;
        }
    }

    private class FnKeyOnTouch implements View.OnTouchListener {
        @SuppressLint("ClickableViewAccessibility")
        @Override
        public boolean onTouch(View v, MotionEvent e) {
            switch (e.getAction()) {
                case MotionEvent.ACTION_DOWN -> {
                    v.performHapticFeedback(HapticFeedbackConstants.KEYBOARD_PRESS);
                    setFnEnabled(true);
                }
                case MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> setFnEnabled(false);
                default -> {
                    return true;
                }
            }
            return true;
        }
    }

    private void setFnEnabled(boolean enabled) {
        if (FnEnable == enabled) return;
        FnEnable = enabled;
        for (int i = 0; i < fnKeyIds.length; i++) {
            Button button = findViewById(fnKeyIds[i]);
            button.setText(getResources().getString(FnEnable ? enabledFnTexts[i] : normalFnTexts[i]));
            button.setTag(getResources().getString(FnEnable ? enabledFnCodes[i] : normalFnCodes[i]));
        }
        ((Button) findViewById(R.id.BtnSpace)).setText(getResources().getString(FnEnable ? R.string.FnSpace : R.string.KeyText70));
        ((Button) findViewById(R.id.BtnEnter)).setText(getResources().getString(FnEnable ? R.string.FnEnter : R.string.KeyText54));
    }
}
