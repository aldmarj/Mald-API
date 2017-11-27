/**
 * 
 */
package webresources;

import models.WorkLog;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * WorkLog servlet to handle worklog processing.
 *  
 * @author Lawrence
 */
@Path("/business/{buisnessTag}/worklog")
public class WorkLogResource
{
	/**
	 * Getter for getting a worklog by its id.
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("{workLogId : \\d+}")
	public WorkLog getWorkLog(@PathParam("buisnessTag") String businessTag,
			@PathParam("workLogId") int workLogId)
	{	
		WorkLog workLog = new WorkLog();
		
		return workLog;
	}
	
	/**
	 * Post method for creating a new worklog.
	 */
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public Response putWorkLog(@PathParam("buisnessTag") String businessTag, 
			WorkLog workLog)
	{
		return Response.status(200).entity("").build();
	}
}
