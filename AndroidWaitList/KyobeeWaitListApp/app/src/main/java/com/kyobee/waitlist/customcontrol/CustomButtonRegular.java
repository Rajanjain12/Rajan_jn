package com.kyobee.waitlist.customcontrol;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.Button;

public class CustomButtonRegular extends Button {
    public CustomButtonRegular (Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public CustomButtonRegular (Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CustomButtonRegular (Context context) {
        super(context);
        init();
    }

    private void init() {
        if (!isInEditMode()) {
            Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "fonts/Rubik-Regular.ttf");
            setTypeface(tf, 0);
        }
    }
}
