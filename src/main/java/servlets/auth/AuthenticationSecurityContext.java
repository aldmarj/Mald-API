package servlets.auth;

import javax.ws.rs.core.SecurityContext;
import java.security.Principal;

public class AuthenticationSecurityContext implements SecurityContext
{
    private final Principal principal;
    private final String scheme;

    AuthenticationSecurityContext(final Principal principal, final String scheme)
    {
        this.principal = principal;
        this.scheme = scheme;
    }

    @Override
    public Principal getUserPrincipal()
    {
        return this.principal;
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
