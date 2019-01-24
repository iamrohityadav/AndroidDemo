package com.mainli.activity

import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.SeekBar
import com.mainli.R
import com.mainli.utils.toDpInt
import com.mainli.view.RoundFrameLayout

class RoundActivity : SeekBarActivity() {
    lateinit var roundFrameLayout: RoundFrameLayout
    override fun attachView(linearlayout: LinearLayout) {
        roundFrameLayout = RoundFrameLayout(this)
        val imageView = ImageView(this)
        imageView.scaleType = ImageView.ScaleType.CENTER_CROP
        imageView.setImageResource(R.mipmap.img_test);
        roundFrameLayout.addView(imageView, FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT))
        val layoutParams = LinearLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT)
        val dp50 = 50.toDpInt()
        layoutParams.leftMargin = dp50
        layoutParams.topMargin = dp50
        layoutParams.rightMargin = dp50
        layoutParams.bottomMargin = dp50
        linearlayout.addView(roundFrameLayout, layoutParams)
    }

    override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
        roundFrameLayout.setRadius(progress.toDpInt())
    }

}