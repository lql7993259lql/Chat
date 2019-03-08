package com.jixiang.chat.util;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.provider.Browser;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.text.style.URLSpan;
import android.view.View;
import android.widget.TextView;

public class UrlSpanUBB extends ClickableSpan {

	protected Context mContext;
	protected TextView mTextView;
	private SpannableStringBuilder style;
	private URLSpan[] urls;

	/**
	 * @param context
	 * @param tv
	 */
	public UrlSpanUBB(Context context, TextView tv) {
		mContext = context;
		mTextView = tv;
	}

	/**
	 * 更新Span
	 * 
	 * @param rv
	 */
	public SpannableStringBuilder updateClickSpan(CharSequence text) {
		try {
			if (text instanceof Spannable) {
				int end = text.length();
				Spannable sp = (Spannable) text;
				urls = sp.getSpans(0, end, URLSpan.class);
				style = new SpannableStringBuilder(text);
				style.clearSpans(); // should clear old spans
				for (URLSpan url : urls) {
					// 设置Span
					style.setSpan(this, sp.getSpanStart(url), sp.getSpanEnd(url), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return style;
	}

	@Override
	public void updateDrawState(TextPaint ds) {
		super.updateDrawState(ds);
		// 去掉超链接的下划线
		ds.setUnderlineText(false);
		ds.setColor(Color.parseColor("#388bbb"));
	}

	@Override
	public void onClick(View widget) {
		try {
			Uri uri = null;
			for (URLSpan url : urls) {
				uri = Uri.parse(url.getURL());
			}
			Context context = widget.getContext();
			Intent intent = new Intent(Intent.ACTION_VIEW, uri);
			intent.putExtra(Browser.EXTRA_APPLICATION_ID, context.getPackageName());
			context.startActivity(intent);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
