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
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import models.Client;
/**
 * Client servlet to handle client processing.
 *  
 * @author Lawrence
 */
@Path("/business/{buisnessTag}/clientId")
public class ClientServlet
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

