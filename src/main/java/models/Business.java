package models;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * Class representing the business.
 * 
 * @author Lawrence
 */
@XmlRootElement
public class Business
{
	/** The business tag, also acts as the unique identifie */
    private String businessTag;

    /** The full name of the business */
    private String businessName;
    
    /**
     * CLASS CONSTRUCTOR
     */
    public Business()
    {
    	this.businessTag = new String();
    	this.businessName = new String();
    }

    /**
     * CLASS CONSTRUCTOR
     * 
     * @param businessTag - the business tag.
     * @param businessName - the business name
     */
    public Business(final String businessTag, final String businessName)
    {
        this.businessName = businessName;
        this.businessTag = businessTag;
    }

    /**
     * Getter for business name.
     * 
     * @return the business name.
     */
    public String getBusinessName()
    {
        return this.businessName;
    }

    /**
     * Getter for business tag.
     * 
     * @return the business tag.
     */
    public String getBusinessTag()
    {
        return this.businessTag;
    }
    
    /**
     * Setter for the business name.
     * 
     * @param businessName the business name.
     */
    public void setBusinessName(String businessName)
    {
        this.businessName = businessName;
    }

    /**
     * Setter for the business tag.
     * 
     * @param businessName the business tag.
     */
    public void setBusinessTag(String businessTag)
    {
        this.businessTag = businessTag;
    }
}
