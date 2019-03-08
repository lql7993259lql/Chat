package com.jixiang.chat.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.Executors;

import com.jixiang.chat.ui.MainActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;

public class DownLoad {
	/**
	 * 下载客服头像
	 * 
	 * @param path
	 *            头像url地址
	 * @param handler
	 */
	public static void downLoadServiceBitmap(final String path, final Handler handler) {
		if(MainActivity.stopThread){
			AsyncTask<Void, Void, Void> task = new AsyncTask<Void, Void, Void>() {
				@Override
				protected Void doInBackground(Void... params) {
					try {
						if (path != null) {
							URL url = new URL(path);
							HttpURLConnection connection = (HttpURLConnection) url.openConnection();
							connection.setRequestMethod("GET");
							connection.setDoInput(true);
							InputStream is = connection.getInputStream();
							Bitmap bitmap = BitmapFactory.decodeStream(is);
							is.close();
							Message msg = handler.obtainMessage();
							msg.what = MainActivity.SERVERAVATARSUCCESS;
							msg.obj = bitmap;
							handler.sendMessage(msg);
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
					return null;
				}
				
				@Override
				protected void onPostExecute(Void result) {
					super.onPostExecute(result);
				}
			};
			task.executeOnExecutor(Executors.newCachedThreadPool());
		}
	}

	/**
	 * 下载用户头像
	 * 
	 * @param path
	 *            头像url地址
	 * @param handler
	 */
	public static void downLoadAvatarBitmap(final String path, final Handler handler) {
		if(MainActivity.stopThread){
			AsyncTask<Void, Void, Void> task = new AsyncTask<Void, Void, Void>() {
				@Override
				protected Void doInBackground(Void... params) {
					try {
						if (path != null) {
							URL url = new URL(path);
							HttpURLConnection connection = (HttpURLConnection) url.openConnection();
							connection.setRequestMethod("GET");
							connection.setDoInput(true);
							InputStream is = connection.getInputStream();
							Bitmap bitmap = BitmapFactory.decodeStream(is);
							is.close();
							Message msg = handler.obtainMessage();
							msg.what = MainActivity.AVATARSUCCESS;
							msg.obj = bitmap;
							handler.sendMessage(msg);
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
					return null;
				}
				
				@Override
				protected void onPostExecute(Void result) {
					super.onPostExecute(result);
				}
			};
			task.executeOnExecutor(Executors.newCachedThreadPool());
		}
	}

	/**
	 * 下载历史消息客服头像
	 * 
	 * @param path
	 *            头像url地址
	 * @param handler
	 */
	public static void downLoadHistoryServiceBitmap(final String path, final Handler handler, final int msgid) {
		new Thread() {
			public void run() {
				if(MainActivity.stopThread){
					try {
						if (path != null) {
							URL url = new URL(path);
							HttpURLConnection connection = (HttpURLConnection) url.openConnection();
							connection.setRequestMethod("GET");
							connection.setDoInput(true);
							InputStream is = connection.getInputStream();
							Bitmap bitmap = BitmapFactory.decodeStream(is);
							is.close();
							Message msg = handler.obtainMessage();
							msg.what = MainActivity.HISTORYSERVICEBITMAP;
							msg.obj = bitmap;
							msg.arg1 = msgid;
							handler.sendMessage(msg);
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}.start();
	}

	/**
	 * 下载图片消息
	 * 
	 * @param path
	 *            图片url地址
	 * @param handler
	 * @param lmsgid
	 *            第几条消息
	 */
	public static void downLoadBitmap(final String path, final Handler handler, final int lmsgid) {
		new Thread() {
			public void run() {
				if(MainActivity.stopThread){
					try {
						if (path != null) {
							URL url = new URL(path);
							HttpURLConnection connection = (HttpURLConnection) url.openConnection();
							connection.setRequestMethod("GET");
							connection.setDoInput(true);
							InputStream is = connection.getInputStream();
							byte[] bs = new byte[1024];
							int len;
							String name = path.substring(path.lastIndexOf("/"));
							String filePath = Environment.getExternalStorageDirectory() + "/ldm_pic";
							File file = new File(filePath);
							if (!file.exists()) {
								file.mkdirs();
							}
							File file1 = new File(filePath + name);
							file1.createNewFile();
							OutputStream os = new FileOutputStream(file1);
							while ((len = is.read(bs)) != -1) {
								os.write(bs, 0, len);
							}
							os.close();
							is.close();
							Message msg = handler.obtainMessage();
							msg.what = MainActivity.DOWNLOADPIC;
							msg.obj = filePath + name;
							msg.arg1 = lmsgid;
							handler.sendMessage(msg);
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}.start();
	}

	/**
	 * 下载语音消息
	 * 
	 * @param path
	 *            图片url地址
	 * @param handler
	 * @param lmsgid
	 *            第几条消息
	 */
	public static void downLoadVoice(final String path, final Handler handler, final int lmsgid) {
		new Thread() {
			public void run() {
				if(MainActivity.stopThread){
					try {
						if (path != null) {
							URL url = new URL(path);
							HttpURLConnection connection = (HttpURLConnection) url.openConnection();
							connection.setRequestMethod("GET");
							connection.setDoInput(true);
							InputStream is = connection.getInputStream();
							byte[] bs = new byte[1024];
							int len;
							String name = path.substring(path.lastIndexOf("/"));
							String filePath = Environment.getExternalStorageDirectory() + "/ldm_voice";
							File file = new File(filePath);
							if (!file.exists()) {
								file.mkdirs();
							}
							File file1 = new File(filePath + name);
							file1.createNewFile();
							OutputStream os = new FileOutputStream(file1);
							while ((len = is.read(bs)) != -1) {
								os.write(bs, 0, len);
							}
							os.close();
							is.close();
							Message msg = handler.obtainMessage();
							msg.what = MainActivity.DOWNLOADVOICE;
							msg.obj = filePath + name;
							msg.arg1 = lmsgid;
							handler.sendMessage(msg);
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}.start();
	}
	
	/**
	 * 下载视频消息
	 * 
	 * @param path
	 *            视频url地址
	 * @param handler
	 * @param lmsgid
	 *            第几条消息
	 */
	public static void downLoadVideo(final String path, final Handler handler, final int lmsgid) {
		new Thread() {
			public void run() {
				if(MainActivity.stopThread){
					try {
						if (path != null) {
							URL url = new URL(path);
							HttpURLConnection connection = (HttpURLConnection) url.openConnection();
							connection.setRequestMethod("GET");
							connection.setDoInput(true);
							InputStream is = connection.getInputStream();
							byte[] bs = new byte[1024];
							int len;
							String name = path.substring(path.lastIndexOf("/"));
							String filePath = Environment.getExternalStorageDirectory() + "/ldm_video";
							File file = new File(filePath);
							if (!file.exists()) {
								file.mkdirs();
							}
							File file1 = new File(filePath + name);
							file1.createNewFile();
							OutputStream os = new FileOutputStream(file1);
							while ((len = is.read(bs)) != -1) {
								os.write(bs, 0, len);
							}
							os.close();
							is.close();
							Message msg = handler.obtainMessage();
							msg.what = MainActivity.DOWNLOADVIDEO;
							msg.obj = filePath + name;
							msg.arg1 = lmsgid;
							handler.sendMessage(msg);
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}.start();
	}

}
