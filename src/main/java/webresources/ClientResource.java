/**
 * 
 */
package webresources;

import java.util.ArrayList;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import database.DBBusinessQueries;
import database.NoDataStoreConnectionException;
import models.Business;
import models.Client;

/**
 * Client servlet to handle client processing.
 *  
 * @author Lawrence
 */
@Path("/business/{buisnessTag}/clientId")
public class ClientResource
{
	/**
	 * Getter for getting a client by its id.
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("{clientId}")
	public Client getWorkLog(@PathParam("buisnessTag") String businessTag,
			@PathParam("client") int clientId)
	{	
		Client client = new Client();
		
		return client;
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
		ArrayList<Client> result = new ArrayList<Client>();
		
		Client client = new Client(10, "Frank", "baesystems");
		result.add(client);
		
		return result;
	}
	
	/**
	 * Post method for creating a new client.
	 */
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public Response putWorkLog(@PathParam("buisnessTag") String businessTag, 
			Client client)
	{
		return Response.status(200).entity("").build();
	}
}

