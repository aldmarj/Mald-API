package servlets.auth;

import models.users.Account;

import java.util.Objects;

class AccountTracking
{
    private final Account account;

    private long lastTimestamp;

    public AccountTracking(final Account account)
    {
        this(account, System.currentTimeMillis());
    }

    public AccountTracking(final Account account, final long lastTimestamp)
    {
        this.account = Objects.requireNonNull(account, "Account cannot be null"); //NON-NLS
        this.lastTimestamp = lastTimestamp;
    }

    public Account getAccount()
    {
        return this.account;
    }

    public long getLastTimeUsed()
    {
        return this.lastTimestamp;
    }

    public long updateLastTimeUsed()
    {
        return this.lastTimestamp = System.currentTimeMillis();
    }

    @Override
    public String toString()
    {
        return this.account + ":" + this.lastTimestamp;
    }

    @Override
    public boolean equals(final Object obj)
    {
        if (!(obj instanceof AccountTracking))
        {
            return false;
        }
        final AccountTracking other = (AccountTracking) obj;
        return this.lastTimestamp == other.lastTimestamp && this.account.equals(other.account);
    }
}
