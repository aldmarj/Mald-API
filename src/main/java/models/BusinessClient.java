package models;

import org.json.JSONObject;

public class BusinessClient
{
    private final int clientId;

    private final String clientName;

    private final Business business;

    public BusinessClient(final int clientId, final String clientName, final Business business)
    {
        this.clientId = clientId;
        this.clientName = clientName;
        this.business = business;
    }

    public int getClientId()
    {
        return this.clientId;
    }

    public String getClientName()
    {
        return this.clientName;
    }

    public Business getBusiness()
    {
        return this.business;
    }
    
    public JSONObject getJsonObject()
    {
		return new JSONObject()
				.put("clientId", this.clientId)
				.put("clientName", this.clientName)
				.put("business", this.business.getJsonObject());
    }
    
    public String getJsonString()
    {
		return getJsonObject().toString();
    }
}
