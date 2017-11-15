package resources.auth;

import database.DBAccountQueries;
import database.DBQueries;
import database.NoDataStoreConnectionException;
import models.users.Account;
import org.apache.log4j.Logger;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import java.security.NoSuchAlgorithmException;
import java.security.Principal;

/**
 * The login resource that provides a range of authentication methods. most notably the ability to login accounts.
 *
 * @author Matt Rayner
 */
@Path("/login")
public class LoginResource
{
    /** Logger **/
    private static final Logger LOGGER = Logger.getLogger(LoginResource.class);

    @GET
    public String getName(@Context SecurityContext sc)
    {
        final Principal principal = sc.getUserPrincipal();
        return principal == null ? "null" : principal.getName();
    }

    @POST
    @Consumes("application/x-www-form-urlencoded")
    public String login(@FormParam("u") String username, @FormParam("p") String password)
    {
        try
        {
            final Account account = new DBAccountQueries().getAccount(username);
            if (account != null && account.getStoredPassword().matches(password))
            {
                return AuthenticationFilter.addAuthenticatedAccount(account);
            }
        }
        catch (final NoDataStoreConnectionException e)
        {
            final String msg = "Could not connect to authentication database"; //todo externalise
            LOGGER.error(msg, e);
            throw new WebApplicationException(msg, e, Response.Status.SERVICE_UNAVAILABLE);
        }
        catch (final NoSuchAlgorithmException e)
        {
            final String msg = "Server could not authenticate password"; //todo externalise
            LOGGER.error(msg, e);
            throw new WebApplicationException(msg, e, Response.Status.INTERNAL_SERVER_ERROR);
        }
        LOGGER.info("user '" + username + "' failed to login"); //NON-NLS
        throw new WebApplicationException("username or password is incorrect", Response.Status.FORBIDDEN);
    }
}
