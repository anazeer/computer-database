package com.excilys.cdb.validation;

import com.excilys.cdb.service.Order;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Parser for page parameters
 */
public class Parser {

    // Limit allowed values
    private static List<Integer> limitValues;

    // Order existing values (in String format)
    private static List<String> orders;

    static {
        limitValues = new ArrayList<>(3);
        limitValues.add(10);
        limitValues.add(20);
        limitValues.add(50);
        orders = Arrays.stream(Order.values()).map(Enum::name).collect(Collectors.toList());
    }

    /**
     * Parse a page string into a page integer
     * @param page the page number
     * @return the parsed page, or 1 if the parsing failed
     */
    public static int parsePageNumber(String page) {
        int result;
        if (page != null && !page.trim().isEmpty()) {
            try {
                result = Integer.parseInt(page);
            } catch(NumberFormatException e) {
                result = 1;
            }
        } else {
            result = 1;
        }
        return result;
    }

    /**
     * Parse a limit string into an integer
     * @param limit the page limit
     * @return the parsed limit, or 10 if the parsing failed
     */
    public static int parseLimit(String limit) {
        int result;
        if(limit != null && !limit.trim().isEmpty()) {
            try {
                result = Integer.parseInt(limit);
                if (!limitValues.contains(result)) {
                    result = 10;
                }
            } catch(NumberFormatException e) {
                result = 10;
            }
        } else {
            result = 10;
        }
        return result;
    }

    /**
     *
     * @param search the search string
     * @return the search string if it is valid, an empty string otherwise
     */
    public static String parseSearch(String search) {
        String result = "";
        if (search != null && !search.trim().isEmpty()) {
            result = search;
        }
        return result;
    }

    /**
     * Parse an order string into an order enum
     * @param order the order string
     * @return the parsed Order, or null if no matches were found
     */
    public static Order parseOrder(String order) {
        Order result = null;
        if(order != null && !order.trim().isEmpty()) {
            String orderString = order.toUpperCase();
            if (orders.contains(orderString)) {
                result = Order.valueOf(orderString);
            }
        }
        return result;
    }
}