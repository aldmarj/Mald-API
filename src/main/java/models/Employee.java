package models;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Employee
{
    private String firstName;

    private String surName;

    private String businessTag;

    private String parentUserName;

    private String jobRole;

    private Account account;
    
    public Employee()
    {
        this.account = new Account();
        this.firstName = "";
        this.surName = "";
        this.businessTag = "";
        this.parentUserName = null;
        this.jobRole = "";
    }
    
    public Employee(final Account account, final String firstName, final String surName, final String businessTag,
                    final String parentUserName, final String jobRole)
    {
        this.account = account;
        this.firstName = firstName;
        this.surName = surName;
        this.businessTag = businessTag;
        this.parentUserName = parentUserName;
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

    public String getBusinessTag()
    {
        return this.businessTag;
    }

    public String getParentUserName()
    {
        return this.parentUserName;
    }
    
    public boolean hasParent()
    {
    	return this.parentUserName != null;
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

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public void setSurName(String surName) {
		this.surName = surName;
	}

	public void setBusinessTag(String businessTag) {
		this.businessTag = businessTag;
	}

	public void setParentUserName(String parentUserName) {
		this.parentUserName = parentUserName;
	}

	public void setJobRole(String jobRole) {
		this.jobRole = jobRole;
	}

	public void setAccount(Account account) {
		this.account = account;
	}    
}
