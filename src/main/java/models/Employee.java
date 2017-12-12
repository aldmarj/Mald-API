package models;

import models.users.Account;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * Model class to model and handle conversion between json.
 * The Json uses getters and setter to modify the model so they are all needed.
 * 
 * @author Lawrence
 */
@XmlRootElement
public class Employee
{
	/** The first name of the employee. **/
    private String firstName;

	/** The surname of the employee. **/
    private String surName;

	/** The manager of the employee's username. **/
    private String parentUserName;

	/** A description of the employees role. **/
    private String jobRole;

    /** The account details of the employee. **/
    private Account account;

    /** A optional field to return an amount of hours worked. Used
     * for dashboard purposes.
     **/
	private int hoursWorked;
    
	/**
	 * CLASS CONSTRUCTOR
	 */
	public Employee()
    {
        this.account = new Account();
        this.firstName = "";
        this.surName = "";
        this.parentUserName = null;
        this.jobRole = "";
        this.hoursWorked = -1;
    }
	
	/**
	 * CLASS CONSTRUCTOR
	 * 
	 * @param account - The account details of the employee.
	 * @param firstName - The first name of the employee.
	 * @param surName - The surname of the employee.
	 * @param parentUserName - The username of the employee.
	 * @param jobRole - A description of the employees job role.
	 */
    public Employee(final Account account, final String firstName, final String surName,
            final String parentUserName, final String jobRole)
	{
		this.account = account;
		this.firstName = firstName;
		this.surName = surName;
		this.parentUserName = parentUserName;
		this.jobRole = jobRole;
		this.hoursWorked = -1;
	}
    
	/**
	 * CLASS CONSTRUCTOR
	 * 
	 * @param account - The account details of the employee.
	 * @param firstName - The first name of the employee.
	 * @param surName - The surname of the employee.
	 * @param parentUserName - The username of the employee.
	 * @param jobRole - A description of the employees job role.
	 * @param hoursworked - The number of hours worked by the employee.
	 */
    public Employee(final Account account, final String firstName, final String surName,
                    final String parentUserName, final String jobRole, final Integer hoursWorked)
    {
        this.account = account;
        this.firstName = firstName;
        this.surName = surName;
        this.parentUserName = parentUserName;
        this.jobRole = jobRole;
        this.hoursWorked = hoursWorked;
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
        return this.account.getBusinessTag();
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

	public void setFirstName(String firstName)
	{
		this.firstName = firstName;
	}

	public void setSurName(String surName)
	{
		this.surName = surName;
	}

	public void setBusinessTag(String businessTag)
	{
		this.account.setBusinessTag(businessTag);
	}

	public void setParentUserName(String parentUserName)
	{
		this.parentUserName = parentUserName;
	}

	public void setJobRole(String jobRole) 
	{
		this.jobRole = jobRole;
	}

	public void setAccount(Account account) 
	{
		this.account = account;
	} 
	
    public int getHoursWorked() 
    {
		return hoursWorked;
	}

	public void setHoursWorked(int hoursWorked) 
	{
		this.hoursWorked = hoursWorked;
	}
	
	/**
	 * Check to see if the employee is valid.
	 * 
	 * @return true if the employee is valid.
	 */
	public boolean isValid()
	{
		return (!this.getBusinessTag().isEmpty() 
				&& this.account != null
				&& this.account.isValid());
	}
}
