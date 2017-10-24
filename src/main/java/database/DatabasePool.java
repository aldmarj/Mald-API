/**
 * 
 */
package database;

import java.beans.PropertyVetoException;

import org.apache.log4j.Logger;

import com.mchange.v2.c3p0.ComboPooledDataSource;

/**
 * Singleton object of the database connection pool. 
 * 
 * @author Lawrence
 */
final public class DatabasePool {
		
	/** The instance of the singleton object **/
	private static DatabasePool instance;
	
	/** The connection pool **/
	private static ComboPooledDataSource pool;
	
	/** Logger **/
	final static Logger logger = Logger.getLogger(DatabasePool.class);
	
	/**
	 * CLASS CONSTRUCTOR
	 */
	private DatabasePool() 
	{
		DatabaseProperties properties = DatabaseProperties.getInstance();

		try 
		{
			pool = new ComboPooledDataSource();
		   
			pool.setDriverClass(properties.getJdbcDriver());
	
			pool.setJdbcUrl(properties.getDatabaseURL());
			pool.setUser(properties.getDatabaseUser());
			pool.setPassword(properties.getDatabasePass());
	
	//	   // the settings below are optional -- c3p0 can work with defaults
	//	   pool.setMinPoolSize(5);
	//	   pool.setAcquireIncrement(5);
	//	   pool.setMaxPoolSize(20);
		   
		} 
		catch (PropertyVetoException e) 
		{
			// The pool was not set up correctly
			logger.error("The pool was not set up correctly", e);
		}
	}
	
	/**
	 * Returns the instance of this database pool object.
	 * 
	 * @return the instance.
	 */
	public static ComboPooledDataSource getInstance()
	{
		if (instance == null)
		{
			instance = new DatabasePool();
		}
		return pool;
	}
}
