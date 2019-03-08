package com.jixiang.chat.ui;

import com.jixiang.chat.R;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.VideoView;

public class PlayVideoActivity extends Activity {

	private ImageView ivPlay;
	private VideoView videoView;
	private ImageView ivBack;
	private String videoPath;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.chat_activity_play_video);
		ivPlay = (ImageView) findViewById(R.id.iv_all);
		ivBack = (ImageView) findViewById(R.id.iv_photo_back);
		videoView = (VideoView) findViewById(R.id.videoView);
		Intent intent = getIntent();
		videoPath = intent.getStringExtra("a");
		playVideo();
		setListeners();
	}
	
	/**
	 * 播放视频
	 */
	private void playVideo() {
		try {
			// 设置视频控制器
			videoView.setMediaController(new MediaController(this));
			
			// 播放完成回调
			videoView.setOnCompletionListener(new OnCompletionListener() {
				
				@Override
				public void onCompletion(MediaPlayer mp) {
					ivPlay.setVisibility(View.VISIBLE);
				}
			});
			
			// 设置视频路径
			videoView.setVideoPath(videoPath);
			
			// 开始播放视频
			videoView.start();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 控件点击监听
	 */
	private void setListeners() {
		ivBack.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		ivPlay.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				try {
					ivPlay.setVisibility(View.GONE);
					videoView.start();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	
	@Override
	protected void attachBaseContext(Context newBase) {
		super.attachBaseContext(new ContextWrapper(newBase) {
			@Override
			public Object getSystemService(String name) {
				if (Context.AUDIO_SERVICE.equals(name))
					return getApplicationContext().getSystemService(name);
				return super.getSystemService(name);
			}
		});
	}
}
