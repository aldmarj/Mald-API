/**
 * 
 */
package database;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.ArrayList;

import exceptions.BadKeyException;
import exceptions.DataAccessException;
import models.Business;

/**
 * Class to contain helper functions for interaction with the database.
 * Each instance should only be used once.
 * 
 * @author Lawrence
 */
public final class DBBusinessQueries extends DBQueries 
{
	
	/**
	 * CLASS CONSTRUCTOR
	 * 
	 * @throws DataAccessException
	 */
	public DBBusinessQueries() throws DataAccessException
	{
		super();
	}
	
	/**
	 * Creates a business in the database with the given name and tag.
	 * 
	 * @param business - The business details to add.
	 * @throws BadKeyException in the key given is invalid.
	 * @throws DataAccessException if a connection to the datastore cannot be made.
	 */
	public void createBusiness(Business business) 
			throws BadKeyException, DataAccessException
	{
	    try
	    {		
	    	this.setAutoCommit(false);
	    	
	    	createBusinessSQL(business, this);
			DBAccountQueries.createAccountSQL(business.getDefaultEmployee().getAccount(), this);
	    	DBEmployeeQueries.createEmployeeSQL(business.getDefaultEmployee(), this);
	    	
	    	this.commit();
		} 
	    catch (SQLIntegrityConstraintViolationException e)
	    {
			this.handleIntegrityConstaitViolation(e);
			this.rollback();
	    }
	    catch (SQLException e) 
	    {
	    	this.handleSQLException(e);
	    	this.rollback();
		}
	    finally
	    {
	    	this.setAutoCommit(true);
	    	this.closeConnection();
	    }
	}
	
	/**
	 * Returns a business by its given business tag. 
	 * Returns null if no business is found.
	 * 
	 * @param businessTag - The tag of the business to find.
	 * @return The requested business.
	 * @throws DataAccessException If a connection cannot be made to the store.
	 */
	public Business getBusiness(String businessTag) throws DataAccessException
	{
		Business result = null;
		
	    try
	    {		
	    	result = getBusinessSQL(businessTag, this);
		} 
	    catch (SQLException e) 
	    {
	    	this.handleSQLException(e);
		}
	    finally
	    {
	    	this.closeResultSet();
	    	this.closeConnection();
	    }
	    
		return result;
	}
	
	/**
	 * Returns all of the business. 
	 * 
	 * @return All businesses.
	 * @throws DataAccessException If a connection cannot be made to the store.
	 */
	public ArrayList<Business> getAllBusinesses() throws DataAccessException
	{
		ArrayList<Business> result = new ArrayList<Business>();
		
	    try
	    {		
	    	result = getAllBusinessesSQL(this);
		}
	    catch (SQLException e) 
	    {
	    	this.handleSQLException(e);
		}
	    finally
	    {
	    	this.closeResultSet();
	    	this.closeConnection();
	    }
	    
	    return result;
	}
	
	/**
	 * Creates a business in the database with the given business and DB runner.
	 * 
	 * @param business - the business to create in the database.
	 * @param queryRunner - the DB query runner.
	 * @throws SQLException if the DB cannot be reached.
	 * @throws SQLIntegrityConstraintViolationException if a key breaks the constraints of the DB.
	 */
	private static void createBusinessSQL(Business business, DBQueries queryRunner) 
			throws SQLException, SQLIntegrityConstraintViolationException
	{
		String query = "INSERT INTO Business(businessTag, businessName) VALUES (?, ?);";
		
		final PreparedStatement stmt = queryRunner.connection.prepareStatement(query);
		stmt.setString(1, business.getBusinessTag());
		stmt.setString(2, business.getBusinessName());
		
		stmt.executeUpdate();
	}
	
	/**
	 * Gets a business from the database with the given businessTag and DB runner.
	 * 
	 * @param businessTag - the businessTag to search for in the database.
	 * @param queryRunner - the DB query runner.
	 * @throws SQLException if the DB cannot be reached.
	 * @throws DataAccessException if the DB cannot be reached.
	 */
	private static Business getBusinessSQL(String businessTag, DBQueries queryRunner) 
			throws SQLException, DataAccessException
	{
		Business result = null;

		String query = "SELECT businessTag, businessName FROM Business WHERE Business.businessTag = ?;";
		
		final PreparedStatement stmt = queryRunner.connection.prepareStatement(query);
		stmt.setString(1, businessTag);
		
		queryRunner.resultSet = stmt.executeQuery();
		while (queryRunner.resultSet.next())
		{
			result = new Business(
					queryRunner.resultSet.getString("businessTag"),
					queryRunner.resultSet.getString("businessName"));
		}
		
		return result;
	}
	
	/**
	 * Gets all businesses from the database.
	 * 
	 * @param queryRunner - the DB query runner.
	 * @throws SQLException if the DB cannot be reached.
	 * @throws DataAccessException if the DB cannot be reached.
	 */
	public static ArrayList<Business> getAllBusinessesSQL(DBQueries queryRunner) 
			throws DataAccessException, SQLException
	{
		ArrayList<Business> result = new ArrayList<Business>();

		String query = "SELECT businessTag, businessName FROM Business ORDER BY businessTag ASC;";
		
		final PreparedStatement stmt = queryRunner.connection.prepareStatement(query);
		
		queryRunner.resultSet = stmt.executeQuery();
		while (queryRunner.resultSet.next())
		{
			result.add(new Business(
					queryRunner.resultSet.getString("businessTag"),
					queryRunner.resultSet.getString("businessName")));
		}
		
		return result;
	}
}
