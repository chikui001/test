/*
 * Copyright (C) 2015 G-Wearable Inc.
 * All rights reserved.
 */

package com.example.jiao.myapplication.views;

import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;

/**
 * Created by peter on 1/28/15.
 */

public class LineProgressDrawable extends Drawable {
    /**
     * Logger Tag for Logging purposes.
     */
    public static final String TAG = "LineProgressDrawable";
    /**
     * Paint object to draw the element.
     */
    private final Paint paint;

    private int barColor;

    private int barBgColor;

    private int barHeight;

    private int barBgHeight;
    /**
     * Ring progress.
     */
    protected float progress;

    public LineProgressDrawable(int barHeight, int barBgHeight, int barColor, int barBgColor, float progress) {
        this.progress = progress;
        this.barHeight = barHeight;
        this.barBgHeight = barBgHeight;
        this.barColor = barColor;
        this.barBgColor = barBgColor;
        this.paint = new Paint();
        this.paint.setAntiAlias(true);
    }

    @Override
    public void draw(Canvas canvas) {
        final Rect bounds = getBounds();
        int padding = 38;
        paint.setColor(barBgColor);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(barBgHeight);
        paint.setStrokeCap(Paint.Cap.ROUND);
        canvas.drawLine(padding, bounds.height() / 2, bounds.width() - padding, bounds.height() / 2, paint);

        padding += 2;
        paint.setColor(barColor);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(barHeight);
        paint.setStrokeCap(Paint.Cap.ROUND);
        float length = Math.min(bounds.width() * progress, bounds.width() - padding);
        if (length != 0) {
            canvas.drawLine(padding, bounds.height() / 2, length, bounds.height() / 2, paint);
        }

    }

    @Override
    public void setAlpha(int alpha) {
        paint.setAlpha(alpha);
    }

    @Override
    public void setColorFilter(ColorFilter cf) {
        paint.setColorFilter(cf);
    }

    @Override
    public int getOpacity() {
        return 1 - paint.getAlpha();
    }

    /**
     * Sets the progress [0..1f]
     *
     * @param progress Sets the progress
     */
    public void setProgress(float progress) {
        this.progress = progress;
        invalidateSelf();
    }
}