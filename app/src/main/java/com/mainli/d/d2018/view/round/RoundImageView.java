package com.mainli.d.d2018.view.round;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by lixiaoliang on 2018-5-31.
 */
public class RoundImageView extends AppCompatImageView {

    private Path mPath;
    private Paint mPaint;
    private PorterDuffXfermode mPorterDuffXfermode;

    public RoundImageView(Context context) {
        super(context);
        init();
    }

    public RoundImageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public RoundImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setStyle(Paint.Style.FILL);
        mPorterDuffXfermode = new PorterDuffXfermode(PorterDuff.Mode.DST_OUT);
        mPath = new Path();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, widthMeasureSpec);
        size = MeasureSpec.getSize(widthMeasureSpec);
        setMeasuredDimension(size, size);
    }

    private int size = 0;

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        int r = w >> 1;
        mPath.moveTo(0,0);
        mPath.lineTo(0,w);
        mPath.lineTo(w,w);
        mPath.lineTo(w,0);
        mPath.lineTo(0,0);
        mPath.addCircle(r, r, r, Path.Direction.CW);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        int save = canvas.saveLayer(0F, 0F, size, size, null);
        super.onDraw(canvas);
        mPaint.setXfermode(mPorterDuffXfermode);
        canvas.drawPath(mPath, mPaint);
        mPaint.setXfermode(null);
        canvas.restoreToCount(save);
    }
}
