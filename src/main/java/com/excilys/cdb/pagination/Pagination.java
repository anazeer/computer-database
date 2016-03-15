package com.excilys.cdb.pagination;

import java.util.List;

import com.excilys.cdb.service.dto.DTO;

public abstract class Pagination<T extends DTO> {
	
	private int countEntries;
	private int countPerPage;
	private int countPages;
	private int currentPage;
	protected boolean changed;

	public Pagination(int countEntries, int countPerPage) {
		this.countEntries = countEntries;
		setCountPerPage(countPerPage);
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
	
	public void setCurrentPage(int currentPage) {
		if(currentPage >= 1 && currentPage <= countPages && this.currentPage != currentPage) {
			this.currentPage = currentPage;
			changed = true;
		}
	}
	
	public void setCountPerPage(int countPerPage) {
		if(countPerPage <= 0) {
			throw new IllegalArgumentException("countPerPage should be positive");
		}
		if(countPerPage == this.countPerPage)
			return;
		this.countPerPage = countPerPage;
		countPages = (int) Math.ceil(countEntries/countPerPage);
		countPages += countPages%10 == 0 ? 0 : 1;
		currentPage = 1;
		changed = true;
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
	
	public abstract List<T> getListFromOffset();
}
