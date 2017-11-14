package resources.auth;

import models.users.Account;

import java.util.Objects;

/**
 * Class used for linking an account to the last time that account used this API service.
 *
 * @author Matt Rayner
 */
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
     * test if the last time used is still valid by comparing it to a timeout value.
     * <br />
     * this is calculated as the result of:<br /><code>System.currentTimeMillis() - this.getLastTimeUser() &lt; timeout;</code>
     * @param timeout the max age the last time used a account can have while still being valid.
     * @return <code>true</code> if the account is still valid.
     */
    public boolean isTimeValid(final long timeout)
    {
        return System.currentTimeMillis() - this.lastTimeUsed < timeout;
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
