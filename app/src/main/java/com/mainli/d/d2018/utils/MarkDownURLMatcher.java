package com.mainli.d.d2018.utils;

import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.URLSpan;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Mainli on 2018-3-29.
 */
public class MarkDownURLMatcher {
    private final static String URL_NAME = "[\\w \\(\\)\\t#&%$@\\u4e00-\\u9fa5]*";
    private final static String HTTP = "(https?://)?[-A-Za-z0-9+&@#/%?=~_|!:,.;]+[-A-Za-z0-9+&@#/%=~_|]";
    private final static String MATCHER = "\\[(" + URL_NAME + ")\\]\\((" + HTTP + ")\\)";
    private final static Pattern MD_URL_MATCHER = Pattern.compile(MATCHER);

    /**
     * 收集文本中markdown链接 转换为SpannerString
     * 格式:[xxx](xxxx)
     */
    public static final SpannableStringBuilder convertTextLinks(String text) {
        Matcher m = MD_URL_MATCHER.matcher(text);
        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder();
        int lastIndex = 0;
        while (m.find()) {
            String name = m.group(1);
            String url = m.group(2);
            if (TextUtils.isEmpty(name)) {
                name = "网页链接";
            }
            int start = m.start();
            String substring = text.substring(lastIndex, start);
            spannableStringBuilder.append(substring);
            int urlTextStart = spannableStringBuilder.length();
            spannableStringBuilder.append(name);
            spannableStringBuilder.setSpan(new URLSpan(url), urlTextStart, urlTextStart + name.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            lastIndex = m.end();
        }
        if (lastIndex == 0) {
            spannableStringBuilder.append(text);
        } else if (text.length() > lastIndex) {
            spannableStringBuilder.append(text.substring(lastIndex, text.length()));
        }
        return spannableStringBuilder;
    }

    /**
     * 收集文本中markdown链接
     * 格式:[xxx](xxxx)
     */
    public static final ArrayList<LinkSpec> gatherLinks(String text) {
        Matcher m = MD_URL_MATCHER.matcher(text);
        ArrayList<LinkSpec> links = new ArrayList<LinkSpec>();
        while (m.find()) {
            int start = m.start();
            int end = m.end();
            LinkSpec spec = makeLinkSpec(m, start, end);
            links.add(spec);
        }
        return links;
    }

    private static LinkSpec makeLinkSpec(Matcher group, int start, int end) {
        LinkSpec spec = new LinkSpec();
        spec.text = group.group(0);
        spec.url = group.group(2);
        String urlName = group.group(1);
        spec.urlName = TextUtils.isEmpty(urlName) ? spec.url : urlName;
        spec.start = start;
        spec.end = end;
        return spec;
    }

    public static class LinkSpec {
        String text;
        String url;
        String urlName;
        int start;
        int end;

        @Override
        public String toString() {
            return "text = " + text + '\n' + "url = " + url + '\n' + "urlName = " + urlName + '\n' + "start = " + start + " | end = " + end + "\n-------------------------------------------------------";
        }
    }
}
