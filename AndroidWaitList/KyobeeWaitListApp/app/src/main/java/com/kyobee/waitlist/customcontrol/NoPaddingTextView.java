package com.kyobee.waitlist.customcontrol;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Typeface;
import android.text.Layout;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

/**
 * Created on 3/27/2017.
 */

public class NoPaddingTextView extends android.support.v7.widget.AppCompatTextView {

    private int mAdditionalPadding;

    public NoPaddingTextView(Context context) {
        super(context);
        init();
    }

    public NoPaddingTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {

        if (!isInEditMode()) {
            Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "fonts/moolbor.ttf");
            setTypeface(tf, 0);
        }
        setIncludeFontPadding(false);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        final int compoundPaddingLeft = getCompoundPaddingLeft();
        final int compoundPaddingTop = getCompoundPaddingTop();
        final int compoundPaddingRight = getCompoundPaddingRight();
        final int compoundPaddingBottom = getCompoundPaddingBottom();
        final int scrollX = getScrollX();
        final int scrollY = getScrollY();
        final int right = getRight();
        final int left = getLeft();
        final int bottom = getBottom();
        final int top = getTop();

        TextPaint mTextPaint = getPaint();
        int color = getCurrentTextColor();

        Layout layout = getLayout();

        mTextPaint.setColor(color);
        mTextPaint.drawableState = getDrawableState();

        canvas.save();

        int extendedPaddingTop = getExtendedPaddingTop();
        int extendedPaddingBottom = getExtendedPaddingBottom();

        final int vspace = bottom - top - compoundPaddingBottom - compoundPaddingTop;
        final int maxScrollY = layout.getHeight() - vspace;

        float clipLeft = compoundPaddingLeft + scrollX;
        float clipTop = (scrollY == 0) ? 0 : extendedPaddingTop + scrollY;
        float clipRight = right - left - getFudgedPaddingRight() + scrollX;
        float clipBottom = bottom - top + scrollY -
                ((scrollY == maxScrollY) ? 0 : extendedPaddingBottom);

        canvas.clipRect(clipLeft, clipTop, clipRight, clipBottom);

        int voffsetText = 0;
        if ((getGravity() & Gravity.VERTICAL_GRAVITY_MASK) != Gravity.TOP) {
            voffsetText = getVerticalOffset();
        }
        canvas.translate(compoundPaddingLeft, extendedPaddingTop + voffsetText);

        int yOff = -mAdditionalPadding / 4;
        canvas.translate(0, yOff);

        layout.draw(canvas);
        canvas.restore();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int mode = View.MeasureSpec.getMode(heightMeasureSpec);
        if (mode != View.MeasureSpec.EXACTLY) {
            int measureHeight = measureHeight(getText().toString(), widthMeasureSpec);
            int additionalPadding = getAdditionalPadding();

            int height = measureHeight - additionalPadding;
            height += getPaddingTop() + getPaddingBottom();
            heightMeasureSpec = View.MeasureSpec.makeMeasureSpec(height, View.MeasureSpec.EXACTLY);
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    private int measureHeight(String text, int widthMeasureSpec) {
        float textSize = getTextSize();

        TextView textView = new TextView(getContext());
        textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
        textView.setText(text);
        textView.measure(widthMeasureSpec, 0);
        return textView.getMeasuredHeight();
    }

    private int getAdditionalPadding() {
        float textSize = getTextSize();

        TextView textView = new TextView(getContext());
        textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
        textView.setLines(1);
        textView.measure(0, 0);
        int measuredHeight = textView.getMeasuredHeight();
        if (measuredHeight - textSize > 0) {
            mAdditionalPadding = (int) (measuredHeight - textSize);
            Log.v("NoPaddingTextView", "onMeasure: height=" + measuredHeight + " textSize=" + textSize + " mAdditionalPadding=" + mAdditionalPadding);
        }
        return mAdditionalPadding;
    }

    private int getFudgedPaddingRight() {
        int cursorWidth = 2 + (int) getPaint().density;
        return Math.max(0, getCompoundPaddingRight() - (cursorWidth - 1));
    }

    private int getBoxHeight() {
        return getMeasuredHeight() - getExtendedPaddingTop() - getExtendedPaddingBottom();
    }

    int getVerticalOffset() {
        int voffset = 0;
        final int gravity = getGravity() & Gravity.VERTICAL_GRAVITY_MASK;

        Layout l = getLayout();
        if (gravity != Gravity.TOP) {
            int boxht = getBoxHeight();
            int textht = l.getHeight();

            if (textht < boxht) {
                if (gravity == Gravity.BOTTOM)
                    voffset = boxht - textht;
                else // (gravity == Gravity.CENTER_VERTICAL)
                    voffset = (boxht - textht) >> 1;
            }
        }
        return voffset;
    }
}
