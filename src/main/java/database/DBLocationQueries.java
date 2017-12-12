/**
 * 
 */
package database;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import models.Location;

/**
 * Class to contain helper functions for interaction with the database.
 * Each instance should only be used once.
 * 
 * @author Lawrence
 */
public class DBLocationQueries extends DBQueries 
{

	/**
	 * CLASS CONSTRUCTOR
	 * 
	 * @throws NoDataStoreConnectionException
	 */
	public DBLocationQueries() throws NoDataStoreConnectionException 
	{
		super();
	}
	
	/**
	 * Gets the locations related to the owner location id.
	 * 
	 * @param locationOwnerId the id to search for.
	 * @return the locations related to the id.
	 * @throws SQLException if the DB cannot be reached.
	 * @throws SQLIntegrityConstraintViolationException if a key breaks the constraints of the DB.
	 */
	public List<Location> getLocationsForId(Integer locationOwnerId) 
			throws SQLIntegrityConstraintViolationException, SQLException
	{
		return getLocationsForIdSQL(locationOwnerId, this);
	}
	
	/**
	 * Create and return the location owner id.
	 * 
	 * @return the id of the location owner.
	 * @throws BadKeyException 
	 * @throws NoDataStoreConnectionException 
	 * @throws SQLException if the DB cannot be reached.
	 * @throws SQLIntegrityConstraintViolationException if a key breaks the constraints of the DB.
	 */
	public int createLocationOwner() throws BadKeyException, NoDataStoreConnectionException 
	{
		int result = -1;
		try
		{
			result = createLocationOwnerSQL(this);
		}
		catch (SQLIntegrityConstraintViolationException e) 
		{
			this.handleIntegrityConstaitViolation(e);
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
	 * Creates a location owner in the database with the given DB runner and returns the key of the id.
	 * 
	 * @param queryRunner - the DB query runner.
	 * @return the key of the created location owner.
	 * @throws SQLException if the DB cannot be reached.
	 * @throws SQLIntegrityConstraintViolationException if a key breaks the constraints of the DB.
	 */
	public static Integer createLocationOwnerSQL(DBQueries queryRunner) 
			throws SQLException, SQLIntegrityConstraintViolationException
	{
		Integer result = null;
		
		String query = "INSERT INTO LocationOwner() VALUES ();";
		
		final PreparedStatement stmt = queryRunner.connection.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS);
		
		stmt.executeUpdate();
		
		ResultSet resultSet = stmt.getGeneratedKeys();

		if (resultSet.next()) 
		{
		    result = resultSet.getInt(1);
		}
		
		return result;
	}
	
	/**
	 * Creates a location in the database.
	 * 
	 * @param locations - The locations to create.
	 * @param queryRunner - The DB query runner.
	 * @throws SQLException if the DB cannot be reached.
	 * @throws SQLIntegrityConstraintViolationException if a key breaks the constraints of the DB.
	 */
	public static List<Integer> createLocationsSQL(Collection<Location> locations, DBQueries queryRunner) 
			throws SQLException, SQLIntegrityConstraintViolationException
	{		
		List<Integer> result = new ArrayList<Integer>();
		
		String query = "INSERT INTO Location(postcode, description) VALUES (?, ?);";
		final PreparedStatement stmt = queryRunner.connection.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS);

		for (Location location : locations)
		{
			int index = 1;
			
			stmt.setString(index++, location.getPostCode());
			stmt.setString(index++, location.getDescription());
			
			stmt.addBatch();
		}
		
		stmt.executeBatch();
		
		ResultSet resultSet = stmt.getGeneratedKeys();

		while (resultSet.next()) 
		{
		    result.add(resultSet.getInt(1));
		}

		return result;
	}
	
	/**
	 * Creates a the link between location and owner in the database.
	 * 
	 * @param locationIds - The IDs of the locations to marry up.
	 * @param locationOwnerId - The ID of the location owner.
	 * @param queryRunner - the DB query runner.
	 * @throws SQLException if the DB cannot be reached.
	 * @throws SQLIntegrityConstraintViolationException if a key breaks the constraints of the DB.
	 */
	public static void createLocationOwnerToLocationsSQL(List<Integer> locationIds, Integer locationOwnerId, DBQueries queryRunner) 
			throws SQLException, SQLIntegrityConstraintViolationException
	{		
		String query = "INSERT INTO LocationOwnertoLocation(locationOwnerId, locationId) VALUES (?, ?);";
		final PreparedStatement stmt = queryRunner.connection.prepareStatement(query);

		for (Integer locationId : locationIds)
		{
			int index = 1;
			
			stmt.setInt(index++, locationOwnerId);
			stmt.setInt(index++, locationId);
			
			stmt.addBatch();
		}
		
		stmt.executeBatch();
	}
	
	/**
	 * Get the locations for an owner id.
	 * 
	 * @param locationOwnerId - the owner to locate.
	 * @param queryRunner - the DB query runner.
	 * @return the locations for the id.
	 * @throws SQLException if the DB cannot be reached.
	 * @throws SQLIntegrityConstraintViolationException if a key breaks the constraints of the DB.
	 */
	public static List<Location> getLocationsForIdSQL(Integer locationOwnerId, DBQueries queryRunner) 
			throws SQLException, SQLIntegrityConstraintViolationException
	{
		List<Location> result = new ArrayList<Location>();
		
		String query = "SELECT postcode, description FROM Location "
				+ "LEFT JOIN LocationOwnertoLocation ON Location.locationId = LocationOwnertoLocation.locationId "
				+ "LEFT JOIN LocationOwner ON LocationOwnertoLocation.locationOwnerId = LocationOwner.locationOwnerId "
				+ "WHERE LocationOwner.locationOwnerId = ?;";
		
		final PreparedStatement stmt = queryRunner.connection.prepareStatement(query);
		int index = 1;
		
		stmt.setInt(index, locationOwnerId);
		
		ResultSet resultSet = stmt.executeQuery();
		
		while (resultSet.next()) 
		{
		    result.add(new Location(
		    		resultSet.getString("postcode"),
		    		resultSet.getString("description")));
		}
		
		return result;
	}
}
