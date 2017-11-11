package servlets.auth;

import models.users.Account;

import javax.ws.rs.core.SecurityContext;
import java.security.Principal;

public class AuthenticationSecurityContext implements SecurityContext
{
    private final Account account;
    private final String scheme;

    AuthenticationSecurityContext(final Account account, final String scheme)
    {
        this.account = account;
        this.scheme = scheme;
    }

    @Override
    public Principal getUserPrincipal()
    {
        return this.account;
    }

    @Override
    public boolean isUserInRole(final String role)
    {
        return false;
    }

    @Override
    public boolean isSecure()
    {
        return "https".equalsIgnoreCase(this.scheme); //NON-NLS
    }

    @Override
    public String getAuthenticationScheme()
    {
        return SecurityContext.FORM_AUTH;
    }
}
