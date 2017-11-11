package resources.auth;

import models.users.Account;

import javax.annotation.Priority;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Priorities;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.PreMatching;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;
import java.io.IOException;
import java.util.*;

@Provider
@PreMatching
@Priority(Priorities.AUTHENTICATION)
public class AuthenticationFilter implements ContainerRequestFilter
{
    private static final ResourceBundle AUTH_RB = ResourceBundle.getBundle("auth"); //NON-NLS
    private static final String TOKEN_PARAMETER = AUTH_RB.getString("auth.token.parameter");
    private static final long TOKEN_TIMEOUT = Long.parseLong(AUTH_RB.getString("auth.token.timeout"));

    private static final Map<String, AccountTracking> AUTHENTICATED_ACCOUNTS = new HashMap<>();
    private static final Collection<String> EXEMPT_PATHS;

    /**
     * Static initializer to populate the Exempt_Paths collection from the resourceBundle.
     */
    static
    {
        EXEMPT_PATHS = new ArrayList<>();
        int i = 0;
        while(AUTH_RB.containsKey("auth.exempt."+i)) //NON-NLS
        {
            EXEMPT_PATHS.add(AUTH_RB.getString("auth.exempt."+i)); //NON-NLS
            i++;
        }
    }

    @Context
    private HttpServletRequest servletRequest;

    /**
     *
     * @param account
     * @return
     */
    private static String generateToken(final Account account)
    {
        return UUID.randomUUID().toString().replaceAll("-","");
    }

    static String addAuthenticatedAccount(final Account account)
    {
        final String token = AuthenticationFilter.generateToken(account);
        AuthenticationFilter.AUTHENTICATED_ACCOUNTS.put(token, new AccountTracking(account));
        return token;
    }

    static void removeAuthenticatedAccount(final String token)
    {
        AuthenticationFilter.AUTHENTICATED_ACCOUNTS.remove(token);
    }

    static void removeAuthenticatedAccount(final Account account)
    {
        for(Map.Entry<String, AccountTracking> entry : AUTHENTICATED_ACCOUNTS.entrySet())
        {
            if (entry.getValue().getAccount().equals(account))
            {
                AuthenticationFilter.removeAuthenticatedAccount(entry.getKey());
                return;
            }
        }
    }

    @Override
    public void filter(final ContainerRequestContext requestContext) throws IOException
    {
        final String token = this.servletRequest.getParameter(TOKEN_PARAMETER);
        if (token != null)
        {
            final AccountTracking tracking = AuthenticationFilter.AUTHENTICATED_ACCOUNTS.get(token);
            if (tracking != null)
            {
                if (System.currentTimeMillis() - tracking.getLastTimeUsed() > TOKEN_TIMEOUT)
                {
                    removeAuthenticatedAccount(token);
                }
                else
                {
                    tracking.updateLastTimeUsed();
                    requestContext.setSecurityContext(
                            new AuthenticationSecurityContext(tracking.getAccount(), this.servletRequest.isSecure()));
                    return;
                }
            }
        }
        if (!AuthenticationFilter.EXEMPT_PATHS.contains(requestContext.getUriInfo().getPath()))
        {
            throw new WebApplicationException(Response.Status.FORBIDDEN);
        }
    }
}
