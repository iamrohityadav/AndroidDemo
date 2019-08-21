package com.mainli.view.MultiPointTouch;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewParent;

import com.mainli.R;
import com.mainli.utils.BitmapUtils;
import com.mainli.utils.SizeUtil;

import androidx.annotation.Nullable;
import androidx.core.view.NestedScrollingChild;
import androidx.core.view.NestedScrollingParent;
import androidx.core.view.ViewCompat;

/**
 * 多点触控之接替滑动
 */
public class MultiPointRelayView extends View implements NestedScrollingChild {


    private Bitmap mBitmap;
    private float mOffsetX;
    private float mOffsetY;
    private int mTouchSlop;
    //父View提前消费
    int[] mScrollConsumed = new int[2];
    int[] mTempNestedScrollConsumed = new int[2];

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
                startNestedScroll(ViewCompat.SCROLL_AXIS_HORIZONTAL | ViewCompat.SCROLL_AXIS_VERTICAL);
                break;
            case MotionEvent.ACTION_MOVE:
                int activeIndex = event.findPointerIndex(mActivePointId);
                float x = event.getX(activeIndex);
                float y = event.getY(activeIndex);
                float offsetX = x - mOldX;
                float offsetY = y - mOldY;
                //消耗触摸前分发
                if (dispatchNestedPreScroll((int) offsetX, (int) offsetX, mScrollConsumed, null)) {
                    offsetX -= mScrollConsumed[0];
                    offsetX -= mScrollConsumed[1];
//                    event.offsetLocation(mScrollOffset[0], mScrollOffset[1]);
                }

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
                if (mIsBeingDragged) {
                    //消耗触摸后分发
                    float tmpX = mOffsetX + offsetX;
                    mOldX = x;
                    mOldY = y;

                    float unconsumedX = 0, unconsumedY = 0;
                    float consumedX = 0, consumedY = 0;

                    invalidate();
                    if (tmpX < 0) {
                        consumedX = -mOffsetX;
                        unconsumedX = offsetX + mOffsetX;
                    } else if (tmpX > getWidth() - mBitmap.getWidth()) {
                        unconsumedX = (int) (tmpX - (getWidth() - mBitmap.getWidth()));
                        consumedX = (int) (offsetX - unconsumedX);
                    } else {
                        consumedX = (int) offsetX;
                        unconsumedX = 0;
                    }
                    consumedY = (int) offsetY;
                    unconsumedY = 0;
                    dispatchNestedScroll((int) consumedX, (int) consumedY, (int) unconsumedX, (int) unconsumedY, null);
                    mOffsetX += consumedX;
                    mOffsetY += consumedY;
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
            case MotionEvent.ACTION_UP:
                stopNestedScroll();
                break;
        }
        return true;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawBitmap(mBitmap, mOffsetX, mOffsetY, null);
    }

    //---------------------嵌套滚动-----------------------------------------------
    private boolean isNestedScrollingEnabled = true;

    @Override
    public void setNestedScrollingEnabled(boolean enabled) {
        isNestedScrollingEnabled = enabled;
    }

    @Override
    public boolean isNestedScrollingEnabled() {
        return isNestedScrollingEnabled;
    }

    private NestedScrollingParent mNestedScrollingParent;

    @Override
    public boolean startNestedScroll(int axes) {
        if (hasNestedScrollingParent()) {
            // Already in progress
            return true;
        }
        if (isNestedScrollingEnabled()) {
            ViewParent p = getParent();
            View child = this;
            while (p != null) {
                try {
                    if (p instanceof NestedScrollingParent) {
                        NestedScrollingParent parent = (NestedScrollingParent) p;
                        if (parent.onStartNestedScroll(child, this, axes)) {
                            mNestedScrollingParent = parent;
                            parent.onNestedScrollAccepted(child, this, axes);
                            return true;
                        }
                    }
                } catch (AbstractMethodError e) {
                    Log.e(VIEW_LOG_TAG, "ViewParent " + p + " does not implement interface " + "method onStartNestedScroll", e);
                    // Allow the search upward to continue
                }
                if (p instanceof View) {
                    child = (View) p;
                }
                p = p.getParent();
            }
        }

        return false;
    }


    @Override
    public void stopNestedScroll() {
        if (mNestedScrollingParent != null) {
            mNestedScrollingParent.onStopNestedScroll(this);
            mNestedScrollingParent = null;
        }
    }


    @Override
    public boolean hasNestedScrollingParent() {
        return mNestedScrollingParent != null;
    }

    @Override
    public boolean dispatchNestedScroll(int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed, @Nullable int[] offsetInWindow) {
        if (isNestedScrollingEnabled() && mNestedScrollingParent != null) {
            if (dxConsumed != 0 || dyConsumed != 0 || dxUnconsumed != 0 || dyUnconsumed != 0) {
                int startX = 0;
                int startY = 0;
                if (offsetInWindow != null) {
                    getLocationInWindow(offsetInWindow);
                    startX = offsetInWindow[0];
                    startY = offsetInWindow[1];
                }
                mNestedScrollingParent.onNestedScroll(this, dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed);
                if (offsetInWindow != null) {
                    getLocationInWindow(offsetInWindow);
                    offsetInWindow[0] -= startX;
                    offsetInWindow[1] -= startY;
                }
                return true;
            } else if (offsetInWindow != null) {
                // No motion, no dispatch. Keep offsetInWindow up to date.
                offsetInWindow[0] = 0;
                offsetInWindow[1] = 0;
            }
        }
        return false;
    }

    @Override
    public boolean dispatchNestedPreScroll(int dx, int dy, @Nullable int[] consumed, @Nullable int[] offsetInWindow) {
        if (isNestedScrollingEnabled() && mNestedScrollingParent != null) {
            if (dx != 0 || dy != 0) {
                int startX = 0;
                int startY = 0;
                if (offsetInWindow != null) {
                    getLocationInWindow(offsetInWindow);
                    startX = offsetInWindow[0];
                    startY = offsetInWindow[1];
                }

                if (consumed == null) {
                    if (mTempNestedScrollConsumed == null) {
                        mTempNestedScrollConsumed = new int[2];
                    }
                    consumed = mTempNestedScrollConsumed;
                }
                consumed[0] = 0;
                consumed[1] = 0;
                mNestedScrollingParent.onNestedPreScroll(this, dx, dy, consumed);

                if (offsetInWindow != null) {
                    getLocationInWindow(offsetInWindow);
                    offsetInWindow[0] -= startX;
                    offsetInWindow[1] -= startY;
                }
                return consumed[0] != 0 || consumed[1] != 0;
            } else if (offsetInWindow != null) {
                offsetInWindow[0] = 0;
                offsetInWindow[1] = 0;
            }
        }
        return false;
    }

//    @Override
//    public boolean dispatchNestedFling(float velocityX, float velocityY, boolean consumed) {
//        return false;
//    }
//
//    @Override
//    public boolean dispatchNestedPreFling(float velocityX, float velocityY) {
//        return false;
//    }
}
