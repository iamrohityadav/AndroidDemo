package com.mainli.d.d2018.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatEditText;
import android.text.Editable;
import android.text.Spanned;
import android.text.style.ReplacementSpan;
import android.util.AttributeSet;
import android.util.Log;
import android.util.SparseArray;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by lixiaoliang on 2018-5-3.
 */
public class LinkedEditText extends AppCompatEditText {

    public LinkedEditText(Context context) {
        super(context);
    }

    public LinkedEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public LinkedEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public String toMDString() {
        Editable text = getText();
        StringBuffer stringBuffer = new StringBuffer();
        LinkeSpan[] linkes = text.getSpans(0, text.length(), LinkeSpan.class);
        for (LinkeSpan linkeSpan : linkes) {
            linkeSpan.index = text.getSpanStart(linkeSpan);
        }
        Arrays.sort(linkes);
        char[] tmp;
        int start = 0;
        for (LinkeSpan linke : linkes) {
            int charCount = linke.index - start;
            if (charCount > 0) {
                tmp = new char[charCount];
                text.getChars(start, linke.index, tmp, 0);
                stringBuffer.append(tmp);
                stringBuffer.append(linke.toString());
                start = linke.index + 1;
            } else if (charCount == 0) {
                stringBuffer.append(linke.toString());
                start++;
            }
        }


        //补足剩余字符
        int length = text.length();
        if (start < length) {
            tmp = new char[length - start];
            text.getChars(start, length, tmp, 0);
            stringBuffer.append(tmp);
        }
        return stringBuffer.toString();
    }


    public void insertLinked(String name, String url) {
        insertMDLinked(getSelectionStart(), name, url, convertMDLinked(name, url));
    }

    @NonNull
    private String convertMDLinked(String name, String url) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append('[');
        stringBuilder.append(name);
        stringBuilder.append(']');
        stringBuilder.append('(');
        stringBuilder.append(url);
        stringBuilder.append(')');
        return stringBuilder.toString();
    }

    private void insertMDLinked(int where, String name, String url, String mdLinked) {
        Editable text = getText();
        LinkeSpan span = new LinkeSpan(name, url, mdLinked);
        text.insert(where, "#");
        text.setSpan(span, where, where + 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
    }

    private static class LinkeSpan extends ReplacementSpan implements Comparable<LinkeSpan> {
        private String urlName;
        private String md;
        private String url;
        private int index = 0;

        public void setIndex(int index) {
            this.index = index;
        }

        public LinkeSpan(String urlName, String url, String md) {
            this.urlName = urlName;
            this.url = url;
            this.md = md;
        }

        @Override
        public String toString() {
            return md;
        }

        @Override
        public int getSize(@NonNull Paint paint, CharSequence text, int start, int end, @Nullable Paint.FontMetricsInt fm) {
            return (int) (paint.measureText(urlName) + 0.5f);
        }

        @Override
        public void draw(@NonNull Canvas canvas, CharSequence text, int start, int end, float x, int top, int y, int bottom, @NonNull Paint paint) {
            int color = paint.getColor();
            paint.setColor(Color.RED);
            canvas.drawText(urlName, x, y, paint);
            paint.setColor(color);
        }

        @Override
        public int compareTo(@NonNull LinkeSpan o) {
            return index - o.index;
        }
    }
}
