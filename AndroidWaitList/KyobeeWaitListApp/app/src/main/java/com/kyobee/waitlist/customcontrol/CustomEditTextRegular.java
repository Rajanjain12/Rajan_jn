package com.kyobee.waitlist.customcontrol;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.EditText;

public class CustomEditTextRegular extends EditText {
    public CustomEditTextRegular (Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public CustomEditTextRegular (Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CustomEditTextRegular (Context context) {
        super(context);
        init();
    }

    private void init() {
        if (!isInEditMode()) {
            Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "fonts/San-Francisco-Regular.ttf");
            setTypeface(tf, 0);
        }
    }
}
