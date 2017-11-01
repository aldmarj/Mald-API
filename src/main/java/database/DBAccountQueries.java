/**
 * 
 */
package database;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;

import org.apache.log4j.Logger;

import models.Account;

/**
 * Class to contain helper functions for interaction with the database.
 *  
 * @author Lawrence
 */
public class DBAccountQueries extends DBQueries {

	/** Logger **/
	private final static Logger logger = Logger.getLogger(DBAccountQueries.class);
		
	public DBAccountQueries() throws NoDataStoreConnectionException
	{
		super();
	}

	/**
	 * Creates an account from the given account.
	 * 
	 * @param account the account details to create.
	 * @throws BadKeyException is the key is invalid.
	 * @throws NoDataStoreConnectionException if a connection cannot be made.
	 */
	public void createAccount(Account account)
			throws BadKeyException, NoDataStoreConnectionException
	{
		try
		{
			this.setAutoCommit(false);
			
			//createAccount(employee);
			createAccountSQL(account, this);
			
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
	    finally
	    {
	    	this.setAutoCommit(true);
	    	this.closeConnection();
	    }
	}
	
	/**
	 * Returns an account by its given userName. 
	 * Returns null if no employee is found.
	 * 
	 * @param userName - The userName of the employee to find.
	 * @return The requested account.
	 * @throws NoDataStoreConnectionException If a connection cannot be made to the store.
	 */
	public Account getAccount(String userName) throws NoDataStoreConnectionException
	{
		Account result = null;
		
	    try
	    {		
	    	result = getAccountSQL(userName, this);
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
	 * Creates an account in the database with the given parameters.
	 * 
	 * @param account - the tag of the new account.
	 * @param queryRunner - the name of the new account.
	 * @throws SQLIntegrityConstraintViolationException - If the given key is not unique.
	 * @throws SQLException - If a connection cannot be made to the store.
	 */
	public static void createAccountSQL(Account account, DBQueries queryRunner) 
			throws SQLException, SQLIntegrityConstraintViolationException
	{
		String query = "INSERT INTO Account("
				+ "userName, userPassword, email) "
				+ "VALUES (?, ?, ?);";
		
		final PreparedStatement stmt = queryRunner.connection.prepareStatement(query);
		
		int index = 1;
		
		stmt.setString(index++, account.getUserName());
		stmt.setString(index++, account.getStoredPassword());
		stmt.setString(index++, account.getEmail());
		
		stmt.executeUpdate();
	}
	
	/**
	 * Gets an account in the database with the given parameters.
	 * 
	 * @param userName the name of the user.
	 * @param queryRunner - the name of the new account.
	 * @return the account requested.
	 * @throws SQLIntegrityConstraintViolationException - If the given key is not unique.
	 * @throws SQLException - If a connection cannot be made to the store.
	 */
	public static Account getAccountSQL(String userName, DBQueries queryRunner) 
			throws SQLException, NoDataStoreConnectionException
	{
		Account result = null;
		
		String query = "SELECT userName, userPassword, email "
				+ "FROM Account WHERE Account.userName = ?;";
		
		final PreparedStatement stmt = queryRunner.connection.prepareStatement(query);
		stmt.setString(1, userName);
		
		queryRunner.resultSet = stmt.executeQuery();
		while (queryRunner.resultSet.next())
		{
			result = new Account(
					queryRunner.resultSet.getString("userName"),
					queryRunner.resultSet.getString("userPassword"),
					queryRunner.resultSet.getString("email"));
		}
		
		return result;
	}
}
