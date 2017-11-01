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

import models.Business;

/**
 * Class to contain helper functions for interaction with the database.
 * 
 * @author Lawrence
 */
public abstract class DBBusinessQueries {
	
	/** Logger **/
	private final static Logger logger = Logger.getLogger(DBBusinessQueries.class);

	/**
	 * Creates a business in the database with the given name and tag.
	 * 
	 * @param businessTag - the tag of the new business.
	 * @param businessName - the name of the new business.
	 * @throws IllegalArgumentException - If the given key is not unique.
	 * @throws IOException - If a connection cannot be made to the store.
	 */
	public static void createBusiness(String businessTag, String businessName) 
			throws BadKeyException, NoDataStoreConnectionException
	{
	    try (Connection connection = DatabasePool.getConnection())
	    {		
			String query = "INSERT INTO Business(businessTag, businessName) VALUES (?, ?);";
			
			final PreparedStatement stmt = connection.prepareStatement(query);
			stmt.setString(1, businessTag);
			stmt.setString(2, businessName);
			
			stmt.executeUpdate();
		} 
	    catch (SQLIntegrityConstraintViolationException e)
	    {
			logger.error("A business with the tag: " + businessTag + " already exists.");
			throw new BadKeyException ("A business with that tag: " 
					+ businessTag + " already exists. The tag must be unique.", e, businessTag);
	    }
	    catch (SQLException e) 
	    {
			logger.error("Failed to get a connection to the database" + e);
			throw new NoDataStoreConnectionException ("Failed to get a connection"
					+ " to the database to create the business.", e);
		}
	}
	
	/**
	 * Returns a business by its given business tag. 
	 * Returns null if no business is found.
	 * 
	 * @param businessTag - The tag of the business to find.
	 * @return The requested business.
	 * @throws NoDataStoreConnectionException If a connection cannot be made to the store.
	 */
	public static Business getBusiness(String businessTag) throws NoDataStoreConnectionException
	{
		Business result = null;
		
		ResultSet resultSet = null;
	    try (Connection connection = DatabasePool.getConnection())
	    {		
			String query = "Select businessTag, businessName FROM Business WHERE Business.businessTag = ?;";
			
			final PreparedStatement stmt = connection.prepareStatement(query);
			stmt.setString(1, businessTag);
			
			resultSet = stmt.executeQuery();
			while (resultSet.next())
			{
				result = new Business(
						resultSet.getString("businessTag"),
						resultSet.getString("businessName"));
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
	 * Returns all of the business. 
	 * 
	 * @return All businesses.
	 * @throws NoDataStoreConnectionException If a connection cannot be made to the store.
	 */
	public static ArrayList<Business> getAllBusinesses() throws NoDataStoreConnectionException
	{
		ArrayList<Business> result = new ArrayList<Business>();
		
		ResultSet resultSet = null;
	    try (Connection connection = DatabasePool.getConnection())
	    {		
			String query = "Select businessTag, businessName FROM Business;";
			
			final PreparedStatement stmt = connection.prepareStatement(query);
			
			resultSet = stmt.executeQuery();
			while (resultSet.next())
			{
				result.add(new Business(
						resultSet.getString("businessTag"),
						resultSet.getString("businessName")));
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
