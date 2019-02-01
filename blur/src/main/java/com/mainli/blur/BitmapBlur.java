package com.mainli.blur;

import android.graphics.Bitmap;

/**
 * Created by mobimagic on 2017/12/5.
 */

public class BitmapBlur {
    static {
        System.loadLibrary("blur-lib");
    }

    /**
     * 轻微模糊5 一般模糊15 强烈模糊25
     * @param bimap
     * @param intensity
     * @return
     */
    public native static Bitmap blur(Bitmap bimap, float intensity);
}
