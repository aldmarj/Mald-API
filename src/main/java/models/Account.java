package models;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.security.Principal;
import javax.xml.bind.annotation.XmlTransient;


@XmlRootElement
public final class Account implements Principal
{
	@XmlElement
    private String userName;

    private Password storedPassword;
    
	@XmlElement
    private String businessTag;

	@XmlElement(nillable = true)
    private String email;

    public Account()
    {
        this.userName = null;
        this.storedPassword = null;
        this.businessTag = null;
        this.email = null;
    }

    public Account(final String username, final Password storedPassword,
                   final String businessTag, final String email)
    {
        this.userName = username;
        this.storedPassword = storedPassword;
        this.businessTag = businessTag;
        this.email = email;
    }
    
    public String getUserName()
    {
        return this.userName;
    }

    public void setUserName(final String userName)
    {
        this.userName = userName;
    }
    
    @XmlTransient
    public Password getStoredPassword()
    {
        return this.storedPassword;
    }

    public void setStoredPassword(final Password storedPassword)
    {
        this.storedPassword = storedPassword;
    }

    public String getEmail()
    {
        return this.email;
    }

    public void setEmail(final String email)
    {
        this.email = email;
    }

    public String getBusinessTag()
    {
        return this.businessTag;
    }

    public void setBusinessTag(String businessTag)
    {
        this.businessTag = businessTag;
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
    
	/**
	 * Check to see if the account is valid.
	 * 
	 * @return true if the account is valid.
	 */
    public boolean isValid()
    {
    	return (!this.getBusinessTag().isEmpty()
    			&& !this.getUserName().isEmpty()
    			&& this.getStoredPassword() != null);
    }
}
