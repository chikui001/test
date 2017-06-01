package com.example.jiao.myapplication.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.LinearLayout;

/**
 * @author jiao, <wenliang.jiao@extantfuture.com>
 * @date 2017年05月26日
 */
public class DrawTest extends LinearLayout {


    public DrawTest(Context context) {
        super(context);
    }

    public DrawTest(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public DrawTest(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setStyle(Paint.Style.FILL);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_ATOP));
        canvas.drawCircle(200, 200, 100, paint);
        paint.setColor(Color.RED);
        canvas.drawRect(getLeft(), getTop(), getRight(), getBottom(), paint);

        super.dispatchDraw(canvas);
    }
}
