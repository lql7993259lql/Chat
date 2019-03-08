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
 * 相册选择照片界面
 * @author Wangda
 *
 */
public class AlbumActivity extends Activity {
	private ImageView ivPhoto;
	private ImageView ivSendBack;
	private Bitmap bitmap;
	private Button sendPhoto;
	private String path;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		try {
			setContentView(R.layout.chat_activity_album);
			initViews();
			setListeners();
			Intent intent = getIntent();
			path = intent.getStringExtra("path");
			BitmapWorkerTask bwt = new BitmapWorkerTask(ivPhoto);
			bwt.executeOnExecutor(Executors.newCachedThreadPool());
		} catch (Exception e) {
		}
	}
	
	/**
	 * 监听器
	 */
	private void setListeners() {
		ivSendBack.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		sendPhoto.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setAction(CameraActivity.SEND_PHOTO);
				intent.putExtra("b", path);
				AlbumActivity.this.sendBroadcast(intent);
				finish();
			}
		});
		
	}

	/**
	 * 初始化
	 */
	private void initViews() {
		ivPhoto = (ImageView) findViewById(R.id.iv_send_ptoto1);
		ivSendBack = (ImageView) findViewById(R.id.iv_send_photo_back1);
		sendPhoto = (Button) findViewById(R.id.bt_send_photo1);
	}
	
	/**
	 * 异步加载并设置图片
	 * @author wangda
	 *
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
