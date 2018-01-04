/**
 * 
 */
package database;

import models.Employee;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.ArrayList;
import java.util.Collection;

import exceptions.BadKeyException;
import exceptions.DataAccessException;

/**
 * Class to contain manipulate the calling of functions for employees in the datastore.
 * Each instance should only be used once.
 * 
 * @author Lawrence
 */
public final class DBEmployeeQueries extends DBQueries 
{	
	
	/**
	 * CLASS CONSTRUCTOR
	 * 
	 * @throws DataAccessException
	 */
	public DBEmployeeQueries() throws DataAccessException
	{
		super();
	}

	/**
	 * Creates an employee in the datastore.
	 * 
	 * @param employee the employee details to add.
	 * @throws BadKeyException in the key given is invalid.
	 * @throws DataAccessException if a connection to the datastore cannot be made.
	 */
	public void createEmployeeAccount(Employee employee)
			throws BadKeyException, DataAccessException
	{
		try
		{
			this.setAutoCommit(false);
			
			// create the account first
			DBAccountQueries.createAccountSQL(employee.getAccount(), this);
			
			// create the employee
			createEmployeeSQL(employee, this);
			
			// Account and Employee should be created as a single transaction.
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
		catch (Exception e)
		{
			this.rollback();
		}
	    finally
	    {
	    	this.setAutoCommit(true);
	    	this.closeResultSet();
	    	this.closeConnection();
	    }
	}
	
	/**
	 * Returns an employee by its given userName. 
	 * Returns null if no employee is found.
	 * 
	 * @param userName - The userName of the employee to find.
	 * @param businessTag - The Business of the employee.
	 * @return The requested employee.
	 * @throws DataAccessException If a connection cannot be made to the store.
	 */
	public Employee getEmployee(String userName, String businessTag) throws DataAccessException
	{
		Employee result = null;
		
	    try
	    {		
	    	result = getEmployeeSQL(userName, businessTag, this);
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
	 * Returns all of the employees for a given business. 
	 * 
	 * @return All employees for a business.
	 * @throws DataAccessException If a connection cannot be made to the store.
	 */
	public Collection<Employee> getAllEmployees(String businessTag) throws DataAccessException
	{
		Collection<Employee> result = new ArrayList<Employee>();
		
	    try
	    {		
	    	result = getAllEmployeesSQL(businessTag, this);
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
	 * Returns a range of employees for a given business based of how much they have worked
	 * between two given times. 
	 * 
	 * @return the range of employees order by how much they have worked.
	 * @throws DataAccessException If a connection cannot be made to the store.
	 */
	public Collection<Employee> getAllEmployeesbyMostWorkedRangeBetweenTimes(String businessTag,
			int startRange, int endRange, long startTimeRange, long endTimeRange) 
					throws DataAccessException
	{
		Collection<Employee> result = new ArrayList<Employee>();
		
	    try
	    {		
	    	result = 
	    		getAllEmployeesbyMostWorkedRangeBetweenTimesSQL(
	    				businessTag, startRange, endRange, startTimeRange, endTimeRange, this);
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
	 * Creates a employee in the database with the given employee and DB runner.
	 * 
	 * @param employee - the employee to create in the database.
	 * @param queryRunner - the DB query runner.
	 * @throws SQLException if the DB cannot be reached.
	 * @throws SQLIntegrityConstraintViolationException if a key breaks the constraints of the DB.
	 */
	public static void createEmployeeSQL(Employee employee, DBQueries queryRunner) 
			throws SQLException, SQLIntegrityConstraintViolationException
	{
		String query = "INSERT INTO Employee("
				+ "userName, firstName, surName, businessTag, parentUser, jobRole) "
				+ "VALUES (?, ?, ?, ?, ?, ?);";
		
		final PreparedStatement stmt = queryRunner.connection.prepareStatement(query);
		
		int index = 1;
		
		stmt.setString(index++, employee.getUserName());
		stmt.setString(index++, employee.getFirstName());
		stmt.setString(index++, employee.getSurName());
		stmt.setString(index++, employee.getBusinessTag());
		stmt.setString(index++, employee.hasParent() ? employee.getParentUserName() : null);
		stmt.setString(index++, employee.getJobRole());
		
		stmt.executeUpdate();
	}
	
	/**
	 * Gets an employee from the database with the given userName and DB runner.
	 * 
	 * @param userName - the userName to search for in the database.
	 * @param queryRunner - the DB query runner.
	 * @throws SQLException if the DB cannot be reached.
	 * @throws DataAccessException if the DB cannot be reached.
	 */
	private static Employee getEmployeeSQL(String userName, String businessTag, DBQueries queryRunner) 
			throws SQLException, DataAccessException
	{
		Employee result = null;
		
		String query = "SELECT userName, firstName, surName, parentUser, jobRole "
				+ "FROM Employee WHERE Employee.userName = ? AND Employee.businessTag = ? "
				+ "ORDER BY userName ASC;";
		
		final PreparedStatement stmt = queryRunner.connection.prepareStatement(query);
		int i = 1;
		stmt.setString(i++, userName);
		stmt.setString(i++, businessTag);
		
		queryRunner.resultSet = stmt.executeQuery();
		while (queryRunner.resultSet.next())
		{
			result = new Employee(
					new DBAccountQueries().getAccount(userName, businessTag),
					queryRunner.resultSet.getString("firstName"),
					queryRunner.resultSet.getString("surName"),
					queryRunner.resultSet.getString("parentUser"),
					queryRunner.resultSet.getString("jobRole"));
		}
		
		return result;
	}
	
	/**
	 * Get employees from the database from a business within a range by most worked.
	 * 
	 * @param queryRunner - the DB query runner.
	 * @throws SQLException if the DB cannot be reached.
	 * @throws DataAccessException if the DB cannot be reached.
	 */
	public static ArrayList<Employee> getAllEmployeesSQL(String businessTag, DBQueries queryRunner) 
			throws DataAccessException, SQLException
	{
		ArrayList<Employee> result = new ArrayList<Employee>();
		
		String query = "SELECT userName, firstName, surName, businessTag, parentUser, jobRole "
				+ "FROM Employee WHERE Employee.businessTag = ? ORDER BY userName ASC;";
		
		final PreparedStatement stmt = queryRunner.connection.prepareStatement(query);
		stmt.setString(1, businessTag);
		
		queryRunner.resultSet = stmt.executeQuery();
		while (queryRunner.resultSet.next())
		{
			result.add(new Employee(
					new DBAccountQueries().getAccount(
							queryRunner.resultSet.getString("userName"),
							queryRunner.resultSet.getString("businessTag")),
					queryRunner.resultSet.getString("firstName"),
					queryRunner.resultSet.getString("surName"),
					queryRunner.resultSet.getString("parentUser"),
					queryRunner.resultSet.getString("jobRole")));
		}
		
		return result;
	}
	
	
	/**
	 * Get employees from the database from a business within a range given by most worked.
	 * 
	 * @param businessTag - The business to interrogate.
	 * @param startrange - The start of the range to return.
	 * @param endRange - The end of the range to return.
	 * @param queryRunner - the DB query runner.
	 * @throws SQLException if a key constraint is violated.
	 * @throws DataAccessException if the DB cannot be reached.
	 */
	private static Collection<Employee> getAllEmployeesbyMostWorkedRangeBetweenTimesSQL(
			String businessTag, int startRange, int endRange, long startTimeRange, long endTimeRange,
			DBQueries queryRunner) throws SQLException, DataAccessException 
	{
		Collection<Employee> result = new ArrayList<Employee>();
		
		String query = "SELECT Employee.userName, firstName, surName, Employee.businessTag, parentUser, jobRole, "
			+ "SUM(WorkLog.endTime - WorkLog.startTime) as hoursWorked "
			+ "FROM Employee "
			+ "LEFT JOIN WorkLog ON Employee.userName = WorkLog.userName AND Employee.businessTag = WorkLog.businessTag "
			+ "AND WorkLog.startTime >= ? AND WorkLog.endTime <= ? "
			+ "WHERE Employee.businessTag = ? "
			+ "GROUP BY Employee.userName "
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
			result.add(new Employee(
					new DBAccountQueries().getAccount(
							queryRunner.resultSet.getString("userName"),
							queryRunner.resultSet.getString("businessTag")),
					queryRunner.resultSet.getString("firstName"),
					queryRunner.resultSet.getString("surName"),
					queryRunner.resultSet.getString("parentUser"),
					queryRunner.resultSet.getString("jobRole"),
					queryRunner.resultSet.getInt("hoursWorked")));
		}
		
		return result;
	}
}
