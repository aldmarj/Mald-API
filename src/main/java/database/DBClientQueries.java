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

import exceptions.BadKeyException;
import exceptions.DataAccessException;
import models.Client;

/**
 * Class to contain helper functions for interaction with the database. Each instance should only be
 * used once.
 *
 * @author Lawrence
 */
public final class DBClientQueries extends DBQueries 
{
	
	/**
	 * CLASS CONSTRUCTOR
	 * 
	 * @throws DataAccessException
	 */
	public DBClientQueries() throws DataAccessException
	{
		super();
	}
	
	/**
	 * Creates a client in the database with the given name.
	 * 
	 * @param client - the new client to add
	 * @throws BadKeyException - If the given key is invalid.
	 * @throws DataAccessException - If a connection cannot be made to the store.
	 */
	public void createClient(Client client) 
			throws BadKeyException, DataAccessException
	{		
	    try
	    {		
	    	this.setAutoCommit(false);
	    	
	    	createClientSQL(client, this);
	    	createLocationsForClientSQL(client, this);
	    	
	    	this.commit();
		} 
	    catch (SQLIntegrityConstraintViolationException e)
	    {
	    	this.rollback();
			this.handleIntegrityConstaitViolation(e);
	    }
	    catch (SQLException e) 
	    {
	    	this.rollback();
	    	this.handleSQLException(e);
		}
	    finally
	    {
	    	this.setAutoCommit(true);
	    	this.closeResultSet();
	    	this.closeConnection();
	    }
	}
	
	/**
	 * Returns a client by its given clientId. 
	 * Returns null if no client is found.
	 * 
	 * @param clientId the ID of the client to find.
	 * @return The requested client.
	 * @throws DataAccessException If a connection cannot be made to the store.
	 */
	public Client getClient(int clientId) throws DataAccessException
	{
		Client result = null;
		
	    try
	    {
	    	result = getClientSQL(clientId, this);
	    	
	    	if (result != null)
	    	{
		    	result.setLocations(
		    			DBLocationQueries.getLocationsForIdSQL(
		    			DBClientQueries.getClientLocationOwnerIdSQL(result, this), this));
	    	}
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
	 * Return the client's location owner id.
	 * 
	 * @param client the client to return the location of.
	 * @return the location owner id.
	 * @throws DataAccessException if the data store cannot be reached.
	 */
	public Integer getClientLocationOwnerId(Client client) throws DataAccessException
	{
		Integer result = null;
		
	    try
	    {		
	    	result = getClientLocationOwnerIdSQL(client, this);
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
	 * Returns all of the clients. 
	 * 
	 * @return All clients.
	 * @throws DataAccessException If a connection cannot be made to the store.
	 */
	public ArrayList<Client> getAllClients(final String businessTag) throws DataAccessException
	{
		ArrayList<Client> result = new ArrayList<Client>();
		
	    try
	    {		
	    	result = getAllClientsSQL(businessTag, this);
	    	
	    	for (Client client : result)
	    	{
	    		client.setLocations(
		    			DBLocationQueries.getLocationsForIdSQL(
		    	    	DBClientQueries.getClientLocationOwnerIdSQL(client, this), this));
	    	}
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
	 * Returns a range of clients for a given business based of how much they have worked
	 * between two given times. 
	 * 
	 * @return the range of client ordered by how much they have worked.
	 * @throws DataAccessException If a connection cannot be made to the store.
	 */
	public Collection<Client> getAllClientsbyMostWorkedRangeBetweenTimes(String businessTag,
			int startRange, int endRange, long startTimeRange, long endTimeRange) 
					throws DataAccessException
	{
		Collection<Client> result = new ArrayList<Client>();
		
	    try
	    {		
	    	result = 
	    		getAllClientsbyMostWorkedRangeBetweenTimesSQL(
	    				businessTag, startRange, endRange, startTimeRange, endTimeRange, this);
	    	
	    	for (Client client : result)
	    	{
	    		client.setLocations(
		    			DBLocationQueries.getLocationsForIdSQL(
		    	    	DBClientQueries.getClientLocationOwnerIdSQL(client, this), this));
	    	}
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
	 * Creates a client in the database with the given client and DB runner.
	 * 
	 * @param client - the client to create in the database.
	 * @param queryRunner - the DB query runner.
	 * @throws SQLException if the DB cannot be reached.
	 * @throws SQLIntegrityConstraintViolationException if a key breaks the constraints of the DB.
	 * @throws DataAccessException if the DB cannot be reached.
	 */
	public static void createClientSQL(Client client, DBQueries queryRunner) 
			throws SQLException, SQLIntegrityConstraintViolationException, DataAccessException
	{
		int locationOwnerId = DBLocationQueries.createLocationOwnerSQL(queryRunner);
		
		String query = "INSERT INTO BusinessClient(clientName, businessTag, locationOwnerId) VALUES (?, ?, ?);";
		
		final PreparedStatement stmt = queryRunner.connection.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS);
		int index = 1;
		
		stmt.setString(index++, client.getClientName());
		stmt.setString(index++, client.getBusinessTag());
		stmt.setInt(index++, locationOwnerId);
		
		stmt.executeUpdate();

		ResultSet resultSet = stmt.getGeneratedKeys();

		if (resultSet.next()) 
		{
		    client.setClientId(resultSet.getInt(1));
		}
	}
	
	/**
	 * Creates the locations for the given client in the database.
	 * 
	 * @throws SQLException - If the given key is invalid.
	 * @throws SQLIntegrityConstraintViolationException - If a connection cannot be made to the store.
	 */
	public static void createLocationsForClientSQL(Client client, DBQueries queryRunner)
			throws SQLException, SQLIntegrityConstraintViolationException
	{
		// First find the owner if of the client
    	Integer locationOwnerId = getClientLocationOwnerIdSQL(client, queryRunner);
    	
    	// Then create the locations for this id
    	if (client.getLocations() != null)
    	{
	    	List<Integer> locationIds = DBLocationQueries.createLocationsSQL(client.getLocations(), queryRunner);
	    	DBLocationQueries.createLocationOwnerToLocationsSQL(locationIds, locationOwnerId, queryRunner);
    	}
	}
	
	/**
	 * Gets a client from the database with the given clientId and DB runner.
	 * 
	 * @param client id - the client id to search for in the database.
	 * @param queryRunner - the DB query runner.
	 * @return the client requested.
	 * @throws SQLException if the DB cannot be reached.
	 * @throws DataAccessException if the DB cannot be reached.
	 */
	public static Client getClientSQL(int clientId, DBQueries queryRunner)
			throws SQLException, DataAccessException
	{
		Client result = null;

		String query = "SELECT clientName, businessTag FROM BusinessClient WHERE BusinessClient.clientId = ?;";
		
		final PreparedStatement stmt = queryRunner.connection.prepareStatement(query);
		int index = 1;
		
		stmt.setInt(index++, clientId);
		
		queryRunner.resultSet = stmt.executeQuery();
		while (queryRunner.resultSet.next())
		{
			result = new Client(
					clientId,
					queryRunner.resultSet.getString("clientName"),
					queryRunner.resultSet.getString("businessTag")
			);
		}
		
		return result;
	}
	
	
	/**
	 * Gets all clients from the database.
	 * 
	 * @param businessTag The businessTag to limit the results
	 * @param queryRunner - the DB query runner.
	 * @return All clients. 
	 * @throws SQLException if the DB cannot be reached.
	 * @throws DataAccessException if the DB cannot be reached.
	 */
	public static ArrayList<Client> getAllClientsSQL(final String businessTag, final DBQueries queryRunner)
			throws SQLException, DataAccessException
	{
		ArrayList<Client> result = new ArrayList<Client>();

		String query = "SELECT clientId, clientName, businessTag FROM BusinessClient "
				+ "WHERE BusinessClient.businessTag = ? ORDER BY clientName ASC;";
		
		final PreparedStatement stmt = queryRunner.connection.prepareStatement(query);
		stmt.setString(1, businessTag);
		
		queryRunner.resultSet = stmt.executeQuery();
		while (queryRunner.resultSet.next())
		{
			result.add(new Client(
					queryRunner.resultSet.getInt("clientId"),
					queryRunner.resultSet.getString("clientName"),
					queryRunner.resultSet.getString("businessTag")));
		}
		
		return result;
	}
	
	/**
	 * Get the location owner id from the given client.
	 * 
	 * @param client - the client to interrogate.
	 * @param queryRunner - the DB query runner.
	 * @throws SQLException if the DB cannot be reached.
	 * @throws SQLIntegrityConstraintViolationException if a key breaks the constraints of the DB.
	 */
	public static Integer getClientLocationOwnerIdSQL(Client client, DBQueries queryRunner) 
			throws SQLException, SQLIntegrityConstraintViolationException
	{
		Integer result = null;
		
		String query = "SELECT locationOwnerId FROM BusinessClient WHERE BusinessClient.clientId = ?;";
		
		final PreparedStatement stmt = queryRunner.connection.prepareStatement(query);
		int index = 1;
		
		stmt.setInt(index++, client.getClientId());
		
		ResultSet resultSet = stmt.executeQuery();
		
		if (resultSet.next()) 
		{
		    result = resultSet.getInt(1);
		}
		
		return result;
	}
	
	/**
	 * Get clients from the database from a business within a range given by most worked.
	 * 
	 * @param businessTag - The business to interrogate.
	 * @param startrange - The start of the range to return.
	 * @param endRange - The end of the range to return.
	 * @param queryRunner - the DB query runner.
	 * @throws SQLException if a key constraint is violated.
	 * @throws DataAccessException if the DB cannot be reached.
	 */
	private static Collection<Client> getAllClientsbyMostWorkedRangeBetweenTimesSQL(
			String businessTag, int startRange, int endRange, long startTimeRange, long endTimeRange,
			DBQueries queryRunner) throws SQLException, DataAccessException 
	{
		Collection<Client> result = new ArrayList<Client>();
		
		String query = "SELECT clientName, BusinessClient.clientId, BusinessClient.businessTag, "
			+ "SUM(WorkLog.endTime - WorkLog.startTime) as hoursWorked "
			+ "FROM BusinessClient "
			+ "LEFT JOIN WorkLog ON BusinessClient.clientId = WorkLog.clientId AND BusinessClient.businessTag = WorkLog.businessTag "
			+ "AND WorkLog.startTime >= ? AND WorkLog.endTime <= ? "
			+ "WHERE BusinessClient.businessTag = ? "
			+ "GROUP BY BusinessClient.clientId "
			+ "ORDER BY hoursWorked DESC "
			+ "LIMIT ? OFFSET ?;";
		
		final PreparedStatement stmt = queryRunner.connection.prepareStatement(query);
		int columnIndex = 1;
		
		stmt.setLong(columnIndex++, startTimeRange);
		stmt.setLong(columnIndex++, endTimeRange);
		stmt.setString(columnIndex++, businessTag);
		stmt.setInt(columnIndex++, endRange - startRange);
		stmt.setInt(columnIndex++, startRange);
		
		queryRunner.resultSet = stmt.executeQuery();
		while (queryRunner.resultSet.next())
		{
			result.add(new Client(
					queryRunner.resultSet.getInt("clientId"),
					queryRunner.resultSet.getString("clientName"),
					queryRunner.resultSet.getString("businessTag"),
					queryRunner.resultSet.getInt("hoursWorked")));
		}
		
		return result;
	}
}
