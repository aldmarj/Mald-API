package models;

import org.json.JSONObject;

/**
 * Class representing 
 * 
 * @author Lawrence
 */
public class Business
{
    private final String businessTag;

    private final String businessName;

    public Business(final String businessTag, final String businessName)
    {
        this.businessName = businessName;
        this.businessTag = businessTag;
    }

    public String getBusinessName()
    {
        return this.businessName;
    }

    public String getBusinessTag()
    {
        return this.businessTag;
    }
    
    public JSONObject getJsonObject()
    {
		return new JSONObject()
				.put("businessTag", businessTag)
				.put("businessName", businessName);
    }
    
    public String getJsonString()
    {
		return getJsonObject().toString();
    }
}
