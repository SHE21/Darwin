package com.darwin.aigus.darwin.AndroidUltils;

import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

/**
 * Created by SANTIAGO from AIGUS on 13/03/2018.
 */

public class OnEditTextChange implements TextWatcher {
    private ImageButton button;
    private Drawable drawTrue, drawFalse;

    public OnEditTextChange(ImageButton button, Drawable drawTrue, Drawable drawFalse) {
        this.button = button;
        this.drawTrue = drawTrue;
        this.drawFalse = drawFalse;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        if (start != 0) {
            button.setEnabled(true);
            button.setImageDrawable(drawTrue);
        }else{
            button.setEnabled(false);
            button.setImageDrawable(drawFalse);
        }
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {

    }
}
