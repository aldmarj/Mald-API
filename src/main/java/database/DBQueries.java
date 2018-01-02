/**
 * 
 */
package database;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;

import org.apache.log4j.Logger;

/**
 * @author Lawrence
 *
 */
public class DBQueries {

	/** Logger **/
	private final static Logger logger = Logger.getLogger(DBQueries.class);
	
	/** The connection to the DB **/
	protected final Connection connection;
	
	/** The resultSet from the query **/
	protected ResultSet resultSet;
	
	/**
	 * CLASS CONSTRUCTOR
	 * 
	 * @throws NoDataStoreConnectionException
	 */
	public DBQueries() throws NoDataStoreConnectionException
	{
    	try 
    	{
			this.connection = DatabasePool.getConnection();
			
			this.resultSet = null;
		} 
    	catch (SQLException e) 
    	{
    		logger.error("Failed to get a connection to the database" + e);
    		throw new NoDataStoreConnectionException ("Failed to get a connection"
    				+ " to the database.", e);
    	}
	}
	
	/**
	 * Log the issue and throw a more general exception.
	 * 
	 * @param e the original exception thrown.
	 * @throws NoDataStoreConnectionException when the datastore cannot be reached.
	 */
	protected void handleSQLException(SQLException e) throws NoDataStoreConnectionException
	{
		logger.error("Failed to get a connection to the database");
		throw new NoDataStoreConnectionException ("Failed to get a connection"
				+ " to the database.", e);
	}
	
	/**
	 * Log the issue and throw a more general exception.
	 * 
	 * @param e the original exception thrown.
	 * @throws NoDataStoreConnectionException when the datastore cannot be reached.
	 */
	protected void handleIntegrityConstaitViolation(SQLIntegrityConstraintViolationException e) 
			throws BadKeyException
	{
		logger.error("Tried to insert a primary or foreign key that does not abide to constraints.");
		throw new BadKeyException ("Tried to insert a primary or foreign key that does not abide to constraints.", e);
	}
	
	/**
	 * Turn the autocommit function on and off.
	 * 
	 * @param toggle the on or off status of the autocommit.
	 */
	protected void setAutoCommit(boolean toggle)
	{
    	try 
    	{
			this.connection.setAutoCommit(toggle);
		} 
    	catch (SQLException e)
    	{
    		if (toggle)
    		{
    			logger.error("Failed to enable autocommit,"
    					+ " consistency errors may be present" + e);
    		}
    		else
    		{
    			logger.error("Failed to disable autocommit,"
    					+ " consistency errors may be present" + e);
    		}
		}
	}
	
	/**
	 * Commit the changes to the database.
	 * 
	 * @throws NoDataStoreConnectionException
	 */
	protected void commit() throws NoDataStoreConnectionException
	{
		try 
		{
			if (this.connection.getAutoCommit())
			{
				logger.error("Tried to commit a transaction with autocommit enabled.");
			}
			else
			{
				this.connection.commit();
			}
		} 
		catch (SQLException e) 
		{
			handleSQLException(e);
		}	
	}
	
	/**
	 * Clear the changes to the database.
	 * 
	 * @throws NoDataStoreConnectionException
	 */
	protected void rollback() throws NoDataStoreConnectionException
	{
		try 
		{
			if (this.connection.getAutoCommit())
			{
				logger.error("Tried to rollback a transaction with autocommit enabled.");
			}
			else
			{
				this.connection.rollback();
			}
		} 
		catch (SQLException e) 
		{
			handleSQLException(e);
		}	
	}
	
	/**
	 * Close the resultSet.
	 */
	protected void closeResultSet()
	{
		if (resultSet != null)
		{
	    	try 
	    	{
				resultSet.close();
			} 
	    	catch (SQLException e) 
	    	{
				logger.error("Database elements failed to close, resources may be leaking.", e);
			}
		}
    }
	
	/**
	 * Close the database connection.
	 */
	protected void closeConnection()
	{
		try 
		{
			this.connection.close();
		} 
		catch (SQLException e) 
		{
			logger.error("Database elements failed to close, resources may be leaking.", e);
		}
	}
}
