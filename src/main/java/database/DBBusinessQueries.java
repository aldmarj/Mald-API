/**
 * 
 */
package database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.log4j.Logger;

import com.mysql.jdbc.Statement;

/**
 * Class to contain helper functions for interaction with the database.
 * 
 * @author Lawrence
 */
public abstract class DBBusinessQueries {
	
	/** Logger **/
	private final static Logger logger = Logger.getLogger(DBBusinessQueries.class);

	/**
	 * Creates a business in the database with the given name and tag.
	 * Will return a -1 for an unsuccessful result.
	 * 
	 * @param businessName - the name of the new business.
	 * @param businessTag - the tag of the new business.
	 * @return - the id of the new business.
	 */
	public static int createBusiness(String businessName, String businessTag)
	{
		int businessID = -1;
		
		Connection connection = null;
		ResultSet resultSet = null;
	    try 
	    {
	    	connection = DatabasePool.getConnection();
			
			String query = "INSERT INTO Business(businessName, businessTag) VALUES (?, ?);";
			
			final PreparedStatement stmt = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
			stmt.setString(1, businessName);
			stmt.setString(2, businessTag);
			
			stmt.executeUpdate();
			
			resultSet = stmt.getGeneratedKeys();
			while (resultSet.next())
			{
				businessID = resultSet.getInt(1);
			}
		} 
	    catch (SQLException e) 
	    {
			logger.error("Failed to create new business");
		}
	    finally
	    {
			try 
			{
				connection.close();
				resultSet.close();
			} 
			catch (SQLException e) 
			{
				logger.error("Failed to close SQL resources");
			}
	    }
	    
		return businessID;
	}
}