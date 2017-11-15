package models;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public final class Account
{
    private String userName;

    private String storedPassword;
    
    private String businessTag;

	private String email;

    public Account()
    {
        this.userName = "";
        this.storedPassword = "";
        this.businessTag = "";
        this.email = "";    
    }
    
    public Account(final String username, final String storedPassword,
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

    public final String getStoredPassword()
    {
        return this.storedPassword;
    }
    
    public final String getEmail()
    {
        return this.email;
    }

	public void setUserName(String userName) 
	{
		this.userName = userName;
	}

	public void setStoredPassword(String storedPassword) 
	{
		this.storedPassword = storedPassword;
	}

	public void setEmail(String email) 
	{
		this.email = email;
	}
	
    public String getBusinessTag() 
    {
		return businessTag;
	}

	public void setBusinessTag(String businessTag) 
	{
		this.businessTag = businessTag;
	}
}