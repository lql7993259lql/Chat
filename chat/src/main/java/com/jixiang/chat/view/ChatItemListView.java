package com.jixiang.chat.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

public class ChatItemListView  extends ListView {
	
	public ChatItemListView(Context context) {
		super(context);
	}
	public ChatItemListView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	public ChatItemListView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}
    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) { 
        int heightSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, heightSpec);
    }
}