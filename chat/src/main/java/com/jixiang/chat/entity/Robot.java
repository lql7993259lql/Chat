package com.jixiang.chat.entity;

import java.util.List;
/**
 * 机器人list中的对象 实体类
 * @author Wangda
 */
public class Robot {
	private String body;

	private List<Options> options;

	private int msgid;

	private boolean isOptions;

	private int isList;
	
	private int sort;
	
	public int getSort() {
		return sort;
	}

	public void setSort(int sort) {
		this.sort = sort;
	}

	public int getIsList() {
		return isList;
	}

	public void setIsList(int isList) {
		this.isList = isList;
	}

	public boolean isOptions() {
		return isOptions;
	}

	public void setOptions(boolean isOptions) {
		this.isOptions = isOptions;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public String getBody() {
		return this.body;
	}

	public void setOptions(List<Options> options) {
		this.options = options;
	}

	public List<Options> getOptions() {
		return this.options;
	}

	public void setMsgid(int msgid) {
		this.msgid = msgid;
	}

	public int getMsgid() {
		return this.msgid;
	}
}
