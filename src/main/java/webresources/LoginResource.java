package webresources;

import database.DBAccountQueries;
import exceptions.DataAccessException;
import models.Account;

import org.apache.log4j.Logger;

import authentication.AuthenticationFilter;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.core.Response.Status;

import java.security.NoSuchAlgorithmException;
import java.security.Principal;

/**
 * The login resource that provides a range of authentication methods. most notably the ability to login accounts.
 *
 * @author Matt Rayner
 */
@Path("business/{businessTag}/login")
public class LoginResource
{
    /** Logger **/
    private static final Logger LOGGER = Logger.getLogger(LoginResource.class);

    /**
     * Get current user's name or null if there is no one logged in.
     * 
     * @param sc - the security context.
     * @return the name of the current user.
     */
    @GET
    public String getName(@Context SecurityContext sc)
    {
        final Principal principal = sc.getUserPrincipal();
        return principal == null ? "null" : principal.getName();
    }

    /**
     * Attempt to login to the API.
     * 
     * @param businessTag - The business context to login to.
     * @param username - The user to login to.
     * @param password - The password of the user to attempt to match.
     * @return a session key to use to access the API.
     */
    @POST
    @Consumes("application/x-www-form-urlencoded")
    public String login(@PathParam("businessTag") final String businessTag,
                        @FormParam("u") final String username,
                        @FormParam("p") final String password)
    {
        try
        {
            final Account account = new DBAccountQueries().getAccount(username, businessTag);
            if (account != null && account.getStoredPassword().matches(password))
            {
                return AuthenticationFilter.addAuthenticatedAccount(account);
            }
        }
        catch (final DataAccessException e)
        {
            final String msg = "Could not connect to authentication database"; //todo externalise
            LOGGER.error(msg, e);
            throw new WebApplicationException(msg, e, 
            		Response.status(Status.SERVICE_UNAVAILABLE).entity(msg).build());
        }
        catch (final NoSuchAlgorithmException e)
        {
            final String msg = "Server could not authenticate password"; //todo externalise
            LOGGER.error(msg, e);
            throw new WebApplicationException(msg, e, 
            		Response.status(Status.INTERNAL_SERVER_ERROR).entity(msg).build());
        }
        final String msg = "username or password is incorrect";
        LOGGER.info("user '" + username + "' failed to login"); //NON-NLS
        throw new WebApplicationException(msg,
        		Response.status(Status.FORBIDDEN).entity(msg).build());
    }
}
