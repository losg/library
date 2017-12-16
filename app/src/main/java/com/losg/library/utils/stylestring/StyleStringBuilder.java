package com.losg.library.utils.stylestring;

import android.content.Context;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Be similar with {@link StringBuffer} but both {@link String} and
 * {@link StyleString} can be appended into it.
 */
public class StyleStringBuilder {

    private SpannableStringBuilder builder = new SpannableStringBuilder();

    public StyleStringBuilder() {
    }

    public StyleStringBuilder(CharSequence text) {
        builder.append(text);
    }

    public StyleStringBuilder(StyleString styleString) {
        builder.append(styleString.toStyleString());
    }

    public StyleStringBuilder append(CharSequence text) {
        builder.append(text);
        return this;
    }

    public StyleStringBuilder append(StyleString styleString) {
        builder.append(styleString.toStyleString());
        return this;
    }

    public Spanned build() {
        return builder;
    }

    public static CharSequence formatString(Context context, String text, String regular, int colorResId) {

        try {
            SpannableStringBuilder builder = new SpannableStringBuilder(text);

            Pattern p = Pattern.compile(regular);
            Matcher matcher = p.matcher(text);
            int start = 0;
            int end = 0;
            while (matcher.find()) {
                if (matcher.start() == end) {
                    end = matcher.end();
                } else {
                    if (start != end) {
                        ForegroundColorSpan span = new ForegroundColorSpan(context.getResources().getColor(colorResId));
                        builder.setSpan(span, start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    }
                    start = matcher.start();
                    end = matcher.end();
                }
            }
            if (start != end) {
                ForegroundColorSpan span = new ForegroundColorSpan(context.getResources().getColor(colorResId));
                builder.setSpan(span, start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
            return builder;
        } catch (Exception e) {
            return text;
        }
    }
}
