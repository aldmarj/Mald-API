/**
 *
 */
package webresources;

import database.BadKeyException;
import database.DBClientQueries;
import database.NoDataStoreConnectionException;
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
import models.Client;

/**
 * Client servlet to handle client processing.
 *
 * @author Lawrence
 */
@Path("/business/{businessTag}/client")
public class ClientResource {

    /**
     * Getter for getting a client by its id.
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("{clientId}")
    public Client getClient(@PathParam("clientId") int clientId) {
        try {
            Client result = new DBClientQueries().getClient(clientId);

            if (result != null) {
                return result;
            } else {
                throw new WebApplicationException(Response.Status.NOT_FOUND);
            }
        } catch (NoDataStoreConnectionException e) {
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
    public ArrayList<Client> getClients(@PathParam("businessTag") final String businessTag) {
        ArrayList<Client> result = new ArrayList<Client>();

        try {
            result = new DBClientQueries().getAllClients(businessTag);
        } catch (NoDataStoreConnectionException e) {
            throw new WebApplicationException(Response.Status.BAD_GATEWAY);
        }

        return result;
    }

    /**
     * Post method for creating a new client.
     */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response putClient(@PathParam("businessTag") String businessTag,
        Client client) {
        try {
            // manually set businessTag if not present in model
            if (client.getBusinessTag().isEmpty()) {
                client.setBusinessTag(businessTag);
            }

            new DBClientQueries().createClient(client);

            return Response.status(200).entity("").build();
        } catch (BadKeyException e) {
            return Response.status(400).entity("Invalid businessTag").build();
        } catch (NoDataStoreConnectionException e) {
            throw new WebApplicationException(Response.Status.BAD_GATEWAY);
        }
    }
}

