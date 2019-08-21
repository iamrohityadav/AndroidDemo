package com.mainli.utils;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Camera;
import android.graphics.Point;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;

import java.lang.reflect.Method;

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

    public static final int dp2PixelsInt(float dp) {
        return (int) (dp2Px(dp) + 0.5F);
    }

    public static final float sp2Px(float sp) {
        return sp * SYSTEM_DISPLAY_METRICS.density;
    }

    public static final float sp2PixelsInt(float sp) {
        return (int) (sp2Px(sp) + 0.5F);
    }

    /**
     * 调整Camera绘制时翻转相机高度,适配手机
     * 默认 camera.setLocation(0,0,-8);
     */
    public static final void adjustCameraZHeight(Camera camera) {
        camera.setLocation(0, 0, -6 * SYSTEM_DISPLAY_METRICS.density);
    }

    public static int getNavigationHeightFromResource(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        int navigationBarHeight = 0;
        Resources resources = context.getResources();
        int resourceId = resources.getIdentifier("config_showNavigationBar",
                "bool", "android");
        if (resourceId > 0) {
            boolean hasNav = resources.getBoolean(resourceId);
            if (hasNav) {
                resourceId = resources.getIdentifier("navigation_bar_height",
                        "dimen", "android");
                if (resourceId > 0) {
                    navigationBarHeight = resources
                            .getDimensionPixelSize(resourceId);
                }
            }
        }

        if (navigationBarHeight <= 0) {
            DisplayMetrics dMetrics = new DisplayMetrics();
            display.getMetrics(dMetrics);
            int screenHeight = Math.max(dMetrics.widthPixels, dMetrics.heightPixels);
            int realHeight = 0;
            try {
                Method mt = display.getClass().getMethod("getRealSize", Point.class);
                Point size = new Point();
                mt.invoke(display, size);
                realHeight = Math.max(size.x, size.y);
            } catch (NoSuchMethodException e) {
                Method mt = null;
                try {
                    mt = display.getClass().getMethod("getRawHeight");
                } catch (NoSuchMethodException e2) {
                    try {
                        mt = display.getClass().getMethod("getRealHeight");
                    } catch (NoSuchMethodException e3) {
                    }
                }
                if (mt != null) {
                    try {
                        realHeight = (Integer) mt.invoke(display);
                    } catch (Exception e1) {
                    }
                }
            } catch (Exception e) {
            }
            navigationBarHeight = realHeight - screenHeight;
        }

        return navigationBarHeight;
    }

}
