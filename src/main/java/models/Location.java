/**
 * 
 */
package models;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * Class representing a location.
 * 
 * @author Lawrence
 */
@XmlRootElement
public class Location {

    private String postCode;

    private String description;
    
    public Location()
    {
    	this.postCode = "";
    	this.description = "";
    }
    
    public Location(final String postCode, final String description)
    {
    	this.postCode = postCode;
    	this.description = description;
    }

	public String getPostCode() 
	{
		return postCode;
	}

	public void setPostCode(String postCode) 
	{
		this.postCode = postCode;
	}

	public String getDescription() 
	{
		return description;
	}

	public void setDescription(String description) 
	{
		this.description = description;
	}
}
