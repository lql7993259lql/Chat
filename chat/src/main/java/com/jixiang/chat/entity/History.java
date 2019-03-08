package com.jixiang.chat.entity;

import android.graphics.Bitmap;

/**
 * 历史消息实体类(下拉刷新的消息)
 * @author Wangda
 */
public class History {
	private int id;
	private String sid;
	private String dir;
	private String read;
	private String msgid;
	private String sendtime;
	private String readtime;
	private String body;
	private String avatar;
	private String nickname;
	private Bitmap bitmap;

	public History() {
		super();
	}

	public History(int id, String sid, String dir, String read, String msgid, String sendtime, String readtime,
			String body, String avatar) {
		super();
		this.id = id;
		this.sid = sid;
		this.dir = dir;
		this.read = read;
		this.msgid = msgid;
		this.sendtime = sendtime;
		this.readtime = readtime;
		this.body = body;
		this.avatar = avatar;
	}
	
	
	public Bitmap getBitmap() {
		return bitmap;
	}

	public void setBitmap(Bitmap bitmap) {
		this.bitmap = bitmap;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getId() {
		return id;
	}

	public void setSid(String sid) {
		this.sid = sid;
	}

	public String getSid() {
		return sid;
	}

	public void setDir(String dir) {
		this.dir = dir;
	}

	public String getDir() {
		return dir;
	}

	public void setRead(String read) {
		this.read = read;
	}

	public String getRead() {
		return read;
	}

	public void setMsgid(String msgid) {
		this.msgid = msgid;
	}

	public String getMsgid() {
		return msgid;
	}

	public void setSendtime(String sendtime) {
		this.sendtime = sendtime;
	}

	public String getSendtime() {
		return sendtime;
	}

	public void setReadtime(String readtime) {
		this.readtime = readtime;
	}

	public String getReadtime() {
		return readtime;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public String getBody() {
		return body;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

	public String getAvatar() {
		return avatar;
	}
}
