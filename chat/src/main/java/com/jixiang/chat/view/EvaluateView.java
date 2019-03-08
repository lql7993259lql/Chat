package com.jixiang.chat.view;

import com.jixiang.chat.R;
import com.jixiang.chat.ui.MainActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class EvaluateView extends LinearLayout{
	private ImageView image1, image2, image3, image4, image5;
	private int image1X, image2X, image3X, image4X, image5X;
	public static boolean isEvaluate = false;
	public static int count = 0;
	public static String evaStr = "请对本次服务进行评价";
	
	public EvaluateView(Context context) {
		super(context);
		init(context);
	}

	@SuppressLint("NewApi")
	public EvaluateView(Context context, AttributeSet attrs) {
		super(context, attrs);
		if(!isInEditMode()){
			init(context);
		}
	}

	@SuppressLint("NewApi")
	private void init(Context context) {
		View.inflate(context, R.layout.chat_evaluate_item, this);
		image1 = (ImageView) findViewById(R.id.image1);
		image2 = (ImageView) findViewById(R.id.image2);
		image3 = (ImageView) findViewById(R.id.image3);
		image4 = (ImageView) findViewById(R.id.image4);
		image5 = (ImageView) findViewById(R.id.image5);
	}

	
	
	public boolean onTouchEvent(MotionEvent event) {
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			int[] loc1 = new int[2];
			int[] loc2 = new int[2];
			int[] loc3 = new int[2];
			int[] loc4 = new int[2];
			int[] loc5 = new int[2];
			image1.getLocationInWindow(loc1);
			image2.getLocationInWindow(loc2);
			image3.getLocationInWindow(loc3);
			image4.getLocationInWindow(loc4);
			image5.getLocationInWindow(loc5);
			image1X = loc1[0];
			image2X = loc2[0];
			image3X = loc3[0];
			image4X = loc4[0];
			image5X = loc5[0];
			int x = (int) event.getRawX();
			if(x < 0) x = 0;
			if(x > getMeasuredWidth()) x = getMeasuredWidth();
			break;
		case MotionEvent.ACTION_MOVE:
			int x1 = (int) event.getX();
			if(x1 >= image5X){
				image1.setImageResource(R.drawable.chatshixinxingxing36);
				image2.setImageResource(R.drawable.chatshixinxingxing36);
				image3.setImageResource(R.drawable.chatshixinxingxing36);
				image4.setImageResource(R.drawable.chatshixinxingxing36);
				image5.setImageResource(R.drawable.chatshixinxingxing36);
				count = 5;
				isEvaluate = true;
				evaStr = "非常满意";
			}else if(x1 >= image4X){
				image1.setImageResource(R.drawable.chatshixinxingxing36);
				image2.setImageResource(R.drawable.chatshixinxingxing36);
				image3.setImageResource(R.drawable.chatshixinxingxing36);
				image4.setImageResource(R.drawable.chatshixinxingxing36);
				image5.setImageResource(R.drawable.chatkongxinxingxing35);
				count = 4;
				isEvaluate = true;
				evaStr = "满意";
			}else if(x1 >= image3X){
				image1.setImageResource(R.drawable.chatshixinxingxing36);
				image2.setImageResource(R.drawable.chatshixinxingxing36);
				image3.setImageResource(R.drawable.chatshixinxingxing36);
				image4.setImageResource(R.drawable.chatkongxinxingxing35);
				image5.setImageResource(R.drawable.chatkongxinxingxing35);
				count = 3;
				isEvaluate = true;
				evaStr = "一般";
			}else if(x1 >= image2X){
				image1.setImageResource(R.drawable.chatshixinxingxing36);
				image2.setImageResource(R.drawable.chatshixinxingxing36);
				image3.setImageResource(R.drawable.chatkongxinxingxing35);
				image4.setImageResource(R.drawable.chatkongxinxingxing35);
				image5.setImageResource(R.drawable.chatkongxinxingxing35);
				count = 2;
				isEvaluate = true;
				evaStr = "不满意";
			}else if(x1 >= image1X){
				image1.setImageResource(R.drawable.chatshixinxingxing36);
				image2.setImageResource(R.drawable.chatkongxinxingxing35);
				image3.setImageResource(R.drawable.chatkongxinxingxing35);
				image4.setImageResource(R.drawable.chatkongxinxingxing35);
				image5.setImageResource(R.drawable.chatkongxinxingxing35);
				count = 1;
				isEvaluate = true;
				evaStr = "非常不满意";
			}else{
				image1.setImageResource(R.drawable.chatkongxinxingxing35);
				image2.setImageResource(R.drawable.chatkongxinxingxing35);
				image3.setImageResource(R.drawable.chatkongxinxingxing35);
				image4.setImageResource(R.drawable.chatkongxinxingxing35);
				image5.setImageResource(R.drawable.chatkongxinxingxing35);
				count = 0;
				isEvaluate = false;
				evaStr = "请对本次服务进行评价";
			}
			MainActivity.setEvaluateStr(evaStr);
			break;
		case MotionEvent.ACTION_UP:
			int x2 = (int) event.getX();
			if(x2 >= image5X){
				image1.setImageResource(R.drawable.chatshixinxingxing36);
				image2.setImageResource(R.drawable.chatshixinxingxing36);
				image3.setImageResource(R.drawable.chatshixinxingxing36);
				image4.setImageResource(R.drawable.chatshixinxingxing36);
				image5.setImageResource(R.drawable.chatshixinxingxing36);
				count = 5;
				isEvaluate = true;
				evaStr = "非常满意";
			}else if(x2 >= image4X){
				image1.setImageResource(R.drawable.chatshixinxingxing36);
				image2.setImageResource(R.drawable.chatshixinxingxing36);
				image3.setImageResource(R.drawable.chatshixinxingxing36);
				image4.setImageResource(R.drawable.chatshixinxingxing36);
				image5.setImageResource(R.drawable.chatkongxinxingxing35);
				count = 4;
				isEvaluate = true;
				evaStr = "满意";
			}else if(x2 >= image3X){
				image1.setImageResource(R.drawable.chatshixinxingxing36);
				image2.setImageResource(R.drawable.chatshixinxingxing36);
				image3.setImageResource(R.drawable.chatshixinxingxing36);
				image4.setImageResource(R.drawable.chatkongxinxingxing35);
				image5.setImageResource(R.drawable.chatkongxinxingxing35);
				count = 3;
				isEvaluate = true;
				evaStr = "一般";
			}else if(x2 >= image2X){
				image1.setImageResource(R.drawable.chatshixinxingxing36);
				image2.setImageResource(R.drawable.chatshixinxingxing36);
				image3.setImageResource(R.drawable.chatkongxinxingxing35);
				image4.setImageResource(R.drawable.chatkongxinxingxing35);
				image5.setImageResource(R.drawable.chatkongxinxingxing35);
				count = 2;
				isEvaluate = true;
				evaStr = "不满意";
			}else if(x2 >= image1X){
				image1.setImageResource(R.drawable.chatshixinxingxing36);
				image2.setImageResource(R.drawable.chatkongxinxingxing35);
				image3.setImageResource(R.drawable.chatkongxinxingxing35);
				image4.setImageResource(R.drawable.chatkongxinxingxing35);
				image5.setImageResource(R.drawable.chatkongxinxingxing35);
				count = 1;
				isEvaluate = true;
				evaStr = "非常不满意";
			}else{
				image1.setImageResource(R.drawable.chatkongxinxingxing35);
				image2.setImageResource(R.drawable.chatkongxinxingxing35);
				image3.setImageResource(R.drawable.chatkongxinxingxing35);
				image4.setImageResource(R.drawable.chatkongxinxingxing35);
				image5.setImageResource(R.drawable.chatkongxinxingxing35);
				count = 0;
				isEvaluate = false;
				evaStr = "请对本次服务进行评价";
			}
			MainActivity.setEvaluateStr(evaStr);
			break;
		}
		return true;
	}

}
