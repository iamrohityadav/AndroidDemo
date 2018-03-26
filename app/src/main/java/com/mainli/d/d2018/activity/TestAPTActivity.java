package com.mainli.d.d2018.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.mainli.d.annotations.BindView;
import com.mainli.d.d2018.R;

/**
 * Created by Mainli on 2018-3-26.
 * 日志文件 放在D:\processor-log.txt
 */
public class TestAPTActivity extends AppCompatActivity {
    @BindView(R.id.bottom)
    TextView tx;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TextView textView = new TextView(this);
        textView.setText(Log.log);
        setContentView(textView);
    }
}
