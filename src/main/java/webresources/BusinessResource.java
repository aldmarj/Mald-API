/**
 * 
 */
package webresources;

import database.DBBusinessQueries;
import exceptions.BadKeyException;
import exceptions.NoDataStoreConnectionException;
import models.Business;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;

/**
 * @author Lawrence
 * 
 * Business servlet to handle business processing.
 *
 */
@Path("/business")
public class BusinessResource
{
	/**
	 * Get a business from the API, will return the business for the given tag
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
			Business result = new DBBusinessQueries().getBusiness(businessTag);
			
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
	 * Returns all businesses if no tag is given.
	 * 
	 * @return the requested business.
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public ArrayList<Business> getBusinesss() 
	{	
		ArrayList<Business> result = new ArrayList<Business>();
		
		try
		{
			result = new DBBusinessQueries().getAllBusinesses();
		}
		catch (NoDataStoreConnectionException e) 
		{
			throw new WebApplicationException(Response.Status.BAD_GATEWAY);		
		}
		
		return result;
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
			new DBBusinessQueries().createBusiness(business);
			
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
