/**
 * 
 */
package database;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.ArrayList;

import org.apache.log4j.Logger;

import models.BusinessClient;

/**
 * Class to contain helper functions for interaction with the database.
 * 
 * @author Lawrence
 */
public abstract class DBClientQueries {
	
	/** Logger **/
	private final static Logger logger = Logger.getLogger(DBClientQueries.class);

	/**
	 * Creates a client in the database with the given name.
	 * 
	 * @param clientName - the tag of the new client.
	 * @param businessTag - the name of the owning business.
	 * @throws IllegalArgumentException - If the given key is not unique.
	 * @throws IOException - If a connection cannot be made to the store.
	 */
	public static void createClient(String clientName, String businessTag) 
			throws DuplicateKeyException, NoDataStoreConnectionException
	{		
	    try (Connection connection = DatabasePool.getConnection())
	    {		
			String query = "INSERT INTO BusinessClient(clientName, businessTag) VALUES (?, ?);";
			
			final PreparedStatement stmt = connection.prepareStatement(query);
			stmt.setString(1, clientName);
			stmt.setString(2, businessTag);
			
			stmt.executeUpdate();
		} 
	    catch (SQLIntegrityConstraintViolationException e)
	    {
			logger.error("A business with the tag: " + businessTag + " doesn't exists.");
			throw new DuplicateKeyException ("A business with that tag: " 
					+ businessTag + " doesn't exist. Must reference an existing business", e, businessTag);
	    }
	    catch (SQLException e) 
	    {
			logger.error("Failed to get a connection to the database" + e);
			throw new NoDataStoreConnectionException ("Failed to get a connection"
					+ " to the database to create the client.", e);
		}
	}
	
	/**
	 * Returns a client by its given clientId. 
	 * Returns null if no client is found.
	 * 
	 * @param clientId - The ID of the client to find.
	 * @return The requested client.
	 * @throws NoDataStoreConnectionException If a connection cannot be made to the store.
	 */
	public static BusinessClient getClient(int clientId) throws NoDataStoreConnectionException
	{
		BusinessClient result = null;
		
		ResultSet resultSet = null;
	    try (Connection connection = DatabasePool.getConnection())
	    {		
			String query = "Select clientName, businessTag FROM BusinessClient WHERE BusinessClient.clientId = ?;";
			
			final PreparedStatement stmt = connection.prepareStatement(query);
			stmt.setInt(1, clientId);
			
			resultSet = stmt.executeQuery();
			while (resultSet.next())
			{
				result = new BusinessClient(
						clientId,
						resultSet.getString("clientName"),
						DBBusinessQueries.getBusiness(resultSet.getString("businessTag")));
			}
		} 
	    catch (SQLException e) 
	    {
			logger.error("Failed to get a connection to the database" + e);
			throw new NoDataStoreConnectionException ("Failed to get a "
					+ "connection to the database to create the business.", e);
		}
	    finally
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
	    
		return result;
	}
	
	/**
	 * Returns all of the clients. 
	 * 
	 * @return All businesses.
	 * @throws NoDataStoreConnectionException If a connection cannot be made to the store.
	 */
	public static ArrayList<BusinessClient> getAllClients() throws NoDataStoreConnectionException
	{
		ArrayList<BusinessClient> result = new ArrayList<BusinessClient>();
		
		ResultSet resultSet = null;
	    try (Connection connection = DatabasePool.getConnection())
	    {		
			String query = "Select clientId, clientName, businessTag FROM BusinessClient;";
			
			final PreparedStatement stmt = connection.prepareStatement(query);
			
			resultSet = stmt.executeQuery();
			while (resultSet.next())
			{
				result.add(new BusinessClient(
						resultSet.getInt("clientId"),
						resultSet.getString("clientName"),
						DBBusinessQueries.getBusiness(resultSet.getString("businessTag"))));
			}
		}
	    catch (SQLException e) 
	    {
			logger.error("Failed to get a connection to the database" + e);
			throw new NoDataStoreConnectionException ("Failed to get a "
					+ "connection to the database to create the business.", e);
		}
	    finally
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
	    
	    return result;
	}
}
