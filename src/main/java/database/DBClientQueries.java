/**
 * 
 */
package database;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.ArrayList;
import java.util.List;

import models.Client;

/**
 * Class to contain helper functions for interaction with the database.
 * Each instance should only be used once.
 * 
 * @author Lawrence
 */
public final class DBClientQueries extends DBQueries 
{
	
	/**
	 * CLASS CONSTRUCTOR
	 * 
	 * @throws NoDataStoreConnectionException
	 */
	public DBClientQueries() throws NoDataStoreConnectionException
	{
		super();
	}
	
	/**
	 * Creates a client in the database with the given name.
	 * 
	 * @param clientName - the tag of the new client.
	 * @param businessTag - the name of the owning business.
	 * @throws BadKeyException - If the given key is invalid.
	 * @throws NoDataStoreConnectionException - If a connection cannot be made to the store.
	 */
	public void createClient(Client client) 
			throws BadKeyException, NoDataStoreConnectionException
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
	 * @throws NoDataStoreConnectionException If a connection cannot be made to the store.
	 */
	public Client getClient(int clientId) throws NoDataStoreConnectionException
	{
		Client result = null;
		
	    try
	    {
	    	result = getClientSQL(clientId, this);
	    	
	    	result.setLocations(
	    			new DBLocationQueries().getLocationsForId(
	    			new DBClientQueries().getClientLocationOwnerId(result)));
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
	
	public Integer getClientLocationOwnerId(Client client) throws NoDataStoreConnectionException
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
	 * @throws NoDataStoreConnectionException If a connection cannot be made to the store.
	 */
	public ArrayList<Client> getAllClients() throws NoDataStoreConnectionException
	{
		ArrayList<Client> result = new ArrayList<Client>();
		
	    try
	    {		
	    	result = getAllClientsSQL(this);
	    	
	    	for (Client client : result)
	    	{
	    		client.setLocations(
		    			new DBLocationQueries().getLocationsForId(
		    	    	new DBClientQueries().getClientLocationOwnerId(client)));
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
	 * @throws NoDataStoreConnectionException if the DB cannot be reached.
	 */
	public static void createClientSQL(Client client, DBQueries queryRunner) 
			throws SQLException, SQLIntegrityConstraintViolationException, NoDataStoreConnectionException
	{
		String query = "INSERT INTO BusinessClient(clientName, businessTag, locationOwnerId) VALUES (?, ?, ?);";
		
		final PreparedStatement stmt = queryRunner.connection.prepareStatement(query);
		int index = 1;
		
		stmt.setString(index++, client.getClientName());
		stmt.setString(index++, client.getBusinessTag());
		stmt.setInt(index++, new DBLocationQueries().createLocationOwner());
		
		stmt.executeUpdate();
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
    	List<Integer> locationIds = DBLocationQueries.createLocationsSQL(client.getLocations(), queryRunner);
    	DBLocationQueries.createLocationOwnerToLocationsSQL(locationIds, locationOwnerId, queryRunner);
	}
	
	/**
	 * Gets a client from the database with the given clientId and DB runner.
	 * 
	 * @param client id - the client id to search for in the database.
	 * @param queryRunner - the DB query runner.
	 * @return the client requested.
	 * @throws SQLException if the DB cannot be reached.
	 * @throws NoDataStoreConnectionException if the DB cannot be reached.
	 */
	public static Client getClientSQL(int clientId, DBQueries queryRunner)
			throws SQLException, NoDataStoreConnectionException
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
	 * @param queryRunner - the DB query runner.
	 * @return All clients. 
	 * @throws SQLException if the DB cannot be reached.
	 * @throws NoDataStoreConnectionException if the DB cannot be reached.
	 */
	public static ArrayList<Client> getAllClientsSQL(DBQueries queryRunner)
			throws SQLException, NoDataStoreConnectionException
	{
		ArrayList<Client> result = new ArrayList<Client>();

		String query = "SELECT clientId, clientName, businessTag FROM BusinessClient;";
		
		final PreparedStatement stmt = queryRunner.connection.prepareStatement(query);
		
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
}
