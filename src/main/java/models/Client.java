package models;

import java.util.ArrayList;

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

	private ArrayList<Location> locations;

    public Client()
    {
    	this.clientId = -1;
        this.clientName = "";
        this.businessTag = "";
        this.locations = new ArrayList<Location>();
    }
    
    public Client(final int clientId, final String clientName, final String businessTag)
    {
        this.clientId = clientId;
        this.clientName = clientName;
        this.businessTag = businessTag;
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
	
    public ArrayList<Location> getLocations() 
    {
		return locations;
	}

	public void setLocations(ArrayList<Location> locations) 
	{
		this.locations = locations;
	}
}
