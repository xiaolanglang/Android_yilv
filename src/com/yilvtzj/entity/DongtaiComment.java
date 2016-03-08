package com.yilvtzj.entity;

import java.io.Serializable;

public class DongtaiComment implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Account user;
	private String createDate;
	private String id;
	private String content;

	public Account getUser() {
		return user;
	}

	public void setUser(Account user) {
		this.user = user;
	}

	public String getCreateDate() {
		return createDate;
	}

	public void setCreateDate(String createDate) {
		this.createDate = createDate;
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
