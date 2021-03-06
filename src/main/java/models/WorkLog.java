package models;

import java.util.Date;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * Class representing a worklog.
 * 
 * @author Lawrence
 */
@XmlRootElement
public class WorkLog
{
	/** The worklog id a unique key to identify the worklog */
	private int workLogId;

	/** The username of the employee who submitted it */
    private String userName;

	/** The business tag of the employee who submitted it */
    private String businessTag;

	/** The client if of the client the work was done against */
    private int clientId;

    /** The beginning time of the worklog */
    private long startTime;

    /** The ending time of the worklog */
    private long endTime;

    /** A description of the worklog */
    private String description;
    
    /** The location of the worklog. */
    private Location location;

	/**
     * CLASS CONSTRUCTOR
     * 
     * @param workLogId - the id of the worklog.
     * @param userName - the username of the employee.
     * @param clientId - the cliend id of the client.
     * @param startTime - the starttime of the log.
     * @param endTime - the endtime of the log.
     * @param description - a free text description of work.
     */
    public WorkLog(final int workLogId, final String userName, final String businessTag, final int clientId,
                   final long startTime, final long endTime, final String description)
    {
        this.workLogId = workLogId;
        this.userName = userName;
        this.businessTag = businessTag;
        this.clientId = clientId;
        this.startTime = startTime;
        this.endTime = endTime;
        this.description = description;
    }
    
    /**
     * CLASS CONSTRUCTOR
     */
    public WorkLog()
    {
        this.workLogId = -1;
        this.userName = "";
        this.businessTag = "";
        this.clientId = -1;
        this.startTime = new Date().getTime();
        this.endTime = new Date().getTime();
        this.description = "";
        this.location = null;
    }
    
    /**
     * Getter for the work log id.
     * @return the work log id.
     */
    public int getWorkLogId()
    {
        return this.workLogId;
    }

    /**
     * Getter for the employee username.
     * @return the employee username.
     */
    public String userName()
    {
        return this.userName;
    }

    /**
     * Getter for the client Id.
     * @return the client id.
     */
    public int getClientId()
    {
        return this.clientId;
    }

    /**
     * Getter for the start time of the log in milliseconds.
     * @return the start time of the log in milliseconds.
     */
    public long getStartTime()
    {
        return this.startTime;
    }

    /**
     * Getter for the end time of the log in milliseconds.
     * @return the end time of the log in milliseconds.
     */
    public long getEndTime()
    {
        return this.endTime;
    }

    /**
     * Getter for the description.
     * @return the description.
     */
    public String getDescription()
    {
        return this.description;
    }
    
    /**
     * Getter for the username.
     * @return the username.
     */
    public String getUserName() 
    {
		return userName;
	}

    /**
     * Setter for the username.
     * @param userName to set.
     */
	public void setUserName(String userName) 
	{
		this.userName = userName;
	}

	/**
	 * Setter for the work log id.
	 * @param workLogId
	 */
	public void setWorkLogId(int workLogId) 
	{
		this.workLogId = workLogId;
	}

	/**
	 * Setter for the client id.
	 * @param clientId
	 */
	public void setClientId(int clientId) 
	{
		this.clientId = clientId;
	}

	/**
	 * Setter for the start time.
	 * @param startTime
	 */
	public void setStartTime(long startTime) 
	{
		this.startTime = startTime;
	}

	/**
	 * Setter for the end time.
	 * @param endTime
	 */
	public void setEndTime(long endTime) 
	{
		this.endTime = endTime;
	}

	/**
	 * Setter for the description.
	 * @param description
	 */
	public void setDescription(String description) 
	{
		this.description = description;
	}

	/**
	 * Returns the location of the worklog.
	 * 
	 * @return the location of the worklog.
	 */
    public Location getLocation() 
    {
		return location;
	}

    /**
     * Setter for the location of the worklog.
     * 
     * @param location the location to set.
     */
	public void setLocation(Location location) 
	{
		this.location = location;
	}
	
	/**
	 * Returns the businessTag of the worklog.
	 * 
	 * @return the businessTag of the worklog.
	 */
    public String getBusinessTag() 
    {
		return businessTag;
	}

    /**
     * Setter for the businessTag of the worklog.
     * 
     * @param businessTag the businessTag to set.
     */
	public void setBusinessTag(String businessTag) 
	{
		this.businessTag = businessTag;
	}
	
	/**
	 * Determines whether the worklog is valid or not.
	 * 
	 * @return the validity of the worklog.
	 */
	public boolean isValid()
	{
		return (this.getStartTime() < this.getEndTime()
				&& this.getClientId() > 0
				&& !this.userName.isEmpty()
				&& !this.businessTag.isEmpty());
	}
}
