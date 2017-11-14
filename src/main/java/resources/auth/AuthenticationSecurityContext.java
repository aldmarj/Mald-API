package resources.auth;

import models.users.Account;

import javax.ws.rs.core.SecurityContext;
import java.security.Principal;

/**
 *
 */
public class AuthenticationSecurityContext implements SecurityContext
{
    private final Account account;
    private final boolean isSecure;

    AuthenticationSecurityContext(final Account account, final boolean isSecure)
    {
        this.account = account;
        this.isSecure = isSecure;
    }

    @Override
    public Principal getUserPrincipal()
    {
        return this.account;
    }

    @Override
    public boolean isUserInRole(final String role)
    {
        return "employee".equalsIgnoreCase(role); //TODO change if/when supporting roles.
    }

    @Override
    public boolean isSecure()
    {
        return this.isSecure;
    }

    @Override
    public String getAuthenticationScheme()
    {
        return SecurityContext.FORM_AUTH;
    }
}
