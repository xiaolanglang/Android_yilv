package com.yilvtzj.entity;

import java.util.List;

public class DataResult<E> extends Result {

	private List<E> list;

	private int pageNum;

	private int pages;

	public void setList(List<E> list) {
		this.list = list;
	}

	public void setPageNum(int pageNum) {
		this.pageNum = pageNum;
	}

	public void setPages(int pages) {
		this.pages = pages;
	}

	public List<E> getList() {
		return list;
	}

	public int getPageNum() {
		return pageNum;
	}

	public int getPages() {
		return pages;
	}

}
