/**
 * 
 */
package database;

import models.Employee;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.ArrayList;

/**
 * Class to contain manipulation functions for employees in the datastore.
 * Each instance should only be used once.
 * 
 * @author Lawrence
 */
public final class DBEmployeeQueries extends DBQueries 
{	
	
	/**
	 * CLASS CONSTRUCTOR
	 * 
	 * @throws NoDataStoreConnectionException
	 */
	public DBEmployeeQueries() throws NoDataStoreConnectionException
	{
		super();
	}

	/**
	 * Creates an employee in the datastore.
	 * 
	 * @param employee the employee details to add.
	 * @throws BadKeyException in the key given is invalid.
	 * @throws NoDataStoreConnectionException if a connection to the datastore cannot be made.
	 */
	public void createEmployeeAccount(Employee employee)
			throws BadKeyException, NoDataStoreConnectionException
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
	 * @throws NoDataStoreConnectionException If a connection cannot be made to the store.
	 */
	public Employee getEmployee(String userName, String businessTag) throws NoDataStoreConnectionException
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
	 * Returns all of the employees. 
	 * 
	 * @return All employees.
	 * @throws NoDataStoreConnectionException If a connection cannot be made to the store.
	 */
	public ArrayList<Employee> getAllEmployee() throws NoDataStoreConnectionException
	{
		ArrayList<Employee> result = new ArrayList<Employee>();
		
	    try
	    {		
	    	result = getAllEmployeesSQL(this);
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
	private static void createEmployeeSQL(Employee employee, DBQueries queryRunner) 
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
		stmt.setString(index++, employee.hasParent() ? employee.getUserName() : null);
		stmt.setString(index++, employee.getJobRole());
		
		stmt.executeUpdate();
	}
	
	/**
	 * Gets an employee from the database with the given userName and DB runner.
	 * 
	 * @param userName - the userName to search for in the database.
	 * @param queryRunner - the DB query runner.
	 * @throws SQLException if the DB cannot be reached.
	 * @throws NoDataStoreConnectionException if the DB cannot be reached.
	 */
	private static Employee getEmployeeSQL(String userName, String businessTag, DBQueries queryRunner) 
			throws SQLException, NoDataStoreConnectionException
	{
		Employee result = null;
		
		String query = "SELECT firstName, surName, parentUser, jobRole "
				+ "FROM Employee WHERE Employee.userName = ? AND Employee.businessTag = ?;";
		
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
	 * Gets all employees from the database.
	 * 
	 * @param queryRunner - the DB query runner.
	 * @throws SQLException if the DB cannot be reached.
	 * @throws NoDataStoreConnectionException if the DB cannot be reached.
	 */
	public static ArrayList<Employee> getAllEmployeesSQL(DBQueries queryRunner) 
			throws NoDataStoreConnectionException, SQLException
	{
		ArrayList<Employee> result = new ArrayList<Employee>();
		
		String query = "SELECT userName, firstName, surName, businessTag, parentUser, jobRole "
				+ "FROM Employee";
		
		final PreparedStatement stmt = queryRunner.connection.prepareStatement(query);
		
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
}
