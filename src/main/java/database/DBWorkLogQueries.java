/**
 * 
 */
package database;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import exceptions.BadKeyException;
import exceptions.DataAccessException;
import models.Location;
import models.WorkLog;

/**
 * Class to contain manipulation functions for worklogs in the datastore.
 * Each instance should only be used once. 
 * 
 * @author Lawrence
 */
public class DBWorkLogQueries extends DBQueries {
	
	/**
	 * CLASS CONSTRUCTOR
	 * 
	 * @throws DataAccessException
	 */
	public DBWorkLogQueries() throws DataAccessException
	{
		super();
	}
	
	/**
	 * Creates the given worklog in the datastore.
	 * 
	 * @param worklog - The worklog details to store.
	 * @throws BadKeyException - The key is not valid for the datastore.
	 * @throws DataAccessException - The datastore cannot be reached.
	 */
	public void createWorkLog(WorkLog workLog)
			throws BadKeyException, DataAccessException
	{
		try
		{			
			this.setAutoCommit(false);
			
			createWorkLogSQL(workLog, this);
			
			if (workLog.getLocation() != null)
			{
				createLocationForWorklogSQL(workLog, this);
			}

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
	 * Returns all worklogs in a time range by an employee. 
	 * 
	 * @param businessTag - The business to find the worklogs for.
	 * @param employee - The employee of the worklogs to find.
	 * @param startTime - The time to start searching from.
	 * @param endTime - The time to end searching from.
	 * @return The requested worklog.
	 * @throws DataAccessException If a connection cannot be made to the store.
	 */
	public ArrayList<WorkLog> getAllWorkLogsForTimeRangeAndEmployee(String userName, String businessTag, long startTime, long endTime) 
			throws DataAccessException
	{
		ArrayList<WorkLog> result = new ArrayList<WorkLog>();
		
	    try
	    {		
	    	result = getAllWorkLogsForTimeRangeAndEmployeeSQL(userName, businessTag, startTime, endTime, this);
	    	
	    	for (WorkLog workLog : result)
	    	{
	    		Iterator<Location> location = DBLocationQueries.getLocationsForIdSQL(
		    			DBWorkLogQueries.getWorkLogLocationOwnerIdSQL(workLog, this), this)
		    			.iterator();
	    		
	    		if (location.hasNext())
	    		{
	    			workLog.setLocation(location.next());
	    		}
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
	 * Returns all worklogs in a time range. 
	 * 
	 * @param businessTag - The business to find the worklogs for.
	 * @param startTime - The time to start searching from.
	 * @param endTime - The time to end searching from.
	 * @return The requested worklog.
	 * @throws DataAccessException If a connection cannot be made to the store.
	 */
	public ArrayList<WorkLog> getAllWorkLogsForTimeRange(String businessTag, long startTime, long endTime) 
			throws DataAccessException
	{
		ArrayList<WorkLog> result = new ArrayList<WorkLog>();
		
	    try
	    {		
	    	result = getAllWorkLogsForTimeRangeSQL(businessTag, startTime, endTime, this);
	    	
	    	for (WorkLog workLog : result)
	    	{
	    		Iterator<Location> location = DBLocationQueries.getLocationsForIdSQL(
		    			DBWorkLogQueries.getWorkLogLocationOwnerIdSQL(workLog, this), this)
		    			.iterator();
	    		
	    		if (location.hasNext())
	    		{
	    			workLog.setLocation(location.next());
	    		}
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
	 * Returns a worklog by its given id. 
	 * Returns null if no worklog is found.
	 * 
	 * @param worklogId - The workLogId of the worklog to find.
	 * @return The requested worklog.
	 * @throws DataAccessException If a connection cannot be made to the store.
	 */
	public WorkLog getWorkLog(int workLogId) throws DataAccessException
	{
		WorkLog result = null;
		
	    try
	    {		
	    	result = getWorkLogSQL(workLogId, this);
	    	
	    	if (result != null)
	    	{
	    		Iterator<Location> location = DBLocationQueries.getLocationsForIdSQL(
		    			DBWorkLogQueries.getWorkLogLocationOwnerIdSQL(result, this), this)
		    			.iterator();
	    		
	    		if (location.hasNext())
	    		{
	    			result.setLocation(location.next());
	    		}
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
	 * Returns a worklog by a given user. 
	 * 
	 * @param username - The user of the worklogs to find.
	 * @return The requested worklogs.
	 * @throws DataAccessException If a connection cannot be made to the store.
	 */
	public ArrayList<WorkLog> getWorkLogbyUser(String username) throws DataAccessException
	{
		ArrayList<WorkLog> result = new ArrayList<WorkLog>();
		
	    try
	    {		
	    	result = getWorkLogbyUserSQL(username, this);
	    	
	    	for (WorkLog workLog : result)
	    	{
	    		Iterator<Location> location = DBLocationQueries.getLocationsForIdSQL(
		    			DBWorkLogQueries.getWorkLogLocationOwnerIdSQL(workLog, this), this)
		    			.iterator();
	    		
	    		if (location.hasNext())
	    		{
	    			workLog.setLocation(location.next());
	    		}
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
	 * Get the owner location for the given worklog.
	 * 
	 * @param workLog the worklog to find the id for.
	 * @return the owner id of the worklog.
	 * @throws DataAccessException If a connection cannot be made to the store.
	 */
	public Integer getWorkLogLocationOwnerId(WorkLog workLog) throws DataAccessException
	{
		Integer result = null;
		
	    try
	    {		
	    	result = getWorkLogLocationOwnerIdSQL(workLog, this);
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
	 * Creates a worklog in the database with the given parameters.
	 * 
	 * @param workLog - the worklog details to create.
	 * @param queryRunner - the DB query runner.
	 * @throws SQLException if the DB cannot be reached.
	 * @throws SQLIntegrityConstraintViolationException if a key breaks the constraints of the DB.
	 * @throws DataAccessException if the DB cannot be reached.
	 * @throws BadKeyException 
	 */
	public static void createWorkLogSQL(WorkLog workLog, DBQueries queryRunner) 
			throws SQLException, SQLIntegrityConstraintViolationException, DataAccessException, BadKeyException
	{
		int locationOwnerId = DBLocationQueries.createLocationOwnerSQL(queryRunner);
				
		String query = "INSERT INTO WorkLog("
				+ "userName, businessTag, clientId, startTime, endTime, description, locationOwnerId) "
				+ "VALUES (?, ?, ?, ?, ?, ?, ?);";
		
		final PreparedStatement stmt = 
				queryRunner.connection.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS);
		
		int index = 1;
		
		stmt.setString(index++, workLog.getUserName());
		stmt.setString(index++, workLog.getBusinessTag());
		stmt.setInt(index++, workLog.getClientId());
		stmt.setLong(index++, workLog.getStartTime());
		stmt.setLong(index++, workLog.getEndTime());
		stmt.setString(index++, workLog.getDescription());
		stmt.setInt(index++, locationOwnerId);
		
		stmt.executeUpdate();

		ResultSet resultSet = stmt.getGeneratedKeys();

		if (resultSet.next()) 
		{
		    workLog.setWorkLogId(resultSet.getInt(1));
		}	
	}
	
	/**
	 * Creates the locations for the given client in the database.
	 * 
	 * @throws SQLException - If the given key is invalid.
	 * @throws SQLIntegrityConstraintViolationException - If a connection cannot be made to the store.
	 */
	public static void createLocationForWorklogSQL(WorkLog worklog, DBQueries queryRunner)
			throws SQLException, SQLIntegrityConstraintViolationException
	{
		// First find the owner if of the client
    	Integer locationOwnerId = getWorkLogLocationOwnerIdSQL(worklog, queryRunner);
    	
    	// Then create the locations for this id
    	List<Integer> locationIds = 
    			DBLocationQueries.createLocationsSQL(Collections.singleton(worklog.getLocation()), queryRunner);
    	DBLocationQueries.createLocationOwnerToLocationsSQL(locationIds, locationOwnerId, queryRunner);
	}

	/**
	 * Gets a WorkLog from the database with the given workLogId.
	 * 
	 * @param workLogId - the workLogId to search for.
	 * @param queryRunner - the DB query runner.
	 * @throws SQLException - if the DB cannot be reached.
	 * @throws DataAccessException - if the DB cannot be reached.
	 */
	public static WorkLog getWorkLogSQL(int workLogId, DBQueries queryRunner) 
			throws SQLException, DataAccessException
	{
		WorkLog result = null;
		
		String query = "SELECT userName, businessTag, clientId, startTime, endTime, description "
				+ "FROM WorkLog WHERE WorkLog.workLogId = ?;";
		
		final PreparedStatement stmt = queryRunner.connection.prepareStatement(query);
		int index = 1;
		
		stmt.setInt(index++, workLogId);
		
		queryRunner.resultSet = stmt.executeQuery();
		while (queryRunner.resultSet.next())
		{
			result = new WorkLog(
					workLogId,
					queryRunner.resultSet.getString("userName"),
					queryRunner.resultSet.getString("businessTag"),
					queryRunner.resultSet.getInt("clientId"),
					queryRunner.resultSet.getLong("startTime"),
					queryRunner.resultSet.getLong("endtime"),
					queryRunner.resultSet.getString("description"));
		}
		
		return result;
	}
	
	/**
	 * Gets a WorkLog from the database with the given workLog user.
	 * 
	 * @param workLogUser - the user to search for.
	 * @param queryRunner - the DB query runner.
	 * @throws SQLException - if the DB cannot be reached.
	 * @throws DataAccessException - if the DB cannot be reached.
	 */
	public static ArrayList<WorkLog> getWorkLogbyUserSQL(String username, DBQueries queryRunner) 
			throws SQLException, DataAccessException
	{
		ArrayList<WorkLog> result = new ArrayList<WorkLog>();
		
		String query = "SELECT workLogId, userName, businessTag, clientId, startTime, endTime, description "
				+ "FROM WorkLog WHERE WorkLog.userName = ? ORDER BY startTime DESC;";
		
		final PreparedStatement stmt = queryRunner.connection.prepareStatement(query);
		int index = 1;
		
		stmt.setString(index++, username);
		
		queryRunner.resultSet = stmt.executeQuery();
		while (queryRunner.resultSet.next())
		{
			result.add(new WorkLog(
					queryRunner.resultSet.getInt("workLogId"),
					username,
					queryRunner.resultSet.getString("businessTag"),
					queryRunner.resultSet.getInt("clientId"),
					queryRunner.resultSet.getLong("startTime"),
					queryRunner.resultSet.getLong("endtime"),
					queryRunner.resultSet.getString("description")));
		}
		
		return result;
	}
	
	/**
	 * Gets all worklogs for the given time range and employee.
	 * 
	 * @param employee - the given employee to check.
	 * @param startTime - the given startTime to work from.
	 * @param endTime - the given endTime to work from.
	 * @param queryRunner - the DB query runner.
	 * @throws SQLException - if the DB cannot be reached.
	 * @throws DataAccessException - if the DB cannot be reached.
	 */
	public static ArrayList<WorkLog> getAllWorkLogsForTimeRangeAndEmployeeSQL(
			String userName, String businessTag, long startTime, long endTime, DBQueries queryRunner) 
			throws DataAccessException, SQLException
	{
		ArrayList<WorkLog> result = new ArrayList<WorkLog>();
		
		String query = "SELECT workLogId, userName, businessTag, clientId, startTime, endTime, description "
				+ "FROM WorkLog WHERE startTime >= ? AND endTime <= ? AND userName = ? AND businessTag = ? "
				+ "ORDER BY startTime DESC;";
		
		final PreparedStatement stmt = queryRunner.connection.prepareStatement(query);
		int index = 1;
		
		stmt.setLong(index++, startTime);
		stmt.setLong(index++, endTime);
		stmt.setString(index++, userName);
		stmt.setString(index++, businessTag);

		queryRunner.resultSet = stmt.executeQuery();
		while (queryRunner.resultSet.next())
		{
			result.add(new WorkLog(
					queryRunner.resultSet.getInt("workLogId"),
					queryRunner.resultSet.getString("userName"),
					queryRunner.resultSet.getString("businessTag"),
					queryRunner.resultSet.getInt("clientId"),
					queryRunner.resultSet.getLong("startTime"),
					queryRunner.resultSet.getLong("endtime"),
					queryRunner.resultSet.getString("description")));
		}
		
		return result;
	}
	
	/**
	 * Gets all worklogs for the given time range.
	 * 
	 * @param businessTag - the given business to check.
	 * @param startTime - the given startTime to work from.
	 * @param endTime - the given endTime to work from.
	 * @param queryRunner - the DB query runner.
	 * @throws SQLException - if the DB cannot be reached.
	 * @throws DataAccessException - if the DB cannot be reached.
	 */
	public static ArrayList<WorkLog> getAllWorkLogsForTimeRangeSQL(
			String businessTag, long startTime, long endTime, DBQueries queryRunner) 
			throws DataAccessException, SQLException
	{
		ArrayList<WorkLog> result = new ArrayList<WorkLog>();
		
		String query = "SELECT workLogId, userName, businessTag, clientId, startTime, endTime, description "
				+ "FROM WorkLog WHERE startTime >= ? AND endTime <= ? AND businessTag = ? ORDER BY startTime DESC;";
		
		final PreparedStatement stmt = queryRunner.connection.prepareStatement(query);
		int index = 1;
		
		stmt.setLong(index++, startTime);
		stmt.setLong(index++, endTime);
		stmt.setString(index++, businessTag);

		queryRunner.resultSet = stmt.executeQuery();
		while (queryRunner.resultSet.next())
		{
			result.add(new WorkLog(
					queryRunner.resultSet.getInt("workLogId"),
					queryRunner.resultSet.getString("userName"),
					queryRunner.resultSet.getString("businessTag"),
					queryRunner.resultSet.getInt("clientId"),
					queryRunner.resultSet.getLong("startTime"),
					queryRunner.resultSet.getLong("endtime"),
					queryRunner.resultSet.getString("description")));
		}
		
		return result;
	}
	
	/**
	 * Get the location owner id from the given worklog.
	 * 
	 * @param client - the client to interrogate.
	 * @param queryRunner - the DB query runner.
	 * @throws SQLException if the DB cannot be reached.
	 * @throws SQLIntegrityConstraintViolationException if a key breaks the constraints of the DB.
	 */
	public static Integer getWorkLogLocationOwnerIdSQL(WorkLog worklog, DBQueries queryRunner) 
			throws SQLException, SQLIntegrityConstraintViolationException
	{
		Integer result = null;
		
		String query = "SELECT locationOwnerId FROM WorkLog WHERE WorkLog.workLogId = ?;";
		
		final PreparedStatement stmt = queryRunner.connection.prepareStatement(query);
		int index = 1;
		
		stmt.setInt(index++, worklog.getWorkLogId());
		
		ResultSet resultSet = stmt.executeQuery();
		
		if (resultSet.next()) 
		{
		    result = resultSet.getInt(1);
		}
		
		return result;
	}
}
