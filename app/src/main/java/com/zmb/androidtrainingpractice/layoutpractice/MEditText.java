package com.zmb.androidtrainingpractice.layoutpractice;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.widget.EditText;

/**
 * Created by zhangmingbao on 17-7-21.
 */
public class MEditText extends EditText{
    public MEditText(Context context) {
        super(context);
    }

    private String notifytext = "please input you text:";
    public MEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public MEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        Paint p = new Paint();
        p.setTextSize(40);
        p.setColor(Color.GRAY);
        canvas.drawText(notifytext,2,getHeight()/2 + 5,p);

        super.onDraw(canvas);
    }

    @Override
    public void setSelection(int index) {
        super.setSelection(index);
    }
}
