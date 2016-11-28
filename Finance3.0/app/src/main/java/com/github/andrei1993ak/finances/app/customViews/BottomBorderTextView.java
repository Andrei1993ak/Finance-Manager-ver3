package com.github.andrei1993ak.finances.app.customViews;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.widget.TextView;

import com.github.andrei1993ak.finances.R;

public class BottomBorderTextView extends TextView {

    private final Paint paint;
    private final int color;

    public BottomBorderTextView(final Context context, final AttributeSet attrs) {
        super(context, attrs);
        this.paint = new Paint();
        final TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.BottomBorderTextView, 0, 0);
        try {
            color = ta.getColor(R.styleable.BottomBorderTextView_mBorderColor, Color.TRANSPARENT);
        } finally {
            ta.recycle();
        }
    }

    @Override
    protected void onDraw(final Canvas canvas) {
        super.onDraw(canvas);
        paint.setColor(color);
        paint.setStrokeWidth(4f);
        paint.setStyle(Paint.Style.STROKE);
        canvas.drawLine(0, getHeight(), getWidth(), getHeight(), paint);

    }
}
