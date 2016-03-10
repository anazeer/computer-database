package com.excilys.cdb.pagination;

import java.util.List;

public abstract class Pagination<T> {
	
	private int countEntries;
	private int countPerPage;
	private int countPages;
	private int currentPage;
	protected boolean changed;

	public Pagination(int countEntries, int countPerPage) {
		this.countEntries = countEntries;
		this.countPerPage = countPerPage;
		countPages = (int) Math.ceil(countEntries/countPerPage);
		currentPage = 1;
		changed = true;
	}

	public int getCountEntries() {
		return countEntries;
	}

	public int getCountPerPage() {
		return countPerPage;
	}
	
	public void getCountPerPage(int countPerPage) {
		this.countPerPage = countPerPage;
		changed = true;
	}

	public int getCountPages() {
		return countPages;
	}

	public int getCurrentPage() {
		return currentPage;
	}

	public void next() {
		if(currentPage < countPages) {
			currentPage += 1;
			changed = true;
		}
	}
	
	public void previous() {
		if(currentPage > 1) {
			currentPage -= 1;
			changed = true;
		}
	}
	
	public abstract List<T> listFromOffset();
}
