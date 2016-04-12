package com.excilys.cdb.pagination;

import com.excilys.cdb.persistence.mapper.Mapper;
import com.excilys.cdb.service.Query;
import com.excilys.cdb.service.Service;
import com.excilys.cdb.service.dto.DTO;

import java.util.ArrayList;
import java.util.List;

/**
 * Generic class for pagination implementation
 * @param <T> the object type we want to paginate
 */
public abstract class AbstractPage<T> {

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
     * The filter to apply to the list result
     */
    private String filter;

    /**
     * The order to apply to the list result
     */
    private String order;

    /**
     * The list containing the result of the pagination
     */
    private List<DTO> elements;

    /**
     * The page query
     */
    private Query query;

    private Service<T> service;
    protected Mapper<T> mapper;

    public AbstractPage() {
    }

    protected AbstractPage(Service<T> service, Query query, int currentPage) {
    	this.service = service;
        this.query = query;
        this.currentPage = currentPage;
        this.limit = query.getLimit();
        this.totalCount = service.count(query);
        lastPage = (int) Math.ceil(totalCount / limit);
        lastPage += totalCount % limit == 0 ? 0 : 1;
        this.filter = query.getFilter();
        if (query.getOrder() != null) {
            this.order = query.getOrder().name().toLowerCase();
        }
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public int getLastPage() {
        return lastPage;
    }

    public void setLastPage(int lastPage) {
        this.lastPage = lastPage;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public String getFilter() {
        return filter;
    }

    public void setFilter(String filter) {
        this.filter = filter;
    }

    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        this.order = order;
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
     * Return the result list based on the query parameters
     * @return the list containing all the elements of the current page
     */
    public List<DTO> getElements() {
        int offset = (currentPage - 1) * limit;
        List<DTO> listDTO = new ArrayList<>();
        Query.Builder builder = new Query.Builder();
        Query query = builder.offset(offset).limit(limit).filter(filter).order(this.query.getOrder()).build();
        List<T> list = service.list(query);
        for(T model : list) {
            listDTO.add(mapper.getFromModel(model));
        }
        elements = listDTO;
        return elements;
    }
}