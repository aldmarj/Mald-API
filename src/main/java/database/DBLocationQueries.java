/**
 * 
 */
package database;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.ArrayList;

import com.mysql.jdbc.Statement;

import models.Client;
import models.Location;

/**
 * @author Lawrence
 *
 */
public class DBLocationQueries extends DBQueries {

	/**
	 * CLASS CONSTRUCTOR
	 * 
	 * @throws NoDataStoreConnectionException
	 */
	public DBLocationQueries() throws NoDataStoreConnectionException 
	{
		super();
	}
	
	public Integer createLocationOwner()
			throws SQLException, SQLIntegrityConstraintViolationException
	{
		return createLocationOwnerSQL(this);
	}
	
	/**
	 * Creates a location owner in the database with the given DB runner returns the key of the id.
	 * 
	 * @param queryRunner - the DB query runner.
	 * @throws SQLException if the DB cannot be reached.
	 * @throws SQLIntegrityConstraintViolationException if a key breaks the constraints of the DB.
	 */
	public static Integer createLocationOwnerSQL(DBQueries queryRunner) 
			throws SQLException, SQLIntegrityConstraintViolationException
	{
		Integer result = null;
		
		String query = "INSERT INTO LocationOwner() VALUES ();";
		
		final PreparedStatement stmt = queryRunner.connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
		
		stmt.executeUpdate();
		
		ResultSet resultSet = stmt.getGeneratedKeys();

		if (resultSet.next()) 
		{
		    result = resultSet.getInt("locationOwnerId");
		}
		
		return result;
	}
	
	/**
	 * Creates a location in the database.
	 * 
	 * @param queryRunner - the DB query runner.
	 * @throws SQLException if the DB cannot be reached.
	 * @throws SQLIntegrityConstraintViolationException if a key breaks the constraints of the DB.
	 */
	public static ArrayList<Integer> createLocationsSQL(ArrayList<Location> locations, DBQueries queryRunner) 
			throws SQLException, SQLIntegrityConstraintViolationException
	{		
		ArrayList<Integer> result = new ArrayList<Integer>();
		
		String query = "INSERT INTO Location(postcode, description) VALUES (?, ?);";
		final PreparedStatement stmt = queryRunner.connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);

		for (Location location : locations)
		{
			int index = 1;
			
			stmt.setString(index++, location.getPostCode());
			stmt.setString(index++, location.getDescription());
			
			stmt.addBatch();
		}
		
		stmt.executeBatch();
		
		ResultSet resultSet = stmt.getGeneratedKeys();

		if (resultSet.next()) 
		{
		    result.add(resultSet.getInt("locationId"));
		}

		return result;
	}
	
	/**
	 * Creates a location in the database.
	 * 
	 * @param queryRunner - the DB query runner.
	 * @throws SQLException if the DB cannot be reached.
	 * @throws SQLIntegrityConstraintViolationException if a key breaks the constraints of the DB.
	 */
	public static void createLocationOwnerToLocationsSQL(ArrayList<Integer> locationIds, Integer locationOwnerId, DBQueries queryRunner) 
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
	 * Creates a location owner in the database and returns the key.
	 * 
	 * @return the key generated.
	 * @throws BadKeyException - If the given key is invalid.
	 * @throws NoDataStoreConnectionException - If a connection cannot be made to the store.
	 */
	public static void createLocationsForClientSQL(Client client, DBQueries queryRunner)
			throws SQLException, SQLIntegrityConstraintViolationException
	{		
			    	
    	Integer locationOwnerId = getLocationOwnerIdSQL(client, queryRunner);
    	ArrayList<Integer> locationIds = createLocationsSQL(client.getLocations(), queryRunner);
    	createLocationOwnerToLocationsSQL(locationIds, locationOwnerId, queryRunner);

	}
	
	/**
	 * Get the location owner if from the given client.
	 * 
	 * @param client - the client to interrogate.
	 * @param queryRunner - the DB query runner.
	 * @throws SQLException if the DB cannot be reached.
	 * @throws SQLIntegrityConstraintViolationException if a key breaks the constraints of the DB.
	 */
	public static Integer getLocationOwnerIdSQL(Client client, DBQueries queryRunner) 
			throws SQLException, SQLIntegrityConstraintViolationException
	{
		Integer result = null;
		
		String query = "SELECT locationOwnerId FROM BusinessClient WHERE BusinessClient.clientId = ?;";
		
		final PreparedStatement stmt = queryRunner.connection.prepareStatement(query);
		int index = 1;
		
		stmt.setInt(index, client.getClientId());
		
		ResultSet resultSet = stmt.executeQuery();
		
		if (resultSet.next()) 
		{
		    result = resultSet.getInt("locationOwnerId");
		}
		
		return result;
	}
	
	/**
	 * Get the locations for an owner.
	 * 
	 * @param locationOwnerId - the owner to locate.
	 * @param queryRunner - the DB query runner.
	 * @throws SQLException if the DB cannot be reached.
	 * @throws SQLIntegrityConstraintViolationException if a key breaks the constraints of the DB.
	 */
	public static ArrayList<Location> getLocationsForIdSQL(Integer locationOwnerId, DBQueries queryRunner) 
			throws SQLException, SQLIntegrityConstraintViolationException
	{
		ArrayList<Location> result = new ArrayList<Location>();
		
		String query = "SELECT postcode, description FROM Location "
				+ "LEFT JOIN LocationOwnertoLocation ON Location.locationId = LocationOwnertoLocation.locationId "
				+ "LEFT JOIN LocationOwner ON LocationOwnertoLocation.locationOwnerId = LocationOwner.locationOwnerId "
				+ "WHERE LocationOwner.locationOwnerId = ?;";
		
		final PreparedStatement stmt = queryRunner.connection.prepareStatement(query);
		int index = 1;
		
		stmt.setInt(index, locationOwnerId);
		
		ResultSet resultSet = stmt.executeQuery();
		
		if (resultSet.next()) 
		{
		    result.add(new Location(
		    		resultSet.getString("postcode"),
		    		resultSet.getString("description")));
		}
		
		return result;
	}
	
	public static ArrayList<Location> getLocationsSQL(Client client, DBQueries queryRunner)
			throws SQLException, SQLIntegrityConstraintViolationException
	{
		ArrayList<Location> result = new ArrayList<Location>();

		Integer locationOwnerId = getLocationOwnerIdSQL(client, queryRunner);
		result = getLocationsForIdSQL(locationOwnerId, queryRunner);
		
		return result;
	}
}
