package com.excilys.cdb.persistence;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

import org.apache.log4j.Logger;

/**
 * Connection singleton class to keep only one instance of Connection
 * @author excilys
 *
 */
public class ConnectionSingleton {
	
	private static Connection instance = null;
	private static String url;
	private static String driver;
	private static String user;
	private static String pwd;
	private static final String PROPERTIES = "dbconf.properties";
	private static Logger log = Logger.getLogger(ConnectionSingleton.class);
	
	private ConnectionSingleton() {}
	
	/**
	 * 
	 * @return the current instance of Connection if it exists, otherwise a new instance
	 */
	public static Connection getInstance() {
		if(instance == null) {
			Properties properties = new Properties();
			ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
			InputStream propertiesFile = classLoader.getResourceAsStream(PROPERTIES);
			if(propertiesFile == null) {
				log.error("Properties file " + PROPERTIES + " can't be found");
				throw new RuntimeException("Properties file " + PROPERTIES + " can't be found");
			}
			try {
				properties.load(propertiesFile);
				url = properties.getProperty("db.url");
				driver = properties.getProperty("db.driver");
				user = properties.getProperty("db.username");
				pwd = properties.getProperty("db.password");
				log.error(url + " oui");
				log.error(driver + " oui");
				log.error(user + " oui");
				Class.forName(driver);
				instance = DriverManager.getConnection(url, user, pwd);
				log.info("Connection done");
			} catch (ClassNotFoundException | SQLException e) {
				log.error(e.getMessage());
				throw new RuntimeException(e);
			} catch (IOException e) {
				log.error(e.getMessage());
				throw new RuntimeException(e);
			}
		}
		return instance;
	}
}
