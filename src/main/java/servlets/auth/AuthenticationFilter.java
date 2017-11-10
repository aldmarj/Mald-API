package servlets.auth;

import models.users.Account;

import javax.annotation.Priority;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.PreMatching;
import javax.ws.rs.core.Context;
import javax.ws.rs.ext.Provider;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Provider
@PreMatching
@Priority(Priorities.AUTHENTICATION)
public class AuthenticationFilter implements ContainerRequestFilter
{
    private static final String TOKEN_PARAMETER = "t"; //NON-NLS

    private static final Map<String, Account> AUTHENTICATED_ACCOUNTS = new HashMap<>();

    @Context
    private HttpServletRequest servletRequest;

    private static String generateToken()
    {
        return UUID.randomUUID().toString().replaceAll("-","");
    }

    static String addAuthenticatedAccount(final Account account)
    {
        final String token = AuthenticationFilter.generateToken();
        AuthenticationFilter.AUTHENTICATED_ACCOUNTS.put(token, account);
        return token;
    }

    @Override
    public void filter(final ContainerRequestContext requestContext) throws IOException
    {
        final String token = this.servletRequest.getParameter(TOKEN_PARAMETER);
        if (token != null)
        {
            final Account account = AuthenticationFilter.AUTHENTICATED_ACCOUNTS.get(token);
            if (account != null)
            {
                requestContext.setSecurityContext(
                        new AuthenticationSecurityContext(account, this.servletRequest.getScheme()));
            }
        }
    }
}
