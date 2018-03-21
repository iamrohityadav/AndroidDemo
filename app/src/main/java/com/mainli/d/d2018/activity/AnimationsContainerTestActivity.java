package com.mainli.d.d2018.activity;

import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.mainli.d.d2018.R;
import com.mainli.d.d2018.animutils.AnimationsContainer;


/**
 * TITLE
 * Created by shixiaoming on 16/12/27.
 * 来源: https://github.com/VDshixiaoming/AnimationTest
 */
public class AnimationsContainerTestActivity extends AppCompatActivity {
    private ImageView imageView;
    private Button playBtn1, playBtn2;
    private AnimationDrawable animationDrawable;
    private int mode;
    AnimationsContainer.FramesSequenceAnimation animation;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.animations_container_test_activity);

        imageView = findViewById(R.id.imgview);
        playBtn1 = findViewById(R.id.play_btn1);
        playBtn2 = findViewById(R.id.play_btn2);
        playBtn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (animationDrawable != null && animationDrawable.isRunning()) {
                    animationDrawable.stop();
                    playBtn2.setText("普通帧动画 - START");
                }
                if (animation == null) {
                    animation = AnimationsContainer.getInstance(R.array.loading_anim, 58).createProgressDialogAnim(imageView);
                }
                if (!switchBtn1()) {
                    animation.start();
                } else {
                    animation.stop();
                }
                animationDrawable = null;
            }
        });

        playBtn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (animation != null && animation.isRunning()) {
                    animation.stop();
                    playBtn1.setText("优化帧动画 - START");
                }
                if (animationDrawable == null) {
                    imageView.setImageResource(R.drawable.loading_anim);
                    animationDrawable = (AnimationDrawable) imageView.getDrawable();
                }
                if (!switchBtn2()) {
                    animationDrawable.start();
                } else {
                    animationDrawable.stop();
                }

            }
        });


    }

    //控制开关
    private boolean switchBtn1() {
        boolean returnV = animation.isRunning();
        playBtn1.setText(returnV ? "优化帧动画 - START" : "优化帧动画 - STOP");
        return returnV;
    }

    //控制开关
    private boolean switchBtn2() {
        boolean returnV = animationDrawable.isRunning();
        playBtn2.setText(returnV ? "普通帧动画 - START" : "普通帧动画 - STOP");
        return returnV;
    }
}
