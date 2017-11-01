/**
 * 
 */
package database;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.ArrayList;

import org.apache.log4j.Logger;

import models.Employee;

/**
 * Class to contain helper functions for interaction with the database.
 * 
 * @author Lawrence
 */
public final class DBEmployeeQueries extends DBQueries {
	
	/** Logger **/
	private final static Logger logger = Logger.getLogger(DBEmployeeQueries.class);
		
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
			this.createEmployeeSQL(employee);
			
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
	 * Creates a employee in the database with the given parameters.
	 * 
	 * @param employee - the details of the employee to add.
	 * @throws SQLIntegrityConstraintViolationException - If the given key is not unique.
	 * @throws SQLException - If a connection cannot be made to the store.
	 */
	private void createEmployeeSQL(Employee employee) 
			throws SQLException, SQLIntegrityConstraintViolationException
	{
		String query = "INSERT INTO Employee("
				+ "userName, firstName, surName, businessTag, parentUser, jobRole) "
				+ "VALUES (?, ?, ?, ?, ?, ?);";
		
		final PreparedStatement stmt = connection.prepareStatement(query);
		
		int index = 1;
		
		stmt.setString(index++, employee.getUserName());
		stmt.setString(index++, employee.getFirstName());
		stmt.setString(index++, employee.getSurName());
		stmt.setString(index++, employee.getBusiness().getBusinessTag());
		stmt.setString(index++, employee.hasParent() ? employee.getParent().getUserName() : null);
		stmt.setString(index++, employee.getJobRole());
		
		stmt.executeUpdate();
	}
	
	/**
	 * Returns an employee by its given userName. 
	 * Returns null if no employee is found.
	 * 
	 * @param userName - The userName of the employee to find.
	 * @return The requested employee.
	 * @throws NoDataStoreConnectionException If a connection cannot be made to the store.
	 */
	public Employee getEmployee(String userName) throws NoDataStoreConnectionException
	{
		Employee result = null;
		
	    try
	    {		
	    	result = this.getEmployeeSQL(userName);
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
	 * The SQL command for the getting an employee.
	 * 
	 * @param userName the name of the employee to return.
	 * @return the requested employee.
	 * @throws SQLException If a connection cannot be made to the store.
	 * @throws NoDataStoreConnectionException If a connection cannot be made to the store.
	 */
	private Employee getEmployeeSQL(String userName) throws SQLException, NoDataStoreConnectionException
	{
		Employee result = null;
		
		String query = "SELECT firstName, surName, businessTag, parentUser, jobRole "
				+ "FROM Employee WHERE Employee.userName = ?;";
		
		final PreparedStatement stmt = this.connection.prepareStatement(query);
		stmt.setString(1, userName);
		
		this.resultSet = stmt.executeQuery();
		while (resultSet.next())
		{
			result = new Employee(
					new DBAccountQueries().getAccount(userName),
					this.resultSet.getString("firstName"),
					this.resultSet.getString("surName"),
					DBBusinessQueries.getBusiness(this.resultSet.getString("businessTag")),
					new DBEmployeeQueries().getEmployee(this.resultSet.getString("parentUser")),
					this.resultSet.getString("jobRole"));
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
	    	result = this.getAllEmployeesSQL();
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
	 * The SQL command for returning all employees.
	 * 
	 * @return a list of all employees.
	 * @throws SQLException If a connection cannot be made to the store.
	 * @throws NoDataStoreConnectionException If a connection cannot be made to the store.
	 */
	public ArrayList<Employee> getAllEmployeesSQL() throws NoDataStoreConnectionException, SQLException
	{
		ArrayList<Employee> result = new ArrayList<Employee>();
		
		String query = "SELECT userName, firstName, surName, businessTag, parentUser, jobRole "
				+ "FROM Employee";
		
		final PreparedStatement stmt = this.connection.prepareStatement(query);
		
		this.resultSet = stmt.executeQuery();
		while (resultSet.next())
		{
			result.add(new Employee(
					new DBAccountQueries().getAccount(this.resultSet.getString("userName")),
					this.resultSet.getString("firstName"),
					this.resultSet.getString("surName"),
					DBBusinessQueries.getBusiness(this.resultSet.getString("businessTag")),
					new DBEmployeeQueries().getEmployee(this.resultSet.getString("parentUser")),
					this.resultSet.getString("jobRole")));
		}
		
		return result;
	}
}
