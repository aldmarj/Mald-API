package models;

import models.users.Password;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public final class Account
{
    private String userName;

    private Password storedPassword;
    
    private String email;

    public Account()
    {
        this.userName = "";
        this.storedPassword = Password.fromHash("");
        this.email = "";    
    }
    
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

    public final Password getStoredPassword()
    {
        return this.storedPassword;
    }
    
    public final String getEmail()
    {
        return this.email;
    }

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public void setStoredPassword(Password storedPassword) {
		this.storedPassword = storedPassword;
	}

	public void setEmail(String email) {
		this.email = email;
	}
}
