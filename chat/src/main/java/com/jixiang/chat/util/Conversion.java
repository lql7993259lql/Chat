package com.jixiang.chat.util;
/**
 * 符号转换
 * @author Wangda
 */
public class Conversion {
	
	public static String ToServer(String str){
		String s = str.replace("/", "-");
		String result = s.replace("+", ",");
		return result;
	}
	
	public static String FromServer(String str){
		String s = str.replace("-", "/");
		String result = s.replace(",", "+");
		return result;
	}
}
