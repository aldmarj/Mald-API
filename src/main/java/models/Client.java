package models;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * Class representing the business.
 * 
 * @author Lawrence
 */
@XmlRootElement
public class Client
{
    private int clientId;

    private String clientName;

    private String businessTag;
    
    private long hoursWorked;

	private List<Location> locations;

    public Client()
    {
    	this(-1, "", "", -1);
    }
    
    public Client(final int clientId, final String clientName, final String businessTag)
    {
        this(clientId, clientName, businessTag, -1);
    }
    
    public Client(final int clientId, final String clientName, final String businessTag, 
    		final long hoursWorked)
    {
        this.clientId = clientId;
        this.clientName = clientName;
        this.businessTag = businessTag;
        this.hoursWorked = hoursWorked;
        this.locations = new ArrayList<Location>();
    }

    public int getClientId()
    {
        return this.clientId;
    }

    public String getClientName()
    {
        return this.clientName;
    }

	public String getBusinessTag() 
	{
		return businessTag;
	}

	public void setClientId(int clientId) 
	{
		this.clientId = clientId;
	}

	public void setClientName(String clientName) 
	{
		this.clientName = clientName;
	}

	public void setBusinessTag(String businessTag) 
	{
		this.businessTag = businessTag;
	}
	
    public List<Location> getLocations() 
    {
		return locations;
	}

	public void setLocations(List<Location> list) 
	{
		this.locations = list;
	}
	
    public long getHoursWorked() 
    {
		return hoursWorked;
	}

	public void setHoursWorked(long hoursWorked) 
	{
		this.hoursWorked = hoursWorked;
	}
	
	public boolean isValid()
	{
		return !this.clientName.isEmpty() && !this.businessTag.isEmpty() 
				&& this.clientId == -1;
	}
}
