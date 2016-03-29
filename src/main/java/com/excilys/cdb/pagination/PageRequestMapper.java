package com.excilys.cdb.pagination;

import com.excilys.cdb.service.Order;
import com.excilys.cdb.service.Query;
import com.excilys.cdb.validation.Parser;

import javax.servlet.http.HttpServletRequest;

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
     * @param request the page request
     * @return the page depending on the request parameters
     */
    public static ComputerPage get(HttpServletRequest request) {

        // We first get the page information from the URL (method GET)
        String pageRequest = request.getParameter(PAGE_NUMBER_ID);
        String limitRequest = request.getParameter(LIMIT_ID);
        String searchRequest = request.getParameter(SEARCH_ID);
        String orderRequest = request.getParameter(ORDER_ID);

        // Parse the page number
        int page = Parser.parsePageNumber(pageRequest);

        // Parse the limit number
        int limit = Parser.parseLimit(limitRequest);

        // Set the search filter
        String search = Parser.parseSearch(searchRequest);

        // Set the order
        Order order = Parser.parseOrder(orderRequest);

        // Construct and return the result page
        Query query = new Query.Builder().limit(limit).filter(search).order(order).build();
        return new ComputerPage(query, page);
    }

}
