package com.jixiang.chat.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import com.jixiang.chat.R;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;

@SuppressLint("NewApi")
public class ImageUtil {

	// 对bitmap进行质量压缩
	public static Bitmap compressImage(Bitmap image) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		image.compress(Bitmap.CompressFormat.JPEG, 100, baos);// 质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
		int options = 100;
		while (baos.toByteArray().length / 1024 > 100) { // 循环判断如果压缩后图片是否大于100kb,大于继续压缩
			baos.reset();// 重置baos即清空baos
			options -= 10;// 每次都减少10
			image.compress(Bitmap.CompressFormat.JPEG, options, baos);// 这里压缩options%，把压缩后的数据存放到baos中
		}
		ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());// 把压缩后的数据baos存放到ByteArrayInputStream中
		Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);// 把ByteArrayInputStream数据生成图片
		return bitmap;
	}

	// 传入图片路径，返回压缩后的bitmap
	public static Bitmap getImage(String srcPath, int max) {
		if (TextUtils.isEmpty(srcPath)) // 如果图片路径为空 直接返回
			return null;
		BitmapFactory.Options newOpts = new BitmapFactory.Options();
		// 开始读入图片，此时把options.inJustDecodeBounds 设回true了
		newOpts.inJustDecodeBounds = true;
		Bitmap bitmap = BitmapFactory.decodeFile(srcPath, newOpts);// 此时返回bm为空
		newOpts.inJustDecodeBounds = false;
		int w = newOpts.outWidth;
		int h = newOpts.outHeight;
		// 缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
		int be = 1;
		if (w > h) {
			if (w > max) {
				be = (int) Math.ceil((float) w / (float) max);
			} else {
				be = 1;
			}
		}
		if (w < h) {
			if (h > max) {
				be = (int) Math.ceil((float) h / (float) max);
			} else {
				be = 1;
			}
		}
		if (w == h) {
			if (w > max) {
				be = (int) Math.ceil((float) w / (float) max);
			} else {
				be = 1;
			}
		}
		if (be > 2) {
			if (be % 2 == 1) {
				if (w > h) {
					if (w / (be - 1) > max) {
						be = be + 1;
					}
				} else {
					if (h / (be - 1) > max) {
						be = be + 1;
					}
				}
			}
		}
		newOpts.inSampleSize = be;// 设置缩放比例
		// 重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
		bitmap = BitmapFactory.decodeFile(srcPath, newOpts);
		return bitmap;
	}

	/**
	 * 处理图片
	 * 
	 * @param bm
	 *            所要转换的bitmap
	 * @param newWidth新的宽
	 * @param newHeight新的高
	 * @return 指定宽高的bitmap
	 */
	public static Bitmap zoomImg(Bitmap bm, int newWidth, int newHeight) {
		// 获得图片的宽高
		int width = bm.getWidth();
		int height = bm.getHeight();
		// 计算缩放比例
		float scaleWidth = ((float) newWidth) / width;
		float scaleHeight = ((float) newHeight) / height;
		// 取得想要缩放的matrix参数
		Matrix matrix = new Matrix();
		matrix.postScale(scaleWidth, scaleHeight);
		// 得到新的图片
		Bitmap newbm = Bitmap.createBitmap(bm, 0, 0, width, height, matrix, true);
		return newbm;
	}
	
	/**
	 * 将图片保存至手机相册
	 * 
	 * @param context
	 * @param bmp
	 *            需要保存的图片
	 */
	public static String saveImageToGallery(Context context, Bitmap bmp) {
		String name = null;
		String fileName = null;
		try {
			name = Environment.getExternalStorageDirectory() + "/WLGame";
			// 首先保存图片
			File appDir = new File(name);
			if (!appDir.exists()) {
				appDir.mkdir();
			}
			fileName = System.currentTimeMillis() + "image.jpg";
			File file = new File(appDir, fileName);
			try {
				FileOutputStream fos = new FileOutputStream(file);
				bmp.compress(Bitmap.CompressFormat.JPEG, 80, fos);
				fos.flush();
				fos.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}

			/*// 其次把文件插入到系统图库
			try {
				MediaStore.Images.Media.insertImage(context.getContentResolver(), file.getAbsolutePath(), fileName,
						null);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
			// 最后通知图库更新
			context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,
					Uri.parse("file://" + Environment.getExternalStorageDirectory())));*/
		} catch (Exception e) {
			e.printStackTrace();
		}
		return name + "/" + fileName;
	}
	
	/**
	 * 获取第一帧图片
	 * @param path 本地视频路径
	 * @return
	 */
	public static Bitmap getFirstBitmap(String path) {
		MediaMetadataRetriever mm = new MediaMetadataRetriever();
		mm.setDataSource(path);
		return mm.getFrameAtTime();
	}
	
	/**
	 * 将两张图片合成一张
	 * @param firstBitmap 第一张图片
	 * @param secondBitmap 第二张图片
	 * @return
	 */
	public static Bitmap mergeBitmap(Bitmap firstBitmap, Bitmap secondBitmap) {
		Bitmap bitmap = Bitmap.createBitmap(firstBitmap.getWidth(), firstBitmap.getHeight(), firstBitmap.getConfig());
		Canvas canvas = new Canvas(bitmap);
		canvas.drawBitmap(firstBitmap, new Matrix(), null);
		canvas.drawBitmap(secondBitmap, firstBitmap.getWidth() / 2 - secondBitmap.getWidth() / 2,
				firstBitmap.getHeight() / 2 - secondBitmap.getHeight() / 2, null);
		return bitmap;
	}
}
