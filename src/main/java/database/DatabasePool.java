/**
 * 
 */
package database;

import java.beans.PropertyVetoException;

import com.mchange.v2.c3p0.ComboPooledDataSource;

/**
 * Singleton object of the database connection pool. 
 * 
 * @author Lawrence
 */
final public class DatabasePool {
	
	/** Constant for the database connection. */
	public static final String DATABASE_CONNECTION = "jdbc:mysql://mald-rdbms.cm5fqzlymdws.eu-west-2.rds.amazonaws.com/EmployeeManagement";

	/** Constant for the database password. */
	public static final String DATABASE_PASSWORD = "wingedEyebrows";

	/** Constant for the database username. */
	public static final String DATABASE_USERNAME = "maldAdmin";
	
	/** Constant for the JDBC Driver. */
	public static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
	
	/** The instance of the singleton object **/
	private static DatabasePool instance;
	
	private static ComboPooledDataSource pool;
	
	/**
	 * CLASS CONSTRUCTOR
	 */
	private DatabasePool() 
	{
		try 
		{
			pool = new ComboPooledDataSource();
		   
			pool.setDriverClass(JDBC_DRIVER);
	
			pool.setJdbcUrl(DATABASE_CONNECTION);
			pool.setUser(DATABASE_USERNAME);
			pool.setPassword(DATABASE_PASSWORD);
	
	//	   // the settings below are optional -- c3p0 can work with defaults
	//	   pool.setMinPoolSize(5);
	//	   pool.setAcquireIncrement(5);
	//	   pool.setMaxPoolSize(20);
		   
		} 
		catch (PropertyVetoException e) 
		{
			e.printStackTrace();
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
