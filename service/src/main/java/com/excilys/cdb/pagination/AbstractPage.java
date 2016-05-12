package com.excilys.cdb.pagination;

import java.util.List;
import java.util.stream.Collectors;

import com.excilys.cdb.dto.IDto;
import com.excilys.cdb.mapper.IMapper;
import com.excilys.cdb.pagination.util.PageRequest;
import com.excilys.cdb.util.Constraint;

/**
 * Generic class for pagination implementation
 * @param <T> the object type we want to paginate
 */
public abstract class AbstractPage<T> {
	
    /**
     * The current page
     */
    private int currentPage;

    /**
     * The total number of elements
     */
    private int totalCount;
    
    /**
     * The last page number
     */
    private int lastPage;

    /**
     * The maximum number of elements per page
     */
    private int limit;
    
    /**
     * The offset to apply to the result
     */
    private int offset;

    /**
     * The filter to apply to the list result
     */
    private String filter;

    /**
     * The order to apply to the list result
     */
    private String order;
    
    /**
     * The mapper to get DTO from the model
     */
    private IMapper<T> mapper;

    /**
     * The list containing the result of the pagination
     */
    protected List<IDto> elements;
    
    protected AbstractPage(IMapper<T> mapper, PageRequest pageRequest, int totalCount) {
    	this.mapper = mapper;
    	Constraint query = pageRequest.getQuery();
    	this.currentPage = pageRequest.getCurrentPage();
        this.totalCount = totalCount;
        limit = query.getLimit();
        filter = query.getFilter();
        
        // Increment the last page if the number of page is not a multiple of the limit for the rest
        lastPage = (int) Math.ceil(totalCount / limit);
        lastPage += totalCount % limit == 0 ? 0 : 1;
        
        if (query.getOrder() != null) {
            this.order = query.getOrder().name().toLowerCase();
        }
        
        // Compute the offset
        this.offset = (currentPage - 1) * limit;
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
    
    public void setElements(List<T> elements) {
		List<IDto> dto = elements.stream()
		        .map(model -> mapper.getFromModel(model))
		        .collect(Collectors.toList());
		this.elements = dto;
    }
    
    public int getCurrentPage() {
		return currentPage;
	}

	public int getTotalCount() {
		return totalCount;
	}

	public int getLastPage() {
		return lastPage;
	}

	public int getLimit() {
		return limit;
	}
	
	public int getOffset() {
		return offset;
	}

	public String getFilter() {
		return filter;
	}

	public String getOrder() {
		return order;
	}

	public List<IDto> getElements() {
		return elements;
	}

	public IMapper<T> getMapper() {
		return mapper;
	}
}