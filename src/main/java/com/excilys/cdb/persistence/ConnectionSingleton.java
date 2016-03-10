package com.excilys.cdb.persistence;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Connection singleton class to keep only one instance of Connection
 * @author excilys
 *
 */
public class ConnectionSingleton {
	
	private static Connection conn = null;
	private final static String url = "jdbc:mysql://localhost:3306/computer-database-db?zeroDateTimeBehavior=convertToNull";
	private final static String user = "admincdb";
	private final static String pwd = "qwerty1234";
	
	private ConnectionSingleton() {}
	
	/**
	 * 
	 * @return the current instance of Connection if it exists, otherwise a new nstance
	 */
	public static Connection getInstance() {
		if(conn == null) {
			try {
				Class.forName("com.mysql.jdbc.Driver");
				conn = DriverManager.getConnection(url, user, pwd);
			} catch (ClassNotFoundException | SQLException e) {
				throw new RuntimeException(e);
			}
		}
		return conn;
	}
}
