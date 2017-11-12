package resources.auth;

import database.DBAccountQueries;
import database.NoDataStoreConnectionException;
import models.users.Account;

import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import java.security.NoSuchAlgorithmException;
import java.security.Principal;

@Path("/login")
public class LoginResource
{
    @GET
    public String getName(@Context SecurityContext sc)
    {
        final Principal principal = sc.getUserPrincipal();
        return principal == null ? "null" : principal.getName();
    }

    @POST
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
            e.printStackTrace();
            throw new WebApplicationException(
                    "Could not connect to authentication database", e, Response.Status.SERVICE_UNAVAILABLE);
        }
        catch (final NoSuchAlgorithmException e)
        {
            e.printStackTrace();
            throw new WebApplicationException(
                    "Server could not authenticate password", e, Response.Status.INTERNAL_SERVER_ERROR);
        }
        throw new WebApplicationException("username or password is incorrect", Response.Status.FORBIDDEN);
    }
}
