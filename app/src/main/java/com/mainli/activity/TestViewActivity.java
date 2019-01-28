package com.mainli.activity;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.SeekBar;

import com.mainli.view.CamearDemoView;

import org.jetbrains.annotations.NotNull;

public class TestViewActivity extends SeekBarActivity {

    private View mChild;

    @Override
    public void attachView(@NotNull LinearLayout linearlayout) {
//        mChild = new DashboardView(this);
//        mChild = new PieView(this);
        mChild = new CamearDemoView(this);
        linearlayout.addView(mChild);
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
//        ((DashboardView) mChild).setPointerCount(progress);
//        ((PieView) mChild).setCurrentPostion(progress);
        ((CamearDemoView) mChild).setRotate(progress);
    }

    @Override
    public int max() {
        return 720;
    }

}
