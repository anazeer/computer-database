package com.excilys.cdb.persistence.dao;

import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.excilys.cdb.exception.DAOException;

/**
 * DAO factory to centralize DAOs instantiation and connections
 */
@Repository
public class DAOFactory {

	@Autowired
	private DataSource dataSource;
	
	// LocalThread for transaction
	private ThreadLocal<Connection> transaction = new ThreadLocal<>();
	
	private Logger log = LoggerFactory.getLogger(DAOFactory.class);
	
	private final String TRANSACTION_ERROR = "No transaction have been initialized";

	private DAOFactory() {
	}

	/**
	 * 
	 * @return an available connection from the connection pool
	 * @throws SQLException 
	 */
	public Connection getConnection() throws SQLException {
		return dataSource.getConnection();
	}

	/**
	 * Initialize a new transaction
	 */
	public void initTransaction() {
		try {
			Connection conn = dataSource.getConnection();
			conn.setAutoCommit(false);
			transaction.set(conn);
			log.info("Transaction initialized");
		} catch (SQLException e) {
			log.error(e.getMessage());
		}
	}

	/**
	 *
	 * @return the current transaction connection
	 */
	public Connection getCurrentTransaction() throws DAOException {
		Connection conn = transaction.get();
		if (conn == null) {
			throw new DAOException(TRANSACTION_ERROR);
		}
		log.info("Transaction retrieved");
		return conn;
	}

	/**
	 * Commit the current transaction and close it
	 */
	public void commitTransaction() throws DAOException {
		try (Connection conn = transaction.get()) {
			if (conn == null) {
				throw new DAOException(TRANSACTION_ERROR);
			}
			conn.commit();
			conn.close();
			log.info("Transaction committed");
		} catch (SQLException e) {
			log.error(e.getMessage());
		}
	}

	/**
	 * Rollback the current transaction and close it
	 */
	public void rollbackTransaction() {
		try (Connection conn = transaction.get()) {
			if (conn == null) {
				log.error(TRANSACTION_ERROR);
			} else {
				conn.rollback();
				conn.close();
				log.info("Transaction rollbacked");
			}
			log.warn("Operation failed, rollback done");
		} catch (SQLException e) {
			log.error("Operation failed, rollback failed");
			log.error(e.getMessage());
		}
	}
}