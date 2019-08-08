package com.mainli.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.OverScroller;

import com.mainli.view.MultiPointTouch.MultiPointRelayView;

/**
 * 练习ViewGroup滑动与多点触控
 */
public class MyViewPager extends ViewGroup implements MultiPointRelayView.OnViewTouchFinish {

    private final int SCALED_TOUCH_SLOP;
    private final int MIN_FLING_VELOCITY;
    private final int MAX_FLING_VELOCITY;
    private final int LONG_PRESS_TIMEOUT;
    private int mActivePointerId;
    private int mViewTouchFinishPagesIndex = 1;

    public MyViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        measureChildren(widthMeasureSpec, heightMeasureSpec);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    private VelocityTracker mVelocityTracker;
    private OverScroller mOverScroller;

    {
        Context context = getContext();
        ViewConfiguration viewConfiguration = ViewConfiguration.get(context);
        SCALED_TOUCH_SLOP = viewConfiguration.getScaledTouchSlop();

        MAX_FLING_VELOCITY = viewConfiguration.getScaledMaximumFlingVelocity();
        MIN_FLING_VELOCITY = viewConfiguration.getScaledMinimumFlingVelocity() << 1;
        LONG_PRESS_TIMEOUT = ViewConfiguration.getLongPressTimeout();
        mVelocityTracker = VelocityTracker.obtain();

        mOverScroller = new OverScroller(context);

    }

    private int[] pageLefts;

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int childCount = getChildCount();
        int nextLeft = l;
        pageLefts = new int[childCount];
        for (int i = 0; i < childCount; i++) {
            pageLefts[i] = nextLeft;
            View child = getChildAt(i);
            int right = nextLeft + child.getMeasuredWidth();
            child.layout(nextLeft, t, right, b);
            nextLeft = right;
        }
    }

    private int currentScrollX;
    private float mOldX;
    private boolean mIsBeingDragged;
    private long mInterceptActionDownTime = 0;

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        boolean result = false;
        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                actionDown(event);
                mInterceptActionDownTime = System.currentTimeMillis();
                break;
            case MotionEvent.ACTION_MOVE:
                int pointerIndex = event.findPointerIndex(mActivePointerId);
                if (!mIsBeingDragged//未拖拽
                        && (System.currentTimeMillis() - mInterceptActionDownTime > LONG_PRESS_TIMEOUT)//长按
                        && Math.abs(mOldX - event.getX(pointerIndex)) > SCALED_TOUCH_SLOP) {
                    getParent().requestDisallowInterceptTouchEvent(true);
                    result = true;
                    mOldX = event.getX(pointerIndex);
                    currentScrollX = getScrollX();
                    mVelocityTracker.clear();
                    mIsBeingDragged = true;
                }
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
                pointDown(event);
                break;
            case MotionEvent.ACTION_POINTER_UP:
                onSecondaryPointerUp(event);
                mVelocityTracker.clear();
                break;
        }
        return result;
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                actionDown(event);
                break;
            case MotionEvent.ACTION_MOVE:
                float offsedX = mOldX - event.getX(event.findPointerIndex(mActivePointerId));
                if (!mIsBeingDragged && Math.abs(offsedX) > SCALED_TOUCH_SLOP) {
                    getParent().requestDisallowInterceptTouchEvent(true);
                    mVelocityTracker.clear();
                    mIsBeingDragged = true;
                    if (offsedX > 0) {
                        offsedX += SCALED_TOUCH_SLOP;
                    } else {
                        offsedX -= SCALED_TOUCH_SLOP;
                    }
                }
                if (mIsBeingDragged) {
                    mVelocityTracker.addMovement(event);
                    int scrollX = currentScrollX + (int) (offsedX);
                    setScrollX(scrollX);
                }
                break;
            case MotionEvent.ACTION_UP:
                if (mIsBeingDragged) {
                    int scrollX = getScrollX();
                    mVelocityTracker.addMovement(event);
                    mVelocityTracker.computeCurrentVelocity(1000, MAX_FLING_VELOCITY);
                    float xVelocity = mVelocityTracker.getXVelocity();
                    endTouchEventScrollTargetPager(scrollX, xVelocity);
                }
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
                pointDown(event);
                break;
            case MotionEvent.ACTION_POINTER_UP:
                onSecondaryPointerUp(event);
                break;
        }
        return true;
    }

    private void actionDown(MotionEvent event) {
        mActivePointerId = event.getPointerId(0);
        if (!mOverScroller.isFinished()) {
            mOverScroller.abortAnimation();
        }
        mOldX = event.getX();
        currentScrollX = getScrollX();
        mIsBeingDragged = false;
    }

    private void pointDown(MotionEvent event) {
        final int index = event.getActionIndex();
        mOldX = event.getX(index);
        mActivePointerId = event.getPointerId(index);
        currentScrollX = getScrollX();
    }

    private void onSecondaryPointerUp(MotionEvent event) {
        int activeIndex;
        activeIndex = event.getActionIndex();
        if (mActivePointerId == event.getPointerId(activeIndex)) {
            activeIndex = activeIndex == 0 ? 1 : 0;
            mActivePointerId = event.getPointerId(activeIndex);
            mOldX = event.getX(activeIndex);
            currentScrollX = getScrollX();
        }
    }


    /**
     * 结束触摸时滚动到指定页面
     */
    private void endTouchEventScrollTargetPager(int scrollX, float xVelocity) {
        int numberPages = getNumberPages(scrollX);
        if (Math.abs(xVelocity) > MIN_FLING_VELOCITY) {
            if (xVelocity < 0) {
                numberPages++;
            }
            mOverScroller.startScroll(scrollX, 0, pageLefts[numberPages] - scrollX, 0);
            postInvalidateOnAnimation();
        } else {
            int targetPagerIndex = scrollX - pageLefts[numberPages] > getWidth() >> 1 ? ++numberPages : numberPages;
            mOverScroller.startScroll(scrollX, 0, pageLefts[targetPagerIndex] - scrollX, 0);
            postInvalidateOnAnimation();
        }
    }

    @Override
    public void computeScroll() {
        if (mOverScroller.computeScrollOffset()) {
            setScrollX(mOverScroller.getCurrX());
            postInvalidateOnAnimation();
        }
    }

    private int getNumberPages(int scrollX) {
        int childCount = getChildCount();
        int centerIndex = childCount >> 1;
        int targetLessThanIndex = 0;
        if (scrollX < pageLefts[centerIndex]) {
            for (int i = centerIndex; i > 0; i--) {
                if (scrollX > pageLefts[i]) {
                    targetLessThanIndex = i;
                    break;
                }
            }
            return targetLessThanIndex;
        } else if (scrollX > pageLefts[centerIndex]) {
            targetLessThanIndex = childCount - 1;
            for (int i = centerIndex; i < childCount; i++) {
                if (scrollX < pageLefts[i]) {
                    targetLessThanIndex = i;
                    break;
                }
            }
            return --targetLessThanIndex;
        } else {
            return centerIndex;
        }
    }


    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                mOldX = event.getX();
                direction = 0;
                break;
            case MotionEvent.ACTION_MOVE:
                int pointerIndex = event.findPointerIndex(mActivePointerId);
                float x = event.getX(pointerIndex);
                int currentDirection = (int) (mOldX - x);
                if (direction != 0 && isDirectionEqual(direction, currentDirection) && Math.abs(getScrollX() - pageLefts[mViewTouchFinishPagesIndex]) < SCALED_TOUCH_SLOP) {
                    direction = 0;
                    mIsBeingDragged = false;
                    final ViewParent parent = getParent();
                    if (parent != null) {
                        parent.requestDisallowInterceptTouchEvent(true);
                    }
                    mOldX = pageLefts[mViewTouchFinishPagesIndex];
                    scrollTo(pageLefts[mViewTouchFinishPagesIndex],0);
                    event.setAction(MotionEvent.ACTION_CANCEL);
                    MotionEvent ev2 = MotionEvent.obtain(event);
                    dispatchTouchEvent(event);
                    ev2.setAction(MotionEvent.ACTION_DOWN);
                    return dispatchTouchEvent(ev2);
                }
                break;
        }
        return super.dispatchTouchEvent(event);
    }

    private boolean isDirectionEqual(int pointerIndex, int x) {
        if ((pointerIndex > 0 && x > 0) || (pointerIndex < 0 && x < 0)) {
            return true;
        }
        return false;
    }

    int direction = 0;

    @Override
    public boolean isTouchFinish(int direction) {
        this.direction = direction;
        Log.d("Mainli", "direction:" + direction);
        return false;
    }
}
