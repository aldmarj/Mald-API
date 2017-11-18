package webresources.auth;

import models.users.Account;

import javax.annotation.Priority;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Priorities;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.PreMatching;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.PathSegment;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.ext.Provider;
import java.io.IOException;
import java.util.*;
import java.util.regex.Pattern;

/**
 * Filter that all requests pass through.
 * this allows the SecurityContext to be assigned for use by any resource.
 *
 * @author Matt Rayner
 */
@Provider
@PreMatching
@Priority(Priorities.AUTHENTICATION)
public class AuthenticationFilter implements ContainerRequestFilter
{
    private static final ResourceBundle AUTH_RB = ResourceBundle.getBundle("auth"); //NON-NLS
    private static final String TOKEN_PARAMETER = AUTH_RB.getString("auth.token.parameter");
    private static final long TOKEN_TIMEOUT = Long.parseLong(AUTH_RB.getString("auth.token.timeout"));

    private static final Map<String, AccountTracking> AUTHENTICATED_ACCOUNTS = new HashMap<>();
    private static final Collection<Pattern> EXEMPT_PATHS = new ArrayList<>();

    /*
      Static initializer to populate the Exempt_Paths collection from the resourceBundle.
     */
    static
    {
        int i = 0;
        while(AUTH_RB.containsKey("auth.exempt."+i)) //NON-NLS
        {
            EXEMPT_PATHS.add(Pattern.compile(AUTH_RB.getString("auth.exempt."+i))); //NON-NLS
            i++;
        }
    }

    /**
     * variable for the underlying {@link HttpServletRequest}.
     * this variable is assigned by JAX-RS.
     */
    @Context
    private HttpServletRequest servletRequest;

    /**
     * generate a unique token for each account.
     *
     * @param account the account the token will be mapped to.
     * @return the generated token.
     */
    private static String generateToken(final Account account)
    {
        String token;
        do
        {
            token = UUID.randomUUID().toString().replaceAll("-","");
        }while(AuthenticationFilter.AUTHENTICATED_ACCOUNTS.containsKey(token)); //ensure that the token has not been used;

        return token;
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

    private static String extractBusinessTag(final UriInfo uriInfo)
    {
        for (final Iterator<PathSegment> i = uriInfo.getPathSegments().iterator(); i.hasNext();)
        {
            if("business".equals(i.next().getPath()) && i.hasNext())
            {
                return i.next().getPath();
            }
        }
        return null;
    }

    public static boolean removeInvalidAccounts()
    {
        return AUTHENTICATED_ACCOUNTS.entrySet().removeIf(entry -> !entry.getValue().isTimeValid(TOKEN_TIMEOUT));
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
                if (!tracking.isTimeValid(TOKEN_TIMEOUT))
                {
                    removeAuthenticatedAccount(token);
                }
                else
                {
                    if (Objects.equals(extractBusinessTag(requestContext.getUriInfo()), tracking.getAccount().getBusinessTag()))
                    {
                        tracking.updateLastTimeUsed();
                        requestContext.setSecurityContext(
                                new AuthenticationSecurityContext(tracking.getAccount(), this.servletRequest.isSecure()));
                        return;
                    }
                }
            }
        }
        for (final Pattern exemptPath : EXEMPT_PATHS)
        {
            if (exemptPath.matcher(requestContext.getUriInfo().getPath()).matches())
            {
                return;
            }
        }
        throw new WebApplicationException(Response.Status.FORBIDDEN);
    }
}
