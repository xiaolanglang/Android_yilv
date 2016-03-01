package com.yilvtzj.pojo;

import java.io.Serializable;

public class DongtaiComment implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Account account;
	private String time;
	private String id;
	private String content;

	public Account getAccount() {
		return account;
	}

	public void setAccount(Account account) {
		this.account = account;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

}
