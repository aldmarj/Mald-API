/**
 * 
 */
package webresources;

import database.DBBusinessQueries;
import exceptions.BadKeyException;
import exceptions.DataAccessException;
import models.Business;
import models.Employee;
import models.Password;
import utilities.PasswordUtils;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.apache.log4j.Logger;

import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Lawrence
 * 
 * Business resource to handle business processing.
 *
 */
@Path("/business")
public class BusinessResource
{
    /** Logger **/
    private static final Logger LOGGER = Logger.getLogger(BusinessResource.class);
    
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
		catch (DataAccessException e) 
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
		catch (DataAccessException e) 
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
	public String postBusiness(Business business)
	{
		String returnMessage;
		
		try 
		{
			Employee employee = business.getDefaultEmployee();
			
			employee.setBusinessTag(business.getBusinessTag());

			// Check the requested password is okay
			if (employee.getRequestedPassword() != null 
					&& PasswordUtils.conformsToSecurityRules(employee.getRequestedPassword()))
			{
				// Generate the salted hash for the password
				employee.getAccount().setStoredPassword(
						Password.fromPlainText(employee.getRequestedPassword()));
				
				if (business.isValid())
				{
					new DBBusinessQueries().createBusiness(business);
					
					returnMessage = "Business created succesfully";
		            LOGGER.info(returnMessage);
		            return returnMessage;
				}
				{
					returnMessage = "Invalid business supplied";
				}
			}
			else
			{
				returnMessage = "Password needs at least 8 characters; one upper case, one lower case and a digit";
			}
			
            LOGGER.error(returnMessage);
            throw new WebApplicationException(returnMessage, 
            		Response.status(Status.BAD_REQUEST).entity(returnMessage).build());
		}
		catch (final BadKeyException e)
		{
			returnMessage = "Business with given businessTag already exists";
            LOGGER.error(returnMessage);
            throw new WebApplicationException(returnMessage, e, 
            		Response.status(Status.BAD_REQUEST).entity(returnMessage).build());
		}
		catch (final DataAccessException e)
		{
			returnMessage = "No data store found";
            LOGGER.error(returnMessage, e);
            throw new WebApplicationException(returnMessage, e, 
            		Response.status(Status.SERVICE_UNAVAILABLE).entity(returnMessage).build());
		} 
		catch (NoSuchAlgorithmException e) 
		{
            returnMessage = "Server could not authenticate password";
            LOGGER.error(returnMessage, e);
            throw new WebApplicationException(returnMessage, e, 
            		Response.status(Status.INTERNAL_SERVER_ERROR).entity(returnMessage).build());
		}
	}
	
	/**
	 * Create new businesses in the database with the given business objects.
	 */
	@POST
	@Path("/import")
	@Consumes(MediaType.APPLICATION_JSON)
	public String postBusinesses(List<Business> businesses)
	{
		for (Business business : businesses)
		{
			try {
				postBusiness(business);
			}
			catch (WebApplicationException e)
			{
				LOGGER.info("Failed to add: " + business);
				LOGGER.info(e.getMessage());
			}
		}

		String returnMessage = "All businesses created succesfully";
        LOGGER.info(returnMessage);
        return returnMessage;
    }
}
