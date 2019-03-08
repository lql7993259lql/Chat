package com.jixiang.chat.ui;

import java.io.File;

import com.jixiang.chat.video.listener.ClickListener;
import com.jixiang.chat.video.listener.ErrorListener;
import com.jixiang.chat.video.listener.JCameraListener;
import com.jixiang.chat.R;
import com.jixiang.chat.util.ImageUtil;
import com.jixiang.chat.util.ToastControll;
import com.jixiang.chat.video.JCameraView;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;

public class VideoActivity extends Activity {
	private JCameraView jCameraView;
	private int type = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.chat_activity_video);
		init();
	}
	
	/**
	 * 摄像初始化
	 */
	private void init() {
		try {
			// 1.1.1
			jCameraView = (JCameraView) findViewById(R.id.jcameraview);

			// 设置视频保存路径
			jCameraView
					.setSaveVideoPath(Environment.getExternalStorageDirectory().getPath() + File.separator + "JCamera");

			// 设置只能录像或只能拍照或两种都可以（默认两种都可以）
			jCameraView.setFeatures(JCameraView.BUTTON_STATE_BOTH);

			// 设置视频质量
			jCameraView.setMediaQuality(JCameraView.MEDIA_QUALITY_MIDDLE);

			// JCameraView 监听
			jCameraView.setErrorLisenter(new ErrorListener() {

				@Override
				public void onError() {
					// 打开 Camera 失败回调
					ToastControll.showToast("打开相机失败", VideoActivity.this);
					finish();
				}

				@Override
				public void AudioPermissionError() {
					// 打开 Camera 失败回调
					ToastControll.showToast("打开相机失败", VideoActivity.this);
					finish();
				}
			});

			jCameraView.setJCameraLisenter(new JCameraListener() {
				@Override
				public void captureSuccess(Bitmap bitmap) {
					// 获取图片 bitmap
					type = 0;
					MainActivity.photoPath = ImageUtil.saveImageToGallery(VideoActivity.this, bitmap);
					MainActivity.photo = ImageUtil.getImage(MainActivity.photoPath, MainActivity.max);
					Intent intent = new Intent();
					intent.setAction(CameraActivity.SEND_PHOTO);
					intent.putExtra("b", MainActivity.photoPath);
					intent.putExtra("type", type);
					VideoActivity.this.sendBroadcast(intent);
					finish();
				}

				@Override
				public void recordSuccess(String url, Bitmap firstFrame) {
					// 获取视频路径
					type = 1;
					MainActivity.photoPath = ImageUtil.saveImageToGallery(VideoActivity.this, firstFrame);
					MainActivity.photo = ImageUtil.mergeBitmap(
							ImageUtil.getImage(MainActivity.photoPath, MainActivity.max),
							BitmapFactory.decodeResource(getResources(), R.drawable.chat_video_img));
					Intent intent = new Intent();
					intent.setAction(CameraActivity.SEND_PHOTO);
					intent.putExtra("b", MainActivity.photoPath);
					intent.putExtra("type", type);
					intent.putExtra("videoPath", url);
					VideoActivity.this.sendBroadcast(intent);
					finish();
				}
			});
			// 左边按钮点击事件
			jCameraView.setLeftClickListener(new ClickListener() {
				@Override
				public void onClick() {
					finish();
				}
			});
			// 右边按钮点击事件
			jCameraView.setRightClickListener(new ClickListener() {
				@Override
				public void onClick() {
				}
			});
		} catch (Exception e) {
		}

	}

	@SuppressLint("NewApi")
	@Override
	protected void onStart() {
		super.onStart();
		if (Build.VERSION.SDK_INT >= 19) {
			View decorView = getWindow().getDecorView();
			decorView.setSystemUiVisibility(
					View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
							| View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
							| View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
		} else {
			View decorView = getWindow().getDecorView();
			int option = View.SYSTEM_UI_FLAG_FULLSCREEN;
			decorView.setSystemUiVisibility(option);
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		jCameraView.onResume();
	}

	@Override
	protected void onPause() {
		super.onPause();
		jCameraView.onPause();
	}
}
