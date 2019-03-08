package com.jixiang.chat.ui;

import java.lang.ref.WeakReference;
import java.util.concurrent.Executors;

import com.jixiang.chat.R;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
/**
 * 相机选择照片界面
 * @author Wangda
 *
 */
public class CameraActivity extends Activity {
	private ImageView ivPhoto,ivSendBack;
	private Button sendPhoto;
	private Bitmap bitmap;
	private String path;
	public static final String SEND_PHOTO = "SEND_PHOTO";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		try {
			setContentView(R.layout.chat_activity_camera);
			initView();
			Intent intent = getIntent();
			path = intent.getStringExtra("path");
			BitmapWorkerTask bwt = new BitmapWorkerTask(ivPhoto);
			bwt.executeOnExecutor(Executors.newCachedThreadPool());
			setListeners();
		} catch (Exception e) {
		}
	}
	
	/**
	 * 监听器
	 */
	private void setListeners() {
		sendPhoto.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setAction(SEND_PHOTO);
				intent.putExtra("b", path);
				CameraActivity.this.sendBroadcast(intent);
				finish();
			}
		});
		ivSendBack.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}
	
	/**
	 * 初始化
	 */
	private void initView() {
		ivPhoto = (ImageView) findViewById(R.id.iv_send_ptoto);
		ivSendBack = (ImageView) findViewById(R.id.iv_send_photo_back);
		sendPhoto = (Button) findViewById(R.id.bt_send_photo);
	}
	
	/**
	 * 异步加载并设置图片
	 * @author wangda
	 */
	class BitmapWorkerTask extends AsyncTask<Integer, Void, Bitmap>{
		private final WeakReference<ImageView> imageViewReference;
		
		public BitmapWorkerTask(ImageView imageView) {
			imageViewReference = new WeakReference<ImageView>(imageView);
		}
		@Override
		protected Bitmap doInBackground(Integer... params) {
			bitmap = BitmapFactory.decodeFile(path);
			return bitmap;
		}
		@Override
		protected void onPostExecute(Bitmap result) {
			if(imageViewReference != null && bitmap != null){
				final ImageView imageView = imageViewReference.get();
				if(imageView != null){
					imageView.setImageBitmap(bitmap);
				}
			}
		}
	}

}
