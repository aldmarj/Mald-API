package models.users;

import java.util.Collection;
import java.util.Collections;

public abstract class Account
{
    private final int accountId;

    private final String userName;

    private final Password storedPassword;

    private final Collection<Role> roles;

    public Account(final int accountId, final String username, final Password storedPassword)
    {
        this.accountId = accountId;
        this.userName = username;
        this.storedPassword = storedPassword;
        this.roles = Collections.singleton(Role.USER); //TODO change if/when we have more than one type of user.
    }

    public final int getAccountId()
    {
        return this.accountId;
    }

    public final String getUserName()
    {
        return this.userName;
    }

    public final Password getStoredPassword()
    {
        return this.storedPassword;
    }
}
