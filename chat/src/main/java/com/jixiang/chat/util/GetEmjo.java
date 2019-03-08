package com.jixiang.chat.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import android.os.Handler;

public class GetEmjo {
	public static final int ZIPSUCCESS = 1;

	/**
	 * 下载表情zip包
	 * @param load 下载到的路径
	 * @param zip	解压到的路径
	 * @param path 下载表情的URL地址
	 * @throws Exception
	 */
	public static void LoadList(String load, String zip, Handler handler, String path) throws Exception {
		File file = new File(load);
		if (file.exists()) {
			return;
		}
		if (!file.getParentFile().exists()) {
			file.getParentFile().mkdirs();
		}
		URL url = new URL(path);
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		connection.setRequestMethod("GET");
		InputStream is = connection.getInputStream();
		FileOutputStream fos = new FileOutputStream(file);
		byte[] buffer = new byte[1024 * 100];
		int length = 0;
		@SuppressWarnings("unused")
		int current = 0;
		while ((length = is.read(buffer)) != -1) {
			fos.write(buffer, 0, length);
			fos.flush();
			current += length;
		}
		fos.close();
		UnZipFloader(load, zip, handler);
	}

	/**
	 * 解压文件
	 * 
	 * @param zipFileString
	 *            需要解压的文件路径
	 * @param outPathString
	 *            文件解压到的路径
	 * @param handler
	 * @throws Exception
	 */
	public static void UnZipFloader(String zipFileString, String outPathString, Handler handler) throws Exception {
		ZipInputStream inZip = new ZipInputStream(new FileInputStream(zipFileString));
		ZipEntry zipEntry;
		String szName = "";
		while ((zipEntry = inZip.getNextEntry()) != null) {
			szName = zipEntry.getName();
			if (zipEntry.isDirectory()) {
				szName = szName.substring(0, szName.length() - 1);
				File folder = new File(outPathString + File.separator + szName);
				folder.mkdirs();
			} else {
				File f = new File(outPathString);
				if (!f.exists()) {
					f.mkdir();
				}
				File file = new File(outPathString + File.separator + szName);
				file.createNewFile();
				FileOutputStream out = new FileOutputStream(file);
				int len;
				byte[] buffer = new byte[1024 * 4];
				while ((len = inZip.read(buffer)) != -1) {
					out.write(buffer, 0, len);
					out.flush();
				}
				out.close();
			}
		}
		inZip.close();
		handler.sendEmptyMessage(ZIPSUCCESS);
	}

}
