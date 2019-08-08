package com.mainli.view.MultiPointTouch;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewParent;

import com.mainli.R;
import com.mainli.utils.BitmapUtils;
import com.mainli.utils.SizeUtil;

import androidx.annotation.Nullable;

/**
 * 多点触控之接替滑动
 */
public class MultiPointRelayView extends View {

    private Bitmap mBitmap;
    private float mOffsetX;
    private float mOffsetY;
    private int mTouchSlop;
    private byte isfinish = 0;
    private OnViewTouchFinish mFinish;

    public MultiPointRelayView(Context context) {
        super(context);
    }

    public MultiPointRelayView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    {
        mBitmap = BitmapUtils.getTargetWidthBitmap(getResources(), R.mipmap.logo_square, SizeUtil.dp2PixelsInt(200));
        mTouchSlop = ViewConfiguration.get(getContext()).getScaledTouchSlop();
    }

    private float mOldX;
    private float mOldY;
    private int mActivePointId;
    private boolean mIsBeingDragged;

    /**
     * 触摸点有(x,y,index,id)等数据
     * <p>
     * 当多个触摸点,其中之一抬起时 index 发生改变 id不会变
     * 我们记录当前触摸的id  发生ACTION_POINTER_UP时 判断是否是当前控制手指抬起,如果不是 什么也不做,如果是则根据策略选择下一手指接替
     * <p>
     * 1. 获取当前非ACTION_MOVE时间触发手指index 使用event.getActionIndex()方法
     * 2. 通过index转化得到ID 使用event.getPointerId(index)方法
     * 3. 通过id获取该id当前index 使用event.findPointerIndex(id)
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                mActivePointId = event.getPointerId(event.getActionIndex());
                mOldX = event.getX();
                mOldY = event.getY();
                mIsBeingDragged = false;
                isfinish = 0;
                break;
            case MotionEvent.ACTION_MOVE:
                int activeIndex = event.findPointerIndex(mActivePointId);
                float x = event.getX(activeIndex);
                float y = event.getY(activeIndex);
                float offsetX = x - mOldX;
                float offsetY = y - mOldY;
                if (!mIsBeingDragged && (Math.abs(offsetX) > mTouchSlop || Math.abs(offsetY) > mTouchSlop)) {
                    final ViewParent parent = getParent();
                    if (parent != null) {
                        parent.requestDisallowInterceptTouchEvent(true);
                    }
                    mIsBeingDragged = true;
                    if (offsetX > 0) {
                        offsetX += mTouchSlop;
                    } else {
                        offsetX -= mTouchSlop;
                    }
                }
                isfinish = 0;
                if (mIsBeingDragged) {
                    mOffsetX += offsetX;
                    mOffsetY += offsetY;
                    mOldX = x;
                    mOldY = y;
                    invalidate();
                    if (mOffsetX < 0) {
                        final ViewParent parent = getParent();
                        if (parent != null) {
                            parent.requestDisallowInterceptTouchEvent(false);
                        }
                        mOffsetX = 0;
                        mFinish.isTouchFinish(-1);
                    } else if (mOffsetX > getWidth() - mBitmap.getWidth()) {
                        final ViewParent parent = getParent();
                        if (parent != null) {
                            parent.requestDisallowInterceptTouchEvent(false);
                        }
                        mOffsetX = getWidth() - mBitmap.getWidth();
                        mFinish.isTouchFinish(1);
                    }
                }
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
                activeIndex = event.getActionIndex();
                mActivePointId = event.getPointerId(activeIndex);
                mOldX = event.getX(activeIndex);
                mOldY = event.getY(activeIndex);
                break;
            case MotionEvent.ACTION_POINTER_UP:
                activeIndex = event.getActionIndex();
                int pointerUpId = event.getPointerId(activeIndex);
                if (pointerUpId == mActivePointId) {//当前正在使用PointId手指抬起
                    /*抬起手指时找寻最靠前按下手指*/
//                    if (activeIndex == 0) {
//                        activeIndex = 1;
//                    } else {
//                        activeIndex = 0;
//                    }
                    /*抬起手指时找寻最靠后按下手指*/
                    if (activeIndex == event.getPointerCount() - 1) {
                        activeIndex--;
                    } else {
                        activeIndex = event.getPointerCount() - 1;
                    }
                    mOldX = event.getX(activeIndex);
                    mOldY = event.getY(activeIndex);
                    mActivePointId = event.getPointerId(activeIndex);
                }
                break;
//            case MotionEvent.ACTION_UP:
//                if (mIsBeingDragged) {
//
//                }
//                break;
        }
        return true;
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        ViewParent parent = getParent();
        mFinish = (OnViewTouchFinish) parent.getParent();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawBitmap(mBitmap, mOffsetX, mOffsetY, null);
    }

    public interface OnViewTouchFinish {
        boolean isTouchFinish(int direction);
    }

}
