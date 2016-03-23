package com.excilys.cdb.pagination;

import java.util.ArrayList;
import java.util.List;

import com.excilys.cdb.persistence.mapper.Mapper;
import com.excilys.cdb.service.Order;
import com.excilys.cdb.service.Service;
import com.excilys.cdb.service.dto.DTO;

/**
 * Generic class for pagination implementation
 * @param <T> the object type we want to paginate
 */
public abstract class Pagination<T> {

    /**
     * The total number of elements
     */
	private int count;

    /**
     * The maximum number of elements per page
     */
	private int limit;

    /**
     * The last page number
     */
    private int lastPage;

    /**
     * The current page
     */
    private int currentPage;

    /**
     * True if the result has to been computed again (if the current page or the limit has changed)
     */
    protected boolean changed;

    /**
     * The list containing the result of the pagination
     */
    private List<DTO> listFromPage;
    
    /**
     * The filter for the list result
     */
    private String filter;
    
    /**
     * The order filter
     */
    private Order order;

    /**
     * The service that uses the pagination
     */
    protected Service<T> service;

    /**
     * The mapper to get only relevant data from the service
     */
    protected Mapper<T> mapper;

    /**
     *
     * @param count the total number of elements
     * @param limit the maximum number of elements per page
     */
	Pagination(int count, int limit) {
		this.count = count;
		filter = "";
		setLimit(limit);
	}

	public int getCount() {
		return count;
	}

	public int getLimit() {
		return limit;
	}

	public int getLastPage() {
		return lastPage;
	}

	public int getCurrentPage() {
		return currentPage;
	}

    /**
     * This will recalculate the number of pages a
     * @param count the new count of elements
     */
    public void setCount(int count) {
        if(count !=  this.count) {
            this.count = count;
            boolean wasLastPage = currentPage == lastPage;
            lastPage = (int) Math.ceil(count / limit);
            lastPage += count % limit == 0 ? 0 : 1;
            if(currentPage > lastPage || wasLastPage) {
                setCurrentPage(lastPage);
            }
            changed = true;
        }
    }

    /**
     * Change the current page only if 1 <= currentPage <= the last page
     * @param currentPage the new page desired.
     */
	public void setCurrentPage(int currentPage) {
		if(currentPage >= 1 && currentPage <= lastPage && this.currentPage != currentPage) {
			this.currentPage = currentPage;
			changed = true;
		}
	}
	
	/**
	 * Set a new filter for the list result
	 * @param search the new filter for the research
	 */
	public void setFilter(String filter) {
		this.filter = filter;
		changed = true;
	}
	
	/**
	 * Set a new order for the list result
	 * @param order the new filter for the order
	 */
	public void setOrder(Order order) {
		this.order = order;
		changed = true;
	}

    /**
     * Change the limit of the pagination. It must be a positive integer
     * It will update the total number of pages and set the current page to the first one
     * @param limit the new number of elements per page
     */
	public void setLimit(int limit) {
		if(limit <= 0) {
			throw new IllegalArgumentException("limit should be positive");
		}
		if(limit == this.limit)
			return;
		this.limit = limit;
		lastPage = (int) Math.ceil(count / limit);
		lastPage += count % limit == 0 ? 0 : 1;
		currentPage = 1;
		changed = true;
	}

	/**
	 * Jump to the next page
	 */
	public void next() {
		if(currentPage < lastPage) {
			currentPage += 1;
			changed = true;
		}
	}

	/**
	 * Jump to the previous page
	 */
	public void previous() {
		if(currentPage > 1) {
			currentPage -= 1;
			changed = true;
		}
	}

	/**
	 * Return the result list based on the pagination parameters if they've changed
	 * @return the list containing all the elements of the current page
     */
	public List<DTO> getListFromPage() {
		if(!changed) {
			return listFromPage;
		}
		int offset = (getCurrentPage() - 1) * getLimit();
		int limit = getLimit();
        List<DTO> listDTO = new ArrayList<>();
        List<T> serviceList;
        if(order == null) {
        	order = Order.NOP;
        }
        if(filter.trim().isEmpty()) {
        	serviceList = service.listPage(offset, limit, order);
        }
        else {
        	serviceList = service.listPage(offset, limit, filter, order);
        }
        for(T model : serviceList) {
            listDTO.add(mapper.getFromModel(model));
        }
		listFromPage = listDTO;
		changed = false;
		return listFromPage;
	}
	
	/**
	 * Invalidate the cache to get a fresh list from the database
	 */
	public void invalidateCache() {
		changed = true;
	}
}