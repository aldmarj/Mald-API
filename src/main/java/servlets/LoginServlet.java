package servlets;

import database.DBAccountQueries;
import database.NoDataStoreConnectionException;
import models.Account;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Path("/login")
public class LoginServlet
{
    private static final Map<UUID, Account> activeAccounts = new HashMap<>();

    @GET
    public boolean isAuthenticated()
    {
        return false;
    }

    @POST
    public String login(@FormParam("u") String username, @FormParam("p") String password)
    {
        try
        {
            final Account account = new DBAccountQueries().getAccount(username);
            if (account.getStoredPassword().matches(password))
            {
                if (!activeAccounts.containsValue(account))
                {
                    final UUID token = UUID.randomUUID();
                    activeAccounts.put(token, account);
                    return token.toString();
                }
            }
            throw new WebApplicationException(Response.Status.BAD_REQUEST);
        }
        catch (final NoDataStoreConnectionException | NoSuchAlgorithmException e)
        {
            e.printStackTrace();
            throw new WebApplicationException(e, Response.Status.SERVICE_UNAVAILABLE);
        }
    }
}
