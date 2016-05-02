package com.excilys.cdb.pagination.util;

import com.excilys.cdb.util.Constraint;

/**
 * Page request contains the information about the pagination
 * The query is the object containing the query constraints
 * The current page is the wanted page
 */
public class PageRequest {
	
	private Constraint query;
	private int currentPage;
	
	public PageRequest(Constraint query, int currentPage) {
    	if (query == null) {
    		query = new Constraint.Builder().limit(10).build();
    	}
    	if (query.getLimit() <= 0) {
    		query.setLimit(10);
    	}
		this.query = query;
		this.currentPage = currentPage;
	}
	
	public void setQuery(Constraint query) {
		this.query = query;
	}

	public void setCurrentPage(int currentPage) {
		this.currentPage = currentPage;
	}

	public Constraint getQuery() {
		return query;
	}

	public int getCurrentPage() {
		return currentPage;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + currentPage;
		result = prime * result + ((query == null) ? 0 : query.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		PageRequest other = (PageRequest) obj;
		if (currentPage != other.currentPage)
			return false;
		if (query == null) {
			if (other.query != null)
				return false;
		} else if (!query.equals(other.query))
			return false;
		return true;
	}
}