package com.excilys.cdb.pagination.mapper;

import java.util.Map;

import com.excilys.cdb.pagination.util.PageRequest;
import com.excilys.cdb.util.Constraint;
import com.excilys.cdb.util.Order;
import com.excilys.cdb.validation.Parser;

/**
 * Page mapper for request
 */
public class PageRequestMapper {

    // ID for the current page number
    private static final String PAGE_NUMBER_ID = "page";

    // ID for the limit
    private static final String LIMIT_ID = "limit";

    // Id for the search form
    private static final String SEARCH_ID = "search";

    // Id for the order parameter
    private static final String ORDER_ID = "order";
    
    /**
     * Analyze the user request and generate the corresponding page
     * @param map the page request
     * @return the page depending on the request parameters
     */
    public static PageRequest get(Map<String, String> request) {
    	    	
        // We first get the page information from the URL (method GET)
        String pageRequest = request.get(PAGE_NUMBER_ID);
        String limitRequest = request.get(LIMIT_ID);
        String searchRequest = request.get(SEARCH_ID);
        String orderRequest = request.get(ORDER_ID);

        // Parse the page number
        int page = Parser.parsePageNumber(pageRequest);

        // Parse the limit number
        int limit = Parser.parseLimit(limitRequest);

        // Set the search filter
        String search = Parser.parseSearch(searchRequest);

        // Set the order
        Order order = Parser.parseOrder(orderRequest);

        // Construct and return the result page
        Constraint query = new Constraint.Builder().limit(limit).filter(search).order(order).build();
        return new PageRequest(query, page);
    }
}
