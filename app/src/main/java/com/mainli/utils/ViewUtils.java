package com.mainli.utils;

import android.annotation.TargetApi;
import android.graphics.Outline;
import android.os.Build;
import android.view.View;
import android.view.ViewOutlineProvider;

public final class ViewUtils {

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public static void clipRoundView(final View targetView, float radius) {
        targetView.setClipToOutline(true);
        targetView.setOutlineProvider(new ClipRoundViewOutlineProvider(radius));
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private static final class ClipRoundViewOutlineProvider extends ViewOutlineProvider {
        float radius = 0;

        public ClipRoundViewOutlineProvider(float radius) {
            this.radius = radius;
        }

        @Override
        public void getOutline(View view, Outline outline) {
            if (radius > 0) {
                outline.setRoundRect(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight(), radius);
            }
        }
    }
}
