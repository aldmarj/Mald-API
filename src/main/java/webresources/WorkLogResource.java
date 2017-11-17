/**
 * 
 */
package webresources;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import models.WorkLog;

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
