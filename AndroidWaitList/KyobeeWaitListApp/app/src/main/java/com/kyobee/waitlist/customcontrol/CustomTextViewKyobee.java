package com.kyobee.waitlist.customcontrol;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

public class CustomTextViewKyobee extends TextView {

    public CustomTextViewKyobee (Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public CustomTextViewKyobee (Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CustomTextViewKyobee (Context context) {
        super(context);
        init();
    }

    public void init() {
        if (!isInEditMode()) {
            Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "fonts/moolbor.ttf");
            setTypeface(tf, 0);
        }
    }
}
