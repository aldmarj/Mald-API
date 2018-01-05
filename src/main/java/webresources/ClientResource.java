/**
 *
 */
package webresources;

import database.DBClientQueries;
import exceptions.BadKeyException;
import exceptions.DataAccessException;

import java.util.ArrayList;
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

