/**
 *
 */
package webresources;

import database.DBClientQueries;
import exceptions.BadKeyException;
import exceptions.DataAccessException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.apache.log4j.Logger;

import models.Client;

/**
 * Client resource to handle client processing.
 *
 * @author Lawrence
 */
@Path("/business/{businessTag}/client")
public class ClientResource
{
    /** Logger **/
    private static final Logger LOGGER = Logger.getLogger(ClientResource.class);

	/**
	 * Getter for getting a client by its id.
	 * 
	 * @param clientId the id of the client to return.
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("{clientId}")
	public Client getWorkLog(@PathParam("clientId") int clientId)
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
		catch (DataAccessException e) 
		{
			throw new WebApplicationException(Response.Status.BAD_GATEWAY);		
		}
	}
	
	/**
	 * Returns all clients.
	 * 
	 * @param The business tag to get clients for.
	 * @return the requested client.
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON)
    public ArrayList<Client> getClients(@PathParam("businessTag") final String businessTag)
	{	
		ArrayList<Client> result;
		
		try 
		{
			result = new DBClientQueries().getAllClients(businessTag);
			
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
	 * Getter for getting the top clients in a range of time. 
	 * Sorted by those that have been worked for the most first.
	 * Does not count worklogs that aren't completely in the time period. i.e.
	 * Start and finish in the time period.
	 * 
	 * @param businessTag - The tag of the business to interrogate.
	 * @param startRange - The start of the range of clients to return, 1 for the first. (Non 0 indexed)
	 * @param endRange - The end of the range of clients to return, x for the xth. (Non 0 indexed)
	 * @param startTimeRange - The time in millisecond to start counting from.
	 * @param endTimeRange - The time in millisecond to stop counting from.
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("mostWorked/top/{startRange : \\d+}/{endRange : \\d+}/between/{startTimeRange : \\d+}/{endTimeRange : \\d+}")
	public Collection<Client> getClientbyMostWorkedRange(@PathParam("businessTag") String businessTag,
			@PathParam("startRange") int startRange, @PathParam("endRange") int endRange,
			@PathParam("startTimeRange") long startTimeRange, @PathParam("endTimeRange") long endTimeRange)
	{	
		Collection<Client> clients = new ArrayList<Client>();
		
		try 
		{
			// Minus one to the given values to account for 0th index.
			clients = new DBClientQueries().getAllClientsbyMostWorkedRangeBetweenTimes(
							businessTag, startRange - 1, endRange - 1, startTimeRange, endTimeRange);
			
			return clients;
		}
		catch (DataAccessException e) 
		{
			throw new WebApplicationException(Response.Status.BAD_GATEWAY);		
		}
	}
	
	/**
	 * Post method for creating a new client.
	 * 
	 * @param businessTag the id of the business to add to.
	 * @param client the client to add to the datastore.
	 */
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public String putClient(@PathParam("businessTag") String businessTag, 
			Client client)
	{
		String returnMessage;
		
		try 
		{
            client.setBusinessTag(businessTag);
            
            if (client.isValid())
            {
            	new DBClientQueries().createClient(client);
            	
    			returnMessage = "Successfully added new client: " + client.getClientId();
                LOGGER.info(returnMessage);
    			return returnMessage;
            }
            else
            {
            	returnMessage = "Given client is not valid. Must contain a name, businessTag and no id.";
                LOGGER.error(returnMessage);
                throw new WebApplicationException(returnMessage,
                		Response.status(Status.BAD_REQUEST).entity(returnMessage).build());
            }
		}
		catch (BadKeyException e)
		{
			returnMessage = "A key used already exists in the database";
            LOGGER.error(returnMessage, e);
            throw new WebApplicationException(returnMessage, e,
            		Response.status(Status.BAD_REQUEST).entity(returnMessage).build());
		}
		catch (DataAccessException e)
		{
			returnMessage = "No datastore found";
            LOGGER.error(returnMessage, e);
            throw new WebApplicationException(returnMessage, e,
            		Response.status(Status.SERVICE_UNAVAILABLE).entity(returnMessage).build());
		}
	}
	
	/**
	 * Post method for creating a new client.
	 * 
	 * @param businessTag the id of the business to add to.
	 * @param clients the clients to add to the datastore.
	 */
	@POST
	@Path("/import")
	@Consumes(MediaType.APPLICATION_JSON)
	public String putClients(@PathParam("businessTag") String businessTag, 
			List<Client> clients)
	{
		for (Client client : clients)
		{
			try {
				putClient(businessTag, client);
			}
			catch (WebApplicationException e)
			{
				LOGGER.info("Failed to add: " + client);
				LOGGER.info(e.getMessage());
			}
		}

		String returnMessage = "All clients created succesfully";
        LOGGER.info(returnMessage);
        return returnMessage;	
    }
}

