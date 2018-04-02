package com.mainli.d.d2018.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.SpannableStringBuilder;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import com.mainli.d.d2018.R;
import com.mainli.d.d2018.utils.MarkDownURLMatcher;

/**
 * Created by Mainli on 2018-3-28.
 */
public class RichMediaActivity extends AppCompatActivity {
    String TEXT = "123456789[GFM语法 (GitHub Flavored Markdown)](https://github.com/guodongxiaren/README)" + "阿发达123132132阿萨德阿萨德阿萨德" + "[1]()" + "[baidu](http://www.baidu.com)[](http://www.baidu.com)+" + "奥术大师大所[#Asd asd](www.baidu.com)";

    private RecyclerView mRecyclerView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rich_media);
        final TextView tv = findViewById(R.id.tv);
        tv.setText(MarkDownURLMatcher.convertTextLinks(TEXT));
        tv.setMovementMethod(LinkMovementMethod.getInstance());

        final EditText et = findViewById(R.id.et);
        et.setImeOptions(EditorInfo.IME_ACTION_DONE);
        et.setSingleLine();
        et.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            SpannableStringBuilder text = null;

            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    if (text == null) {
                         text = MarkDownURLMatcher.convertTextLinks(v.getText().toString());
                    } else {
                        String s = v.getText().toString();
                        text.append(MarkDownURLMatcher.convertTextLinks(s.substring(text.length(), s.length())));
                    }
                    tv.setText(text);
                    et.setText(text);
                    et.setSelection(text.length());
                    return true;
                }
                return false;
            }
        });

//        et.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
////                et.setText(MarkDownURLMatcher.convertTextLinks(s.toString()));
//            }
//        });
    }


}
