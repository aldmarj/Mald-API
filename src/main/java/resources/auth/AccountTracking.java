package resources.auth;

import models.users.Account;

import java.util.Objects;

class AccountTracking
{
    private final Account account;

    private long lastTimeUsed;

    /**
     * create a new Account Tracking with the current time as the last used time.
     *
     * @param account - the Account this Tracking is for.
     */
    public AccountTracking(final Account account)
    {
        this(account, System.currentTimeMillis());
    }

    /**
     * create a new Account Tracking with a specified last used time.
     *
     * @param account - the Account this Tracking is for.
     * @param lastTimeUsed - the last time the Account was used.
     */
    public AccountTracking(final Account account, final long lastTimeUsed)
    {
        this.account = Objects.requireNonNull(account, "Account cannot be null"); //NON-NLS
        this.lastTimeUsed = lastTimeUsed;
    }

    /**
     * Getter for the Account this tracking is for.
     *
     * @return the Account this tracking is for.
     */
    public Account getAccount()
    {
        return this.account;
    }

    /**
     * Getter for the timestamp of when the account was last used.
     *
     * @return the timestamp.
     */
    public long getLastTimeUsed()
    {
        return this.lastTimeUsed;
    }

    /**
     * Sets the last used time to <code>System.currentTimeMillis()</code>
     *
     * @return the new value of Last time used.
     */
    public long updateLastTimeUsed()
    {
        return this.lastTimeUsed = System.currentTimeMillis();
    }

    @Override
    public String toString()
    {
        return this.account + ": " + this.lastTimeUsed;
    }

    @Override
    public boolean equals(final Object obj)
    {
        if (!(obj instanceof AccountTracking))
        {
            return false;
        }
        final AccountTracking other = (AccountTracking) obj;
        return this.lastTimeUsed == other.lastTimeUsed && this.account.equals(other.account);
    }
}
