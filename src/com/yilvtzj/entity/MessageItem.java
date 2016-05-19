package com.yilvtzj.entity;

public class MessageItem {

	private String id;
	private String fromWho;// 消息来自
	private long time;// 消息日期
	private String message;// 消息内容
	private int headImg;
	private boolean isComMeg = true;// 是否为收到的消息
	private int isNew;

	public MessageItem() {
	}

	public MessageItem(String fromWho, long date, String message, int headImg, boolean isComMeg, int isNew) {
		this.fromWho = fromWho;
		this.time = date;
		this.message = message;
		this.headImg = headImg;
		this.isComMeg = isComMeg;
		this.isNew = isNew;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public long getTime() {
		return time;
	}

	public void setTime(long time) {
		this.time = time;
	}

	public String getFromWho() {
		return fromWho;
	}

	public void setFromWho(String fromWho) {
		this.fromWho = fromWho;
	}

	public long getDate() {
		return time;
	}

	public void setDate(long date) {
		this.time = date;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public int getHeadImg() {
		return headImg;
	}

	public void setHeadImg(int headImg) {
		this.headImg = headImg;
	}

	public boolean isComMeg() {
		return isComMeg;
	}

	public void setComMeg(boolean isComMeg) {
		this.isComMeg = isComMeg;
	}

	public int getIsNew() {
		return isNew;
	}

	public void setIsNew(int isNew) {
		this.isNew = isNew;
	}

}
