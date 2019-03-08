package com.jixiang.chat.util;

import java.io.File;
import java.io.FileInputStream;
/**
 * 读取本地json文件
 * @author Wangda
 */
public class ReadJsonFile {
	public static String readJson(String fileName){
		String resultString = "";
		try {
			File file = new File(fileName);
			FileInputStream fis = new FileInputStream(file);
			byte[] buffer = new byte[fis.available()];
			int len = fis.read(buffer);
			if(len != -1){
				resultString = new String(buffer,"UTF-8");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return resultString;
	}
}
