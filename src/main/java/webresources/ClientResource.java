/**
 * 
 */
package webresources;

import models.Client;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import database.BadKeyException;
import database.DBClientQueries;
import database.NoDataStoreConnectionException;

import java.util.ArrayList;

/**
 * Client servlet to handle client processing.
 *  
 * @author Lawrence
 */
@Path("/business/{buisnessTag}/client")
public class ClientResource
{
	/**
	 * Getter for getting a client by its id.
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("{clientId}")
	public Client getWorkLog(@PathParam("buisnessTag") String businessTag,
			@PathParam("clientId") int clientId)
	{	
		Client result = null;
		try 
		{
			result = new DBClientQueries().getClient(clientId);
			
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
	public ArrayList<Client> getClients() 
	{	
		ArrayList<Client> result;
		
		try 
		{
			result = new DBClientQueries().getAllClients();
			
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
	 * Post method for creating a new client.
	 */
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public Response putWorkLog(@PathParam("buisnessTag") String businessTag, 
			Client client)
	{
		try 
		{
			new DBClientQueries().createClient(client);
			
			return Response.status(200).entity("").build();
		}
		catch (BadKeyException e)
		{
			return Response.status(404).entity("Tag already exists").build();
		}
		catch (NoDataStoreConnectionException e)
		{
			return Response.status(503).entity("No data store found").build();
		}
	}
}

