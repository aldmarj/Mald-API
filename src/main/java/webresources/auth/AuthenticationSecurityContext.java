package webresources.auth;

import models.users.Account;

import javax.ws.rs.core.SecurityContext;
import java.security.Principal;

/**
 * Security context that uses an Account.
 *
 * @author Matt Rayner
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
        return role.equalsIgnoreCase("employee") //TODO change if/when supporting roles.
            || role.equalsIgnoreCase(this.account.getBusinessTag());
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
