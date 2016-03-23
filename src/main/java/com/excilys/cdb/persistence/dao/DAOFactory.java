package com.excilys.cdb.persistence.dao;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

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
	private static final String PROPERTIES = "dbconf.properties";
	private static Logger log = LoggerFactory.getLogger(DAOFactory.class);

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
			int maxConnection = 5;
			int countPartition = 2;
			config.setJdbcUrl(url);
			config.setUsername(user);
			config.setPassword(pwd);
			config.setMinConnectionsPerPartition(5);
			config.setMaxConnectionsPerPartition(maxConnection);
			config.setPartitionCount(countPartition);

			// Initialize the connection pool
			connectionPool = new BoneCP(config);    
			log.info("Connection pool successfully initialized ({} partition of each {} max connection ", countPartition, maxConnection);
		}
		catch(ClassNotFoundException | IOException | SQLException e) {
            log.error(e.getMessage());
            throw new RuntimeException(e);
		}
	}

	/**
	 * 
	 * @return an available connection from the connection pool
	 * @throws SQLException 
	 */
	protected static Connection getConnection() throws SQLException {
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
}