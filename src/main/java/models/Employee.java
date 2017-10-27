package models;

import models.users.Account;
import models.users.Password;

public class Employee extends Account
{
    private final String firstName;

    private final String surName;

    private final Business business;

    private final Employee parent;

    private final String jobRole;


    public Employee(final int accountId, final String username, final Password storedPassword,
                    final String firstName, final String surName, final Business business,
                    final Employee parent, final String jobRole)
    {
        super(accountId, username, storedPassword);
        this.firstName = firstName;
        this.surName = surName;
        this.business = business;
        this.parent = parent;
        this.jobRole = jobRole;
    }

    public String getFirstName()
    {
        return this.firstName;
    }

    public String getSurName()
    {
        return this.surName;
    }

    public Business getBusiness()
    {
        return this.business;
    }

    public Employee getParent()
    {
        return this.parent;
    }

    public String getJobRole()
    {
        return this.jobRole;
    }
}
