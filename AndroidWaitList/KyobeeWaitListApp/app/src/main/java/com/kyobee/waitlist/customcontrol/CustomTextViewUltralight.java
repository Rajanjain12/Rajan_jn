package com.kyobee.waitlist.customcontrol;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

public class CustomTextViewUltralight extends TextView {

    public CustomTextViewUltralight (Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public CustomTextViewUltralight (Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CustomTextViewUltralight (Context context) {
        super(context);
        init();
    }

    public void init() {
        if (!isInEditMode()) {
            Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "fonts/sanfrasci-ultra-light.ttf");
            setTypeface(tf, 0);
        }
    }
}
