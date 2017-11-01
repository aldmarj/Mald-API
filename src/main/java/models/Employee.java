package models;

import org.json.JSONObject;

public class Employee
{
    private final String firstName;

    private final String surName;

    private final Business business;

    private final Employee parent;

    private final String jobRole;

    private final Account account;
    
    public Employee(final Account account, final String firstName, final String surName, final Business business,
                    final Employee parent, final String jobRole)
    {
        this.account = account;
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
    
    public boolean hasParent()
    {
    	return this.parent != null;
    }

    public String getJobRole()
    {
        return this.jobRole;
    }
    
    public String getUserName()
    {
        return this.account.getUserName();
    }
    
    public Account getAccount()
    {
    	return this.account;
    }
    
    public JSONObject getJsonObject()
    {
		return new JSONObject()
				.put("userName", this.account.getUserName())
				.put("firstName", this.getFirstName())
				.put("surName", this.getSurName())
				.put("businessTag", this.getBusiness().getJsonObject())
				.put("parentUser", this.hasParent() ? this.parent.getJsonString() : null)
				.put("jobRole", this.getJobRole());
    }
    
    public String getJsonString()
    {
		return getJsonObject().toString();
    }
}
