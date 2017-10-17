package modals;

public abstract class Account
{
    private final int accountId;

    private final String userName;

    private final String storedPassword;

    public Account(final int accountId, final String username, final String storedPassword)
    {
        this.accountId = accountId;
        this.userName = username;
        this.storedPassword = storedPassword;
    }

    public final int getAccountId()
    {
        return this.accountId;
    }

    public final String getUserName()
    {
        return this.userName;
    }

    public final String getStoredPassword()
    {
        return this.storedPassword;
    }
}
