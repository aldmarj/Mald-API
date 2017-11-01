package models;

import org.json.JSONObject;

public final class Account
{
    private final String userName;

    private final String storedPassword;
    
    private final String email;

    public Account(final String username, final String storedPassword, final String email)
    {
        this.userName = username;
        this.storedPassword = storedPassword;
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
    
    public JSONObject getJsonObject()
    {
		return new JSONObject()
				.put("userName", this.getUserName())
				.put("storedPassword", this.getStoredPassword())
				.put("email", this.getEmail());
    }
    
    public String getJsonString()
    {
		return getJsonObject().toString();
    }
}
