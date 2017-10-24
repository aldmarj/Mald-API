/**
 * 
 */
package database;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Singleton file for handling the properties of the database. The database.properties file
 * should be edited before this.
 * 
 * @author Lawrence
 */
public final class DatabaseProperties {

	/** This singletons instance **/
	private static DatabaseProperties instance;
	
	/** The database URL **/
	private String dburl;
	
	/** the database username for the connection **/
	private String dbuser;
	
	/** the database password for the connection **/
	private String dbpass;
	
	/** the JDBC class name for the driver **/
	private String jdbcdriver;
	
	/**
	 * CLASS CONSTRUCTOR
	 */
	private DatabaseProperties()
	{
		Properties prop = new Properties();
		
		// Loading from context account for all pathing in the various builds
		ClassLoader loader = Thread.currentThread().getContextClassLoader();
		
		try (InputStream resourceStream = loader.getResourceAsStream("database.properties")) {
		    prop.load(resourceStream);
		} 
		catch (IOException e) 
		{
			// Unable to load the file specified.
			e.printStackTrace();
		}
		
		this.jdbcdriver = prop.getProperty("jdbcdriver");
		this.dburl = prop.getProperty("dburl");
		this.dbuser = prop.getProperty("dbuser");
		this.dbpass = prop.getProperty("dbpass");
	}
	
	/**
	 * Get the instance of the DatabaseProperties.
	 * 
	 * @return the instance.
	 */
	public static DatabaseProperties getInstance()
	{
		if (instance == null)
		{
			instance = new DatabaseProperties();
		}
		return instance;
	}
	
	/**
	 * Get the URL of the database connection.
	 * 
	 * @return the url of the db connection.
	 */
	public String getDatabaseURL() 
	{
		return dburl;
	}

	/**
	 * Get the user for the database connection.
	 * 
	 * @return the user of the db connection.
	 */
	public String getDatabaseUser() 
	{
		return dbuser;
	}

	/**
	 * Get the password for the database connection.
	 * 
	 * @return the pass of the db connection.
	 */
	public String getDatabasePass() 
	{
		return dbpass;
	}

	/**
	 * Get the driver classpath.
	 * 
	 * @return the driver classpath.
	 */
	public String getJdbcDriver() 
	{
		return jdbcdriver;
	}
}
