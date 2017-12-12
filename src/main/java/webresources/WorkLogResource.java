/**
 * 
 */
package webresources;

import models.WorkLog;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import org.apache.log4j.Logger;

import database.BadKeyException;
import database.DBWorkLogQueries;
import database.NoDataStoreConnectionException;

/**
 * WorkLog servlet to handle worklog processing.
 *  
 * @author Lawrence
 */
@Path("/business/{buisnessTag}/worklog")
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
	public WorkLog getWorkLog(@PathParam("buisnessTag") String businessTag,
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
		catch (NoDataStoreConnectionException e) 
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
	public ArrayList<WorkLog> getWorkLog(@PathParam("buisnessTag") String businessTag,
			@PathParam("username") String username)
	{	
		ArrayList<WorkLog> result = new ArrayList<WorkLog>();
		try 
		{
			result = new DBWorkLogQueries().getWorkLogbyUser(username);
			
			if (result != null)
			{
				return result;
			}
			else
			{
				throw new WebApplicationException(Response.Status.NOT_FOUND);
			}
		}
		catch (NoDataStoreConnectionException e) 
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
	public List<WorkLog> getWorkLogInRangeForEmployee(@PathParam("buisnessTag") String businessTag,
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
		catch (NoDataStoreConnectionException e) 
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
	public List<WorkLog> getWorkLogInRange(@PathParam("buisnessTag") String businessTag,
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
		catch (NoDataStoreConnectionException e) 
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
	public String putWorkLog(@PathParam("buisnessTag") String businessTag,
			@Context SecurityContext securityContext,
			WorkLog workLog)
	{
		try 
		{
			workLog.setBusinessTag(businessTag);
			workLog.setUserName(securityContext.getUserPrincipal().getName());
			
			if (workLog.getStartTime() > workLog.getEndTime())
			{
				String message = "Start time needs to be before end time";
	            LOGGER.error(message);
	            throw new WebApplicationException(message, Response.Status.BAD_REQUEST);
			}
			
			new DBWorkLogQueries().createWorkLog(workLog);
			
			return "Successfully added";
		}
		catch (BadKeyException e)
		{
			String message = "Worklog of given id already exists";
            LOGGER.error(message);
            throw new WebApplicationException(message, e, Response.Status.BAD_REQUEST);
		}
		catch (NoDataStoreConnectionException e)
		{
			String message = "No data store found";
            LOGGER.error(message, e);
            throw new WebApplicationException(message, e, Response.Status.SERVICE_UNAVAILABLE);
		}
	}
}
