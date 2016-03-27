package com.excilys.cdb.persistence.dao;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

import com.excilys.cdb.exception.DAOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jolbox.bonecp.BoneCP;
import com.jolbox.bonecp.BoneCPConfig;

/**
 * DAO factory to centralize DAOs instantiation and connections
 */
public class DAOFactory {

	private static BoneCP connectionPool;
	private static String url;
	private static String user;
	private static String pwd;
    private static ThreadLocal<Connection> transaction = new ThreadLocal<>();
	private static final String PROPERTIES = "dbconf.properties";
	private static Logger log = LoggerFactory.getLogger(DAOFactory.class);
    private static final String TRANSACTION_ERROR = "No transaction have been initialized";

	/**
	 * Initialize the connection pool
	 */
	static {
		// We first get the connection information from the properties file
		Properties properties = new Properties();
		ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
		InputStream propertiesFile = classLoader.getResourceAsStream(PROPERTIES);
		if (propertiesFile == null) {
			log.error("Properties file " + PROPERTIES + " can't be found");
			throw new RuntimeException("Properties file " + PROPERTIES + " can't be found");
		}
		try {
			properties.load(propertiesFile);
			url = properties.getProperty("db.url");
			String driver = properties.getProperty("db.driver");
			user = properties.getProperty("db.username");
			pwd = properties.getProperty("db.password");

			// Load the class driver in the classpath
			Class.forName(driver);

			// Set the pool configuration
			BoneCPConfig config = new BoneCPConfig();
			int minConnection = 10;
			int maxConnection = 20;
			int countPartition = 5;
			config.setJdbcUrl(url);
			config.setUsername(user);
			config.setPassword(pwd);
			config.setMinConnectionsPerPartition(minConnection);
			config.setMaxConnectionsPerPartition(maxConnection);
			config.setPartitionCount(countPartition);

			// Initialize the connection pool
			connectionPool = new BoneCP(config);    
			log.info("Connection pool successfully initialized ({} partitions of each {} max connections)", countPartition, maxConnection);
		} catch (ClassNotFoundException | IOException | SQLException e) {
            log.error(e.getMessage());
            throw new RuntimeException(e);
		}
	}

	/**
	 * 
	 * @return an available connection from the connection pool
	 * @throws SQLException 
	 */
	public static Connection getConnection() throws SQLException {
        return connectionPool.getConnection();
	}

	/**
	 *
	 * @return an instance of company DAO
	 */
	public static CompanyDAO getCompanyDAO() {
		return CompanyDAO.getInstance();
	}

	/**
	 *
	 * @return an instance of computer DAO
	 */
	public static ComputerDAO getComputerDAO() {
		return ComputerDAO.getInstance();
	}

    /**
     * Initialize a new transaction
     */
	public static void initTransaction() {
        try {
			Connection conn = connectionPool.getConnection();
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
    public static Connection getCurrentTransaction() throws DAOException {
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
    public static void commitTransaction() throws DAOException {
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
     * Rollback the current transaction and
     */
    public static void rollbackTransaction() {
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