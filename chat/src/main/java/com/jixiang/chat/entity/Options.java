package com.jixiang.chat.entity;
/**
 * 机器人的opstion实体类
 * @author Wangda
 *
 */
public class Options {
	private String text;

	private String body;

	private int msgid;

	public void setText(String text) {
		this.text = text;
	}

	public String getText() {
		return this.text;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public String getBody() {
		return this.body;
	}

	public void setMsgid(int msgid) {
		this.msgid = msgid;
	}

	public int getMsgid() {
		return this.msgid;
	}
}
