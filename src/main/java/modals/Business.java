package modals;

public class Business
{
    private final int businessId;

    private final String businessName;

    private final String businessTag;

    public Business(final int businessId, final String businessName, final String businessTag)
    {
        this.businessId = businessId;
        this.businessName = businessName;
        this.businessTag = businessTag;
    }

    public int getBusinessId()
    {
        return this.businessId;
    }

    public String getBusinessName()
    {
        return this.businessName;
    }

    public String getBusinessTag()
    {
        return this.businessTag;
    }
}
