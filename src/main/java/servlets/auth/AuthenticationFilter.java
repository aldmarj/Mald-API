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

@Provider
@PreMatching
@Priority(Priorities.AUTHENTICATION)
public class AuthenticationFilter implements ContainerRequestFilter
{
    private static final String TOKEN_PARAMETER = "t";

    private static final Map<String, Account> AUTHENTICATED_ACCOUNTS = new HashMap<>();

    @Context
    private HttpServletRequest servletRequest;

    @Override
    public void filter(final ContainerRequestContext requestContext) throws IOException
    {
        final String token = servletRequest.getParameter(TOKEN_PARAMETER);
        if (token != null)
        {

        }
    }
}
