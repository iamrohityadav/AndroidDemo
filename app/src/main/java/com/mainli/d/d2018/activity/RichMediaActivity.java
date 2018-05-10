package com.mainli.d.d2018.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.mainli.d.d2018.R;
import com.mainli.d.d2018.utils.MarkDownURLMatcher;
import com.mainli.d.d2018.view.LinkedEditText;

/**
 * Created by Mainli on 2018-3-28.
 */
public class RichMediaActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rich_media);
        final LinkedEditText et = findViewById(R.id.et);
        final TextView tv = findViewById(R.id.tv);
        final TextView tv1 = findViewById(R.id.tv1);
        tv1.setMovementMethod(LinkMovementMethod.getInstance());
        findViewById(R.id.btn_output).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tv.setText(et.toMDString());
            }
        });
        findViewById(R.id.btn_convert_output).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tv1.setText(MarkDownURLMatcher.convertTextLinks(et.toMDString()));
            }
        });
        findViewById(R.id.btn1).setOnClickListener(new View.OnClickListener() {
            private int count = 0;

            @Override
            public void onClick(View v) {
                et.insertLinked("链接" + count++, "http://www.baidu.com");
            }
        });
    }

}
