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
public final class ConnectionSingleton {

    private static Connection instance;
    private static String url;
    private static String user;
    private static String pwd;
    private static final String PROPERTIES = "dbconf.properties";
    private static Logger log = Logger.getLogger(ConnectionSingleton.class);

    private ConnectionSingleton() {}

    /**
     * Static initialization of database parameters from a properties file
     */
    static {
        try {
            Properties properties = new Properties();
            ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
            InputStream propertiesFile = classLoader.getResourceAsStream(PROPERTIES);
            if (propertiesFile == null) {
                log.error("Properties file " + PROPERTIES + " can't be found");
                throw new RuntimeException("Properties file " + PROPERTIES + " can't be found");
            }
            properties.load(propertiesFile);
            url = properties.getProperty("db.url");
            String driver = properties.getProperty("db.driver");
            user = properties.getProperty("db.username");
            pwd = properties.getProperty("db.password");
            Class.forName(driver);
        }
        catch (ClassNotFoundException | IOException e) {
            log.error(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    /**
     *
     * @return the current instance of Connection if it exists, otherwise a new instance
     */
    public static Connection getInstance() {
        try {
            // We load a new connection if instance has not been initialized
            if (instance == null || instance.isClosed()) {
                instance = DriverManager.getConnection(url, user, pwd);
                log.info("Connection done");
            }
        }
        catch (SQLException e) {
            log.error(e.getMessage());
            throw new RuntimeException(e);
        }
        return instance;
    }
    
    /**
     * Close the current connection if it exists
     */
    public static void closeConnection() {
    	if(instance != null) {
	    	try {
	    		if(!instance.isClosed()) {
	    			instance.close();
	    		}
				instance = null;
			} catch (SQLException e) {
	            log.error(e.getMessage());
	            throw new RuntimeException(e);
			}
	    }
    }
}
