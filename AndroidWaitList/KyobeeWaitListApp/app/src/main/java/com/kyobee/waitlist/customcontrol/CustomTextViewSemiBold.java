package com.kyobee.waitlist.customcontrol;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

public class CustomTextViewSemiBold extends TextView {

    public CustomTextViewSemiBold (Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public CustomTextViewSemiBold (Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CustomTextViewSemiBold (Context context) {
        super(context);
        init();
    }

    public void init() {
        if (!isInEditMode()) {
            Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "fonts/semibold.ttf");
            setTypeface(tf, 0);
        }
    }
}
