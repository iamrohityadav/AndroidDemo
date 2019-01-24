package com.mainli.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.FrameLayout;

import com.mainli.R;

/**
 * 用于使子视图圆角,子View必须撑满该View否则效果缺失
 */
public class RoundFrameLayout extends FrameLayout {
    private Paint mPaint;
    private PorterDuffXfermode mPorterDuffXfermode;
    private RectF mRectF;
    private int radius;
    private Path mPath;

    public RoundFrameLayout(@NonNull Context context) {
        super(context);
        init(context, null);
    }

    public RoundFrameLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public RoundFrameLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(@NonNull Context context, @Nullable AttributeSet attrs) {
        int defValue = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 8, getResources().getDisplayMetrics());
        if (attrs == null) {
            radius = defValue;
        } else {
            final TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.RoundFrameLayout);
            radius = a.getDimensionPixelSize(R.styleable.RoundFrameLayout_radius, defValue);
            a.recycle();
        }
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setStyle(Paint.Style.FILL);
        mPorterDuffXfermode = new PorterDuffXfermode(PorterDuff.Mode.DST_IN);
    }

    public void setRadius(int pixelSize) {
        radius = pixelSize;
        if (mPath != null && mRectF != null) {
            mPath.reset();
            mPath.addRoundRect(mRectF, radius, radius, Path.Direction.CW);
            invalidate();
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mRectF = new RectF(0, 0, w, h);
        mPath = new Path();
        mPath.addRoundRect(mRectF, radius, radius, Path.Direction.CW);
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        int save = canvas.saveLayer(0F, 0F, mRectF.right, mRectF.bottom, null, Canvas.ALL_SAVE_FLAG);
        super.dispatchDraw(canvas);
        mPaint.setXfermode(mPorterDuffXfermode);
        canvas.drawPath(mPath, mPaint);//必须使用path 画圆角矩形才可以正常叠加 直接使用canvas绘制圆角矩形
        mPaint.setXfermode(null);
        canvas.restoreToCount(save);
    }
}