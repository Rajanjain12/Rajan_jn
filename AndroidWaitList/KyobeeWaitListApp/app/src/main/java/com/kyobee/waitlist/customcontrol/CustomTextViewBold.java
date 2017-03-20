package com.kyobee.waitlist.customcontrol;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

public class CustomTextViewBold extends TextView {

    public CustomTextViewBold (Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public CustomTextViewBold (Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CustomTextViewBold (Context context) {
        super(context);
        init();
    }

    public void init() {
        if (!isInEditMode()) {
            Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "fonts/San-Francisco-Bold.ttf");
            setTypeface(tf, 0);
        }
    }
}
