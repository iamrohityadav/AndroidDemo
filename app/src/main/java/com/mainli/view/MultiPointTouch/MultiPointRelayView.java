package com.mainli.view.MultiPointTouch;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.mainli.R;
import com.mainli.utils.BitmapUtils;
import com.mainli.utils.SizeUtil;

/**
 * 多点触控之接替滑动
 */
public class MultiPointRelayView extends View {

    private Bitmap mBitmap;
    private float mOffsetX;
    private float mOffsetY;

    public MultiPointRelayView(Context context) {
        super(context);
    }

    public MultiPointRelayView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    {
        mBitmap = BitmapUtils.getTargetWidthBitmap(getResources(), R.mipmap.logo_square, SizeUtil.dp2PixelsInt(200));
    }

    private float mOldX;
    private float mOldY;
    private int mActiveIndex;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                mActiveIndex = event.getActionIndex();
                mOldX = event.getX(mActiveIndex);
                mOldY = event.getY(mActiveIndex);
                break;
            case MotionEvent.ACTION_MOVE:
                float x = event.getX(mActiveIndex);
                float y = event.getY(mActiveIndex);
                mOffsetX += x - mOldX;
                mOffsetY += y - mOldY;
                mOldX = x;
                mOldY = y;
                invalidate();
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
                mActiveIndex = event.getActionIndex();
                mOldX = event.getX(mActiveIndex);
                mOldY = event.getY(mActiveIndex);
                break;
            case MotionEvent.ACTION_POINTER_UP:
                mActiveIndex = event.getActionIndex();
                /**
                 * 抬起手指时数组
                 */
                if (mActiveIndex == 0) {
                    mOldX = event.getX(1);
                    mOldY = event.getY(1);
                } else {
                    mActiveIndex = 0;//直接取第一个按下手指控制逻辑 模仿ListView多指策略接替
//                    mActiveIndex--;//取前一个按下手指
                    mOldX = event.getX(mActiveIndex);
                    mOldY = event.getY(mActiveIndex);
                }
                break;
        }
        return true;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawBitmap(mBitmap, mOffsetX, mOffsetY, null);
    }
}
