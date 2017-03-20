package com.kyobee.waitlist.customcontrol;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

public class CustomTextViewThin extends TextView {

    public CustomTextViewThin (Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public CustomTextViewThin (Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CustomTextViewThin (Context context) {
        super(context);
        init();
    }

    public void init() {
        if (!isInEditMode()) {
            Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "fonts/San-Francisco-Thin.ttf");
            setTypeface(tf, 0);
        }
    }
}
