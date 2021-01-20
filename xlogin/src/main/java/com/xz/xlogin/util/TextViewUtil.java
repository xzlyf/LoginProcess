package com.xz.xlogin.util;

import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.URLSpan;
import android.widget.TextView;

/**
 * @author czr
 * @email czr2001@outlook.com
 * @date 2020/12/11
 */
public class TextViewUtil {

	/**
	 * 拦截文本框超链接
	 *
	 * @param tv
	 */
	public static void interceptHyperLink(TextView tv) {
		CharSequence text = tv.getText();
		if (text instanceof Spannable) {
			int end = text.length();
			Spannable sp = (Spannable) tv.getText();
			URLSpan[] spans = sp.getSpans(0, end, URLSpan.class);
			SpannableStringBuilder style = new SpannableStringBuilder(text);
			style.clearSpans();// should clear old spans
			for (URLSpan span : spans) {
				CustomUrlSpan mySpan = new CustomUrlSpan(span.getURL());
				style.setSpan(mySpan, sp.getSpanStart(span), sp.getSpanEnd(span), Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
			}
			tv.setText(style);
		}
	}
}
