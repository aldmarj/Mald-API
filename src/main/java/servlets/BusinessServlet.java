/**
 * 
 */
package servlets;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import database.BadKeyException;
import database.DBBusinessQueries;
import database.NoDataStoreConnectionException;
import models.Business;

/**
 * @author Lawrence
 * 
 * Business servlet to handle business processing.
 *
 */
@Path("/business")
public class BusinessServlet
{
	/**
	 * Get a business from the API, will return the business for the given tag
	 * or all businesses if not tag is given.
	 * 
	 * @return the requested business.
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("{businessTag}")
	public Business getBusiness(@PathParam("businessTag") String businessTag) 
	{	   		
		try
		{
			Business result = DBBusinessQueries.getBusiness(businessTag);
			
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
	 * Create a new business in the database with the given business object.
	 */
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public Response putBusiness(Business business)
	{
		try 
		{
			DBBusinessQueries.createBusiness(business.getBusinessTag(), business.getBusinessName());
			
			return Response.status(200).entity("").build();
		}
		catch (BadKeyException e)
		{
			return Response.status(400).entity("Tag already exists").build();
		}
		catch (NoDataStoreConnectionException e)
		{
			return Response.status(400).entity("No data store found").build();
		}
	}
}