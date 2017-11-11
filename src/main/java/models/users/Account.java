package models.users;

import javax.xml.bind.annotation.XmlRootElement;
import java.security.Principal;

@XmlRootElement
public final class Account implements Principal
{
    private String userName;

    private Password storedPassword;
    
    private String email;

    public Account(final String username, final Password storedPassword, final String email)
    {
        this.userName = username;
        this.storedPassword = storedPassword;
        this.email = email;
    }
    
    public String getUserName()
    {
        return this.userName;
    }

    public Password getStoredPassword()
    {
        return this.storedPassword;
    }
    
    public String getEmail()
    {
        return this.email;
    }

    @Override
    public String getName()
    {
        return this.userName;
    }

    @Override
    public String toString()
    {
        return this.userName;
    }

    @Override
    public boolean equals(final Object obj)
    {
        if (!(obj instanceof Account))
        {
            return false;
        }
        final Account otherAcc = (Account) obj;
        return this.userName.equals(otherAcc.userName)
                && this.storedPassword.equals(otherAcc.storedPassword)
                && this.email.equals(otherAcc.email);

    }
}
