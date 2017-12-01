/**
 * 
 */
package database;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import models.Employee;
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
	 * @throws NoDataStoreConnectionException
	 */
	public DBWorkLogQueries() throws NoDataStoreConnectionException
	{
		super();
	}
	
	/**
	 * Creates the given worklog in the datastore.
	 * 
	 * @param worklog - The worklog details to store.
	 * @throws BadKeyException - The key is not valid for the datastore.
	 * @throws NoDataStoreConnectionException - The datastore cannot be reached.
	 */
	public void createWorkLog(WorkLog workLog)
			throws BadKeyException, NoDataStoreConnectionException
	{
		try
		{			
			this.setAutoCommit(false);
			
			createWorkLogSQL(workLog, this);
	    	createLocationForWorklogSQL(workLog, this);

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
	    	this.closeConnection();
	    }
	}
	
	/**
	 * Returns all worklogs in a time range by an employee. 
	 * 
	 * @param employee - The employee of the worklogs to find.
	 * @param startTime - The time to start searching from.
	 * @param endTime - The time to end searching from.
	 * @return The requested worklog.
	 * @throws NoDataStoreConnectionException If a connection cannot be made to the store.
	 */
	public ArrayList<WorkLog> getAllWorkLogsForTimeRangeAndEmployee(Employee employee, Date startTime, Date endTime) 
			throws NoDataStoreConnectionException
	{
		ArrayList<WorkLog> result = null;
		
	    try
	    {		
	    	result = getAllWorkLogsForTimeRangeAndEmployeeSQL(employee, startTime, endTime, this);
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
	 * @throws NoDataStoreConnectionException If a connection cannot be made to the store.
	 */
	public WorkLog getWorkLog(int workLogId) throws NoDataStoreConnectionException
	{
		WorkLog result = null;
		
	    try
	    {		
	    	result = getWorkLogSQL(workLogId, this);
	    	
	    	// Set only the first location
	    	result.setLocation(
	    			new DBLocationQueries().getLocationsForId(
	    			new DBWorkLogQueries().getWorkLogLocationOwnerId(result))
	    			.iterator().next());
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
	
	public Integer getWorkLogLocationOwnerId(WorkLog workLog) throws NoDataStoreConnectionException
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
	 * @throws NoDataStoreConnectionException if the DB cannot be reached.
	 */
	public static void createWorkLogSQL(WorkLog workLog, DBQueries queryRunner) 
			throws SQLException, SQLIntegrityConstraintViolationException, NoDataStoreConnectionException
	{
		String query = "INSERT INTO WorkLog("
				+ "user, clientId, startTime, endTime, description, locationOwnerId) "
				+ "VALUES (?, ?, ?, ?, ?, ?);";
		
		final PreparedStatement stmt = 
				queryRunner.connection.prepareStatement(query);
		
		int index = 1;
		
		stmt.setString(index++, workLog.getUserName());
		stmt.setInt(index++, workLog.getClientId());
		stmt.setLong(index++, workLog.getStartTime());
		stmt.setLong(index++, workLog.getEndTime());
		stmt.setString(index++, workLog.getDescription());
		stmt.setInt(index++, new DBLocationQueries().createLocationOwner());
		
		stmt.executeUpdate();
	}

	/**
	 * Gets a WorkLog from the database with the given workLogId.
	 * 
	 * @param workLogId - the workLogId to search for.
	 * @param queryRunner - the DB query runner.
	 * @throws SQLException - if the DB cannot be reached.
	 * @throws NoDataStoreConnectionException - if the DB cannot be reached.
	 */
	public static WorkLog getWorkLogSQL(int workLogId, DBQueries queryRunner) 
			throws SQLException, NoDataStoreConnectionException
	{
		WorkLog result = null;
		
		String query = "SELECT user, clientId, startTime, endTime, description "
				+ "FROM WorkLog WHERE WorkLog.workLogId = ?;";
		
		final PreparedStatement stmt = queryRunner.connection.prepareStatement(query);
		stmt.setInt(1, workLogId);
		
		queryRunner.resultSet = stmt.executeQuery();
		while (queryRunner.resultSet.next())
		{
			result = new WorkLog(
					workLogId,
					queryRunner.resultSet.getString("user"),
					queryRunner.resultSet.getInt("clientId"),
					queryRunner.resultSet.getLong("startTime"),
					queryRunner.resultSet.getLong("endtime"),
					queryRunner.resultSet.getString("description"));
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
	 * @throws NoDataStoreConnectionException - if the DB cannot be reached.
	 */
	public static ArrayList<WorkLog> getAllWorkLogsForTimeRangeAndEmployeeSQL(
			Employee employee, Date startTime, Date endTime, DBQueries queryRunner) 
			throws NoDataStoreConnectionException, SQLException
	{
		ArrayList<WorkLog> result = new ArrayList<WorkLog>();
		
		String query = "SELECT workLogId, user, clientId, startTime, endTime, description "
				+ "FROM WorkLog WHERE startTime > ? AND endTime < ? AND user = ?;";
		
		final PreparedStatement stmt = queryRunner.connection.prepareStatement(query);
		int index = 1;
		
		stmt.setLong(index++, startTime.getTime());
		stmt.setLong(index++, endTime.getTime());
		stmt.setString(index++, employee.getUserName());

		queryRunner.resultSet = stmt.executeQuery();
		while (queryRunner.resultSet.next())
		{
			result.add(new WorkLog(
					queryRunner.resultSet.getInt("workLogId"),
					queryRunner.resultSet.getString("user"),
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
		
		stmt.setInt(index, worklog.getWorkLogId());
		
		ResultSet resultSet = stmt.executeQuery();
		
		if (resultSet.next()) 
		{
		    result = resultSet.getInt(1);
		}
		
		return result;
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
    	List<Integer> locationIds = DBLocationQueries.createLocationsSQL(Arrays.asList(worklog.getLocation()), queryRunner);
    	DBLocationQueries.createLocationOwnerToLocationsSQL(locationIds, locationOwnerId, queryRunner);
	}
}
