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
public abstract class Page<T> {

    /**
     * The total number of elements
     */
	private int totalCount;

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
     * The list containing the result of the pagination
     */
    private List<DTO> elements;
    
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
     * @param totalCount the total number of elements
     * @param limit the maximum number of elements per page
     */
	Page(int totalCount, int limit) {
		this.totalCount = totalCount;
		filter = "";
		setLimit(limit);
	}

	public int getTotalCount() {
		return totalCount;
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
     * This will recalculate the number of pages
     * @param totalCount the new total count of elements
     */
    public void setTotalCount(int totalCount) {
        if(totalCount !=  this.totalCount) {
            this.totalCount = totalCount;
            boolean wasLastPage = currentPage == lastPage;
            lastPage = (int) Math.ceil(totalCount / limit);
            lastPage += totalCount % limit == 0 ? 0 : 1;
            if(currentPage > lastPage || wasLastPage) {
                setCurrentPage(lastPage);
            }
        }
    }

    /**
     * Change the current page only if 1 <= currentPage <= the last page
     * @param currentPage the new page desired.
     */
	public void setCurrentPage(int currentPage) {
		if(currentPage >= 1 && currentPage <= lastPage && this.currentPage != currentPage) {
			this.currentPage = currentPage;
		}
	}
	
	/**
	 * Set a new filter for the list result
	 * @param filter the new filter for the research
	 */
	public void setFilter(String filter) {
		this.filter = filter;
		currentPage = 1;
	}
	
	/**
	 * Set a new order for the list result
	 * @param order the new filter for the order
	 */
	public void setOrder(Order order) {
		this.order = order;
	}

    /**
     * Change the limit of the pagination. It must be a positive integer
     * It will update the total number of pages and set the current page to the first one
     * @param limit the new number of elements per page
     */
	public void setLimit(int limit) {
		if(limit <= 0) {
			throw new IllegalArgumentException("Limit should be positive");
		}
		if(limit == this.limit)
			return;
		this.limit = limit;
		lastPage = (int) Math.ceil(totalCount / limit);
		lastPage += totalCount % limit == 0 ? 0 : 1;
		currentPage = 1;
	}

	/**
	 * Jump to the next page
	 */
	public void next() {
		if(currentPage < lastPage) {
			currentPage += 1;
		}
	}

	/**
	 * Jump to the previous page
	 */
	public void previous() {
		if(currentPage > 1) {
			currentPage -= 1;
		}
	}

	/**
	 * Return the result list based on the pagination parameters if they've changed
	 * @return the list containing all the elements of the current page
     */
	public List<DTO> getElements() {
		int offset = (getCurrentPage() - 1) * limit;
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
		elements = listDTO;
		return elements;
	}
}