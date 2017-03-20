package com.kyobee.waitlist.customcontrol;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

public class CustomTextViewRegular extends TextView {

    public CustomTextViewRegular (Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public CustomTextViewRegular (Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CustomTextViewRegular (Context context) {
        super(context);
        init();
    }

    public void init() {
        if (!isInEditMode()) {
            Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "fonts/San-Francisco-Regular.ttf");
            setTypeface(tf, 0);
        }
    }
}
