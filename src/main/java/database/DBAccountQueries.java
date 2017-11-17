/**
 * 
 */
package database;

import models.users.Account;
import models.users.Password;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;

/**
 * Class to contain manipulation functions for accounts in the datastore.
 * Each instance should only be used once. 
 * 
 * @author Lawrence
 */
public class DBAccountQueries extends DBQueries {
	
	/**
	 * CLASS CONSTRUCTOR
	 * 
	 * @throws NoDataStoreConnectionException
	 */
	public DBAccountQueries() throws NoDataStoreConnectionException
	{
		super();
	}

	/**
	 * Creates the given account in the datastore.
	 * 
	 * @param account - The account details to store.
	 * @throws BadKeyException - The key is not valid for the datastore.
	 * @throws NoDataStoreConnectionException - The datastore cannot be reached.
	 */
	public void createAccount(Account account)
			throws BadKeyException, NoDataStoreConnectionException
	{
		try
		{    				
			createAccountSQL(account, this);
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
	    	this.closeConnection();
	    }
	}
	
	/**
	 * Returns an account by its given userName. 
	 * Returns null if no account is found.
	 * 
	 * @param userName - The userName of the account to find.
	 * @param businessTag - The business the account to belongs to.
	 * @return The requested account.
	 * @throws NoDataStoreConnectionException If a connection cannot be made to the store.
	 */
	public Account getAccount(String userName, String businessTag) throws NoDataStoreConnectionException
	{
		Account result = null;
		
	    try
	    {		
	    	result = getAccountSQL(userName, businessTag, this);
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
				+ "userName, userPassword, businessTag, email) "
				+ "VALUES (?, ?, ?, ?);";
		
		final PreparedStatement stmt = queryRunner.connection.prepareStatement(query);
		
		int index = 1;
		
		stmt.setString(index++, account.getUserName());
		stmt.setString(index++, account.getStoredPassword());
		stmt.setString(index++, account.getBusinessTag());
		stmt.setString(index++, account.getEmail());
		
		stmt.executeUpdate();
	}
	
	/**
	 * Gets an account in the database with the given parameters.
	 * 
	 * @param userName the name of the user.
	 * @param businessTag the business of the user.
	 * @param queryRunner - the name of the new account.
	 * @return the account requested.
	 * @throws NoDataStoreConnectionException - If a connection cannot be made to the store.
	 * @throws SQLException - If a connection cannot be made to the store.
	 */
	public static Account getAccountSQL(String userName, String businessTag, DBQueries queryRunner)
			throws SQLException, NoDataStoreConnectionException
	{
		Account result = null;
		
		String query = "SELECT userName, userPassword, businessTag, email "
				+ "FROM Account WHERE Account.userName = ? AND Account.businessTag = ?;";
		
		final PreparedStatement stmt = queryRunner.connection.prepareStatement(query);

		int i = 1;
		stmt.setString(i++, userName);
		stmt.setString(i++, businessTag);
		
		queryRunner.resultSet = stmt.executeQuery();
		while (queryRunner.resultSet.next())
		{
			result = new Account(
					queryRunner.resultSet.getString("userName"),
					queryRunner.resultSet.getString("userPassword"),
					queryRunner.resultSet.getString("businessTag"),
					queryRunner.resultSet.getString("email"));
		}
		
		return result;
	}
}
