package com.kyobee.waitlist.customcontrol;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.AppCompatButton;
import android.util.AttributeSet;

public class CustomButtonBold extends AppCompatButton{
    public CustomButtonBold (Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public CustomButtonBold (Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CustomButtonBold (Context context) {
        super(context);
        init();
    }

    private void init() {
        if (!isInEditMode()) {
            Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "fonts/San-Francisco-Bold.ttf");
            setTypeface(tf, 0);
        }
    }
}
