package com.mainli.utils;

import android.content.res.Resources;
import android.graphics.Camera;
import android.util.DisplayMetrics;

public final class SizeUtil {

    private static final DisplayMetrics SYSTEM_DISPLAY_METRICS = Resources.getSystem().getDisplayMetrics();

    public static final int getScreenWidthPixels() {
        return SYSTEM_DISPLAY_METRICS.widthPixels;
    }

    public static final int getScreenHeightPixels() {
        return SYSTEM_DISPLAY_METRICS.heightPixels;
    }

    public static final float dp2Px(float dp) {
        return dp * SYSTEM_DISPLAY_METRICS.density;
    }

    public static final float sp2Px(float sp) {
        return sp * SYSTEM_DISPLAY_METRICS.density;
    }

    /**
     * 修复Camera绘制时翻转相机高度
     * 默认 camera.setLocation(0,0,-8,);
     */
    public static final void fixCameraZ(Camera camera) {
        camera.setLocation(0, 0, -6 * SYSTEM_DISPLAY_METRICS.density);
    }
}
