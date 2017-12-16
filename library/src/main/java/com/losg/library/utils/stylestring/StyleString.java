package com.losg.library.utils.stylestring;

import android.graphics.Typeface;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.BackgroundColorSpan;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.StrikethroughSpan;
import android.text.style.StyleSpan;
import android.text.style.URLSpan;
import android.text.style.UnderlineSpan;

import java.io.Serializable;

/**
 * 
 * A rich string which can set different style for string, like foreground
 * color, background color and so on.
 *
 * 
 */
public class StyleString implements Serializable {
	private String                 mText;
	private SpannableStringBuilder mBuilder;

	public StyleString(String text) {
		mBuilder = new SpannableStringBuilder();
		if(TextUtils.isEmpty(text)) text = "";
		mBuilder.append(mText = text);
	}

	public StyleString setForegroundColor(int color) {
		mBuilder.setSpan(new ForegroundColorSpan(color), 0, mText.length(),
				Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		return this;
	}

	public StyleString setBackgroundColor(int color) {
		mBuilder.setSpan(new BackgroundColorSpan(color), 0, mText.length(),
				Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		return this;
	}

	public StyleString setFontSizePX(int fontSizePX) {
		mBuilder.setSpan(new AbsoluteSizeSpan(fontSizePX), 0, mText.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		return this;
	}

	/**
	 * About typeFace see {@link Typeface}
	 */
	public StyleString setFontStyle(int typeFace) {
		mBuilder.setSpan(new StyleSpan(typeFace), 0, mText.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		return this;
	}

	public StyleString setUnderline() {
		mBuilder.setSpan(new UnderlineSpan(), 0, mText.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		return this;
	}

	/**
	 * If StyleString was used as content of TextView or EditText,
	 * setMovementMethod should be called like below:
	 * 
	 * <pre>
	 * TextView textView = new TextView(context);
	 * textView.setMovementMethod(LinkMovementMethod.getInstance());
	 * </pre>
	 */
	public StyleString setClickable(ClickableSpan clickable) {
		mBuilder.setSpan(clickable, 0, mText.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		return this;
	}

	public StyleString setUri(String uri) {
		mBuilder.setSpan(new URLSpan(uri), 0, mText.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		return this;
	}

	public StyleString setStrikethrough() {
		mBuilder.setSpan(new StrikethroughSpan(), 0, mText.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		return this;
	}

	public SpannableStringBuilder toStyleString() {
		return mBuilder;
	}
}
