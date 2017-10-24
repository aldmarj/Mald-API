/**
 * 
 */
package database;

import java.sql.Connection;
import java.sql.SQLException;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import org.apache.log4j.Logger;

/**
 * Singleton object of the database connection pool. 
 * 
 * @author Lawrence
 */
final public class DatabasePool {
		
	/** Logger **/
	private final static Logger logger = Logger.getLogger(DatabasePool.class);
	
	/** The instance of the singleton object **/
	private static DatabasePool instance;
	
	/** The datasource object **/
	private static DataSource dataSource;

	/**
	 * CLASS CONSTRUCTOR
	 */
	private DatabasePool() 
	{
		try 
		{
			final Context init = new InitialContext();
			dataSource = (DataSource) init.lookup("java:/comp/env/jdbc/api");
		} 
		catch (NamingException e) 
		{
			logger.error("The pool was not set up correctly", e);
		}
	}
	
	/**
	 * Returns a connection from the database pool.
	 * 
	 * @return the connection.
	 * @throws SQLException if no connections available.
	 */
	public static Connection getConnection() throws SQLException
	{
		if (instance == null)
		{
			instance = new DatabasePool();
		}
		return dataSource.getConnection();
	}
}
