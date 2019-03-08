package com.jixiang.chat.entity;
/**
 * 表情的json文件 实体类
 * @author Wangda
 */
public class Emoji {
	private String text;
	private String file;
	
	public Emoji() {
		super();
	}
	public Emoji(String text, String file) {
		super();
		this.text = text;
		this.file = file;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public String getFile() {
		return file;
	}
	public void setFile(String file) {
		this.file = file;
	}
	
	
}
