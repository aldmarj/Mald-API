/**
 * 
 */
package database;

import java.beans.PropertyVetoException;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

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
	public static final String DATABASE_PASSWORD = "A$ajppNE8q&pNBbr";

	/** Constant for the database username. */
	public static final String DATABASE_USERNAME = "api";
	
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
		Properties prop = new Properties();
		InputStream input = null;

		try 
		{
			input = new FileInputStream("config.properties");

			// load a properties file
			prop.load(input);
			
			pool = new ComboPooledDataSource();
		   
			pool.setDriverClass(prop.getProperty("jdbcdriver"));
	
			pool.setJdbcUrl(prop.getProperty("dburl"));
			pool.setUser(prop.getProperty("dbuser"));
			pool.setPassword(prop.getProperty("dbpass"));
	
	//	   // the settings below are optional -- c3p0 can work with defaults
	//	   pool.setMinPoolSize(5);
	//	   pool.setAcquireIncrement(5);
	//	   pool.setMaxPoolSize(20);
		   
		} 
		catch (PropertyVetoException | IOException e) 
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
