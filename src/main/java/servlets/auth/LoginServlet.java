package servlets.auth;

import models.users.Account;
import models.users.Password;

import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.SecurityContext;
import java.security.Principal;

@Path("/login")
public class LoginServlet
{
    @GET
    public String getName(@Context SecurityContext sc)
    {
        final Principal principal = sc.getUserPrincipal();
        return principal == null ? null : principal.getName();
    }

    @POST
    public String login(@FormParam("u") String username, @FormParam("p") String password)
    {
        if(isPasswordCorrect(username, password))
        {
            return AuthenticationFilter.addAuthenticatedAccount(
                    new Account(username, Password.fromHash(password), "email"));
        }
        return null;
    }

    private boolean isPasswordCorrect(final String username, final String password)
    {
        return true; //TODO get from database
    }
}
