/**
 * 
 */
package webresources;

import models.WorkLog;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.apache.log4j.Logger;

import database.DBWorkLogQueries;
import exceptions.BadKeyException;
import exceptions.DataAccessException;

/**
 * WorkLog resource to handle worklog processing.
 *  
 * @author Lawrence
 */
@Path("/business/{businessTag}/worklog")
public class WorkLogResource
{
    /** Logger **/
    private static final Logger LOGGER = Logger.getLogger(WorkLogResource.class);
    
	/**
	 * Getter for getting a worklog by its id.
	 * 
	 * @param businessTag the id of the business to add to.
	 * @param worklogid the id of the worklog to retrieve.
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("{workLogId : \\d+}")
	public WorkLog getWorkLog(@PathParam("businessTag") String businessTag,
			@PathParam("workLogId") int workLogId)
	{	
		WorkLog result = null;
		try 
		{
			result = new DBWorkLogQueries().getWorkLog(workLogId);
			
			if (result != null)
			{
				return result;
			}
			else
			{
				throw new WebApplicationException(Response.Status.NOT_FOUND);
			}
		}
		catch (DataAccessException e) 
		{
			throw new WebApplicationException(Response.Status.BAD_GATEWAY);		
		}
	}
	
	/**
	 * Getter for getting worklogs by user.
	 * 
	 * @param businessTag the id of the business to add to.
	 * @param username the user of the worklogs to retrieve.
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/user/{username}")
	public ArrayList<WorkLog> getWorkLog(@PathParam("businessTag") String businessTag,
			@PathParam("username") String username)
	{	
		ArrayList<WorkLog> result = new ArrayList<WorkLog>();
		try 
		{
			result = new DBWorkLogQueries().getWorkLogbyUser(username, businessTag);
			
			if (result != null)
			{
				return result;
			}
			else
			{
				throw new WebApplicationException(Response.Status.NOT_FOUND);
			}
		}
		catch (DataAccessException e) 
		{
			throw new WebApplicationException(Response.Status.BAD_GATEWAY);		
		}
	}
	
	/**
	 * Get all worklogs for an specific employee in a time range.
	 * 
	 * @param businessTag the id of the business to add to.
	 * @param userName the username of the employee.
	 * @param startTime the time to start the range from.
	 * @param endTime the time to end the range to.
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/user/{userName}/range/{startTime : \\d+}/{endTime : \\d+}")
	public List<WorkLog> getWorkLogInRangeForEmployee(@PathParam("businessTag") String businessTag,
			@PathParam("userName") String userName,
			@PathParam("startTime") long startTime,
			@PathParam("endTime") long endTime)
	{	
		List<WorkLog> result = new ArrayList<WorkLog>();
		try 
		{
			result = new DBWorkLogQueries().getAllWorkLogsForTimeRangeAndEmployee(userName, businessTag, startTime, endTime);
			
			if (result != null)
			{
				return result;
			}
			else
			{
				throw new WebApplicationException(Response.Status.NOT_FOUND);
			}
		}
		catch (DataAccessException e) 
		{
			throw new WebApplicationException(Response.Status.BAD_GATEWAY);		
		}
	}
	
	/**
	 * Get all worklogs for an specific employee in a time range.
	 * 
	 * @param businessTag the id of the business to add to.
	 * @param userName the username of the employee.
	 * @param startTime the time to start the range from.
	 * @param endTime the time to end the range to.
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/range/{startTime : \\d+}/{endTime : \\d+}")
	public List<WorkLog> getWorkLogInRange(@PathParam("businessTag") String businessTag,
			@PathParam("startTime") long startTime,
			@PathParam("endTime") long endTime)
	{	
		List<WorkLog> result = new ArrayList<WorkLog>();
		try 
		{
			result = new DBWorkLogQueries().getAllWorkLogsForTimeRange(businessTag, startTime, endTime);
			
			if (result != null)
			{
				return result;
			}
			else
			{
				throw new WebApplicationException(Response.Status.NOT_FOUND);
			}
		}
		catch (DataAccessException e) 
		{
			throw new WebApplicationException(Response.Status.BAD_GATEWAY);		
		}
	}
	
	/**
	 * Post method for creating a new worklog.
	 * 
	 * @param businessTag the id of the business to add to.
	 * @param worklog the worklog to add to the datastore.
	 */
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public String putWorkLog(@PathParam("businessTag") String businessTag,
			WorkLog workLog)
	{
		String message;

		try 
		{
			workLog.setBusinessTag(businessTag);
			
			if (workLog.isValid())
			{
				new DBWorkLogQueries().createWorkLog(workLog);
				
				message = "Successfully added worklog";
	            LOGGER.info(message);
				return message;
			}
			
			message = "Invalid worklog supplied";
            LOGGER.error(message);
            throw new WebApplicationException(message,
            		Response.status(Status.BAD_REQUEST).entity(message).build());
		}
		catch (final BadKeyException e)
		{
			message = "Client or employee does not correspond to existing records";
            LOGGER.error(message);
            throw new WebApplicationException(message, e,
            		Response.status(Status.BAD_REQUEST).entity(message).build());
		}
		catch (final DataAccessException e)
		{
			message = "No data store found";
            LOGGER.error(message, e);
            throw new WebApplicationException(message, e,
            		Response.status(Status.SERVICE_UNAVAILABLE).entity(message).build());
		}
	}
	
	/**
	 * Post method for creating a new worklog.
	 * 
	 * @param businessTag the id of the business to add to.
	 * @param worklogs - the worklogs to add to the datastore.
	 */
	@POST
	@Path("/import")
	@Consumes(MediaType.APPLICATION_JSON)
	public String putWorkLogs(@PathParam("businessTag") String businessTag,
			List<WorkLog> worklogs)
	{
		for (WorkLog worklog : worklogs)
		{
			try {
				putWorkLog(businessTag, worklog);
			}
			catch (WebApplicationException e)
			{
				LOGGER.info("Failed to add: " + worklog);
				LOGGER.info(e.getMessage());
			}
		}

		String returnMessage = "All worklogs created succesfully";
        LOGGER.info(returnMessage);
        return returnMessage;	
	}
}
