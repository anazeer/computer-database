package com.excilys.cdb.persistence.dao;

import com.excilys.cdb.persistence.mapper.Mapper;
import com.excilys.cdb.service.Query;
import org.slf4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Abstract implementation of DAO
 */
abstract class AbstractDAO<T> implements DAO<T> {

    protected Logger log;
    protected Mapper<T> mapper;

    /**
     *
     * @param conn the connection
     * @param query the object containing the query constraints
     * @return a prepared statement with the filter set
     * @throws SQLException
     */
    protected abstract PreparedStatement createPreparedStatement(Connection conn, Query query) throws SQLException;

    /**
     * Construct the query text for order
     * @param query the object containing the query constraints
     * @return a query text with the order for the table columns, the empty string otherwise
     */
    protected abstract String getOrderText(Query query);

    /**
     * Construct the query text for filter
     * @param query the object containing the query constraints
     * @return a query text with the filter if not empty, the empty string otherwise
     */
    protected abstract String getFilterText(Query query);

    /**
     * Construct the query text for limit and offset
     * @param query the object containing the query constraints
     * @return a query text with the limit and offset if positives, the empty string otherwise
     */
     protected String getLimitText(Query query) {
        String result = "";
        if (query == null) {
            return result;
        }
        int offset = query.getOffset();
        int limit = query.getLimit();
        if (offset >= 0 && limit > 0) {
            result = " LIMIT " + offset + ", " + limit + " ";
        }
        return result;
    }
}