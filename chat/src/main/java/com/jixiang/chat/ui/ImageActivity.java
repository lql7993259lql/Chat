package com.jixiang.chat.ui;

import java.lang.ref.WeakReference;
import java.util.concurrent.Executors;

import com.jixiang.chat.R;
import com.jixiang.chat.util.ImageUtil;
import com.jixiang.chat.util.ToastControll;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.ImageView;

/**
 * 查看图片界面
 * 
 * @author wangda
 */
public class ImageActivity extends Activity {
	private Bitmap bitmap;
	private ImageView ivImage;
	private ImageView ivBack;
	private String path;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		try {
			setContentView(R.layout.chat_activity_image);
			ivImage = (ImageView) findViewById(R.id.iv_imageView);
			ivBack = (ImageView) findViewById(R.id.iv_photo_back);
			Intent intent = getIntent();
			path = intent.getStringExtra("a");
			BitmapWorkerTask bwt = new BitmapWorkerTask(ivImage);
			bwt.executeOnExecutor(Executors.newCachedThreadPool());
			setListeners();
		} catch (Exception e) {
		}
	}

	private void setListeners() {
		ivBack.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		ivImage.setOnLongClickListener(new ImageDialog());
	}

	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		super.onWindowFocusChanged(hasFocus);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		finish();
		return super.onKeyDown(keyCode, event);
	}

	/**
	 * 异步加载并设置图片
	 * 
	 * @author wangda
	 *
	 */
	class BitmapWorkerTask extends AsyncTask<Integer, Void, Bitmap> {
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
			if (imageViewReference != null && bitmap != null) {
				final ImageView imageView = imageViewReference.get();
				if (imageView != null) {
					imageView.setImageBitmap(bitmap);
				}
			}
		}
	}

	/**
	 * 长按图片消息的dialog
	 * 
	 * @author Wangda
	 *
	 */
	public class ImageDialog implements OnLongClickListener {

		public ImageDialog() {
		}

		@Override
		public boolean onLongClick(View v) {
			AlertDialog dialog;
			final String[] str = new String[] { "保存到本地", "取消" };
			AlertDialog.Builder builder = new AlertDialog.Builder(ImageActivity.this, R.style.ChatAlertDialog);
			builder.setItems(str, new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					switch (which) {
					case 0:
						String name = ImageUtil.saveImageToGallery(ImageActivity.this, BitmapFactory.decodeFile(path));
						ToastControll.showToast("图片已保存至" + name, ImageActivity.this);
						break;
					case 1:
						break;
					}
				}
			});
			dialog = builder.create();
			dialog.setCanceledOnTouchOutside(true);
			dialog.show();
			return true;
		}

	}
}
