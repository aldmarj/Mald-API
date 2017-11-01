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
	
	protected final Connection connection;
	
	protected ResultSet resultSet;
	
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
    				+ " to the database to create the business.", e);
    	}
	}
	
	protected void handleSQLException(SQLException e) throws NoDataStoreConnectionException
	{
		logger.error("Failed to get a connection to the database" + e);
		throw new NoDataStoreConnectionException ("Failed to get a connection"
				+ " to the database to create the business.", e);
	}
	
	protected void handleIntegrityConstaitViolation(SQLIntegrityConstraintViolationException e) 
			throws BadKeyException
	{
		logger.error("Tried to insert a primary or foreign key that does not abide to constraints.", e);
		throw new BadKeyException ("Tried to insert a primary or foreign key that does not abide to constraints.", e);
	}
	
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
