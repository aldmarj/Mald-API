package models;

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
}
